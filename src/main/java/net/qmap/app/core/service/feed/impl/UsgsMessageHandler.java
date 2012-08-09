package net.qmap.app.core.service.feed.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.qmap.app.core.dao.MapEventDao;
import net.qmap.app.core.domain.MapEvent;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.transaction.annotation.Transactional;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

public class UsgsMessageHandler {

	private static Logger LOG = LoggerFactory.getLogger(UsgsMessageHandler.class);
	
	private MapEventDao dao;

	@Transactional
	@ServiceActivator
	public void handle(Message<SyndFeed> message) throws MessagingException {
		LOG.debug("Handling.");
		
		if (message == null) {
			
			LOG.error("Message is null, nothing to do.");
			return;
		}
		
		LOG.debug("Handling: " + message.getClass());
		
		final Object payload = message.getPayload();
		if (payload == null) {
			
			LOG.error("Payload is null, nothing to do.");
			return;
		}
		
		LOG.debug("Received message with payload: " + payload.getClass());
		
		final SyndFeed feed = (SyndFeed) payload;
		
		LOG.debug("Getting last event date.");

		Date lastEventDate = dao.getLastEventPublishedDate();
		if (lastEventDate == null) {
			
			LOG.info("last event date is null, setting it in the past");
			final Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 1984);
			cal.set(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			
			lastEventDate = cal.getTime();
		}
		
		LOG.debug("Got last event date: " + lastEventDate);
		
		@SuppressWarnings("unchecked")
		final List<SyndEntry> syndFeedItems = (List<SyndEntry>) feed.getEntries();
		if (syndFeedItems == null || syndFeedItems.isEmpty()) {
			
			LOG.error("Feed has no items, nothing to do.");
			return;
		}
		for (SyndEntry feedEntry : syndFeedItems) {
			
			final Date publishDate = feedEntry.getPublishedDate();
			
			final boolean eventAlreadyHappened = lastEventDate != null && (publishDate.before(lastEventDate) || publishDate.equals(lastEventDate));
			if (eventAlreadyHappened) {
				
				LOG.debug("Event already happerned, continuing");
				// don't store event, it should already have been stored
				continue;
			}
			
			LOG.debug("Creating map event.");
			
			final MapEvent event = new MapEvent();

			// publish date
			event.setPublishDate(publishDate);
			
			LOG.debug("Set publish date.");
			
			// event date
//			feedEntry.getModules().get(0);
			
			@SuppressWarnings("rawtypes")
			final List foreignMarkup = (List) feedEntry.getForeignMarkup();
			
			// latitude
			final Element lat = (Element) foreignMarkup.get(0);
			final String latitude = lat.getText();
			
			event.setLatitude(Double.valueOf(latitude));
			
			LOG.debug("Set lat.");
			
			// longitude
			final Element lng = (Element) foreignMarkup.get(1);
			final String longitude = lng.getText();
			
			event.setLongitude(Double.valueOf(longitude));
			
			LOG.debug("Set lon.");
			
			// magnitude
			final String title = feedEntry.getTitle();
			final String[] splitTitle = title.split(",");
			
			final String stringMagnitude = splitTitle[0].trim();
			final String[] splitMagnitude = stringMagnitude.split(" ");

			final String decimalMagnitude = splitMagnitude[1];
			
			event.setMagnitude(Double.valueOf(decimalMagnitude));
			
			LOG.debug("Set mag.");
			
			// location
			final String location = splitTitle[1].trim();
			
			event.setLocation(location);
			
			LOG.debug("Set loc.");
			
			// persist
			LOG.debug("Persisting.");
			
			dao.create(event);
			
			LOG.debug("Persisted.");
		}
		LOG.debug("Handling done.");
	}

	@Required
	public void setMapEventDao(MapEventDao mapEventDao) {
		this.dao = mapEventDao;
	}
}
