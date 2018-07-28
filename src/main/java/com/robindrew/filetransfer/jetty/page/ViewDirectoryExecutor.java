package com.robindrew.filetransfer.jetty.page;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.robindrew.common.http.ContentType;
import com.robindrew.common.http.servlet.executor.IHttpExecutor;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.http.servlet.response.IHttpResponse;
import com.robindrew.filetransfer.descriptor.DirectoryDescriptor;

public class ViewDirectoryExecutor implements IHttpExecutor {

	private static final Logger log = LoggerFactory.getLogger(ViewDirectoryExecutor.class);

	@Override
	public void execute(IHttpRequest request, IHttpResponse response) {

		// Get the directory
		File directory = new File(request.getString("directory"));
		boolean pretty = request.getBoolean("pretty", true);

		// Sanity checks
		if (!directory.exists()) {
			throw new IllegalArgumentException("directory does not exist: " + directory);
		}
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("file is not a directory: " + directory);
		}

		// List the files
		Stopwatch timer = Stopwatch.createStarted();
		log.info("[ViewDirectory] {}", directory);

		DirectoryDescriptor descriptor = new DirectoryDescriptor(directory);
		String json = descriptor.toJson(pretty);

		timer.stop();
		log.info("[ViewDirectory] {} (in {})", directory, timer);

		// Finished
		response.ok(ContentType.APPLICATION_JSON, json);
	}

}
