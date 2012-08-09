package net.qmap.app.core.service.feed.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.ServiceActivator;

public class UsgsErrorHandler {
	
	private static Logger LOG = LoggerFactory.getLogger(UsgsErrorHandler.class);
	
	@ServiceActivator
	public void handle(Message<?> error) {
		if (error == null) {
			
			LOG.error("Message is null");
		}
		LOG.error(error.getPayload().toString());
	}
}
