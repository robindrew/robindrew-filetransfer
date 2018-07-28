package com.robindrew.filetransfer.descriptor;

import static com.robindrew.common.text.Strings.bytes;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.robindrew.common.codec.Md5FileEncoder;
import com.robindrew.common.text.Strings;

public class FileDescriptor implements Comparable<FileDescriptor> {

	private static final Logger log = LoggerFactory.getLogger(FileDescriptor.class);

	private static final String hash(File file) {
		Md5FileEncoder encoder = new Md5FileEncoder();

		// Because this operation can take a long time for large files, we have added logging
		try {
			Stopwatch timer = Stopwatch.createStarted();
			byte[] bytes = encoder.encodeToBytes(file);
			timer.stop();
			if (timer.elapsed().getSeconds() >= 1) {
				log.info("[Hash] {} to digest file: '{}' ({})", timer, file.getAbsolutePath(), bytes(file.length()));
			}
			return Strings.hex(bytes);

		} catch (Exception e) {
			// Files can be protected, in use, etc
			log.warn("Unable to hash file: '" + file.getAbsolutePath() + "'", e);
			return "";
		}
	}

	private final String name;
	private final long length;
	private final long lastModified;
	private final String hash;

	public FileDescriptor(File file) {
		this.name = file.getName();
		this.length = file.length();
		this.lastModified = file.lastModified();
		this.hash = hash(file);
	}

	public String getName() {
		return name;
	}

	public long getLength() {
		return length;
	}

	public long getLastModified() {
		return lastModified;
	}

	public String getHash() {
		return hash;
	}

	@Override
	public int compareTo(FileDescriptor that) {
		return String.CASE_INSENSITIVE_ORDER.compare(this.name, that.name);
	}

	@Override
	public String toString() {
		return Strings.toString(this);
	}
}