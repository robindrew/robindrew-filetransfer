package com.robindrew.filetransfer.descriptor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robindrew.common.io.Files;
import com.robindrew.common.text.Strings;
import com.robindrew.common.util.Threads;

public class DirectoryDescriptor {

	private final String path;
	private final Set<String> directories = new TreeSet<>();
	private final Set<FileDescriptor> files = new TreeSet<>();

	public DirectoryDescriptor(File directory) {
		this.path = directory.getAbsolutePath();

		// Asynchronous for efficiency on large files ...
		ExecutorService service = Executors.newFixedThreadPool(4);

		// List the files
		List<Future<FileDescriptor>> futures = new ArrayList<>();
		for (File file : Files.listFiles(directory, false)) {
			if (file.isDirectory()) {
				directories.add(file.getName());
			} else {
				futures.add(service.submit(() -> new FileDescriptor(file)));
			}
		}

		// Shutdown the asynchronous execution
		Threads.drainAndShutdown(service, futures, files);
	}

	public String getPath() {
		return path;
	}

	public Set<String> getDirectories() {
		return ImmutableSet.copyOf(directories);
	}

	public Set<FileDescriptor> getFiles() {
		return ImmutableSet.copyOf(files);
	}

	public String toJson(boolean pretty) {

		// Build the JSON response
		GsonBuilder builder = new GsonBuilder();
		if (pretty) {
			builder.setPrettyPrinting();
		}
		Gson gson = builder.create();
		return gson.toJson(this);
	}

	@Override
	public String toString() {
		return Strings.toString(this);
	}
}
