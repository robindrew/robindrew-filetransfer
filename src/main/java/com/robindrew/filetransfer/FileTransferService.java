package com.robindrew.filetransfer;

import com.robindrew.common.service.AbstractService;
import com.robindrew.common.service.component.heartbeat.HeartbeatComponent;
import com.robindrew.common.service.component.logging.LoggingComponent;
import com.robindrew.common.service.component.properties.PropertiesComponent;
import com.robindrew.common.service.component.stats.StatsComponent;
import com.robindrew.filetransfer.jetty.JettyComponent;

public class FileTransferService extends AbstractService {

	/**
	 * Entry point for the File Transfer Server Service.
	 */
	public static void main(String[] args) {
		FileTransferService service = new FileTransferService(args);
		service.startAsync();
	}

	private final JettyComponent jetty = new JettyComponent();
	private final HeartbeatComponent heartbeat = new HeartbeatComponent();
	private final PropertiesComponent properties = new PropertiesComponent();
	private final LoggingComponent logging = new LoggingComponent();
	private final StatsComponent stats = new StatsComponent();

	public FileTransferService(String[] args) {
		super(args);
	}

	@Override
	protected void startupService() throws Exception {
		start(properties);
		start(logging);
		start(heartbeat);
		start(stats);
		start(jetty);
	}

	@Override
	protected void shutdownService() throws Exception {
		stop(jetty);
		stop(heartbeat);
	}
}
