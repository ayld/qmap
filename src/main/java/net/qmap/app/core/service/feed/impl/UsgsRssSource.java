package net.qmap.app.core.service.feed.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.Message;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;

public class UsgsRssSource implements MessageSource<SyndFeed>, InitializingBean {
    
	private static final String FEED_URL = "http://earthquake.usgs.gov/earthquakes/catalogs/eqs1hour-M0.xml";
	private static Logger LOG = LoggerFactory.getLogger(UsgsRssSource.class);
	
	private FeedFetcher feedFetcher;
	
	@Override
    public Message<SyndFeed> receive() {
		LOG.debug("Polling.");
		
    	final SyndFeed feed = getFeed();
    	
    	if (feed == null) {
    		
    		// unlikely to happen, but for correctness sake ...
    		throw new IllegalStateException("Unable to initialize feed at URL: " + FEED_URL);
    	}
    	
    	LOG.debug("Got feed: " + feed.getClass());
    	
    	LOG.debug("Sending message with feed as payload.");
    	
		return MessageBuilder.withPayload(feed).setHeader("feedid", "USGS").build();
    }

	private SyndFeed getFeed() {
		SyndFeed result = null;
    	try {
    		
			result = feedFetcher.retrieveFeed(new URL(FEED_URL));
		} catch (MalformedURLException e) {
			
			throw new IllegalStateException(e);
		} catch (IOException e) {
			
			throw new IllegalStateException(e);
		} catch (FeedException e) {
			
			throw new IllegalStateException(e);
		} catch (FetcherException e) {
			
			throw new IllegalStateException(e);
		}
		return result;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.feedFetcher = new HttpURLFeedFetcher();
	}
}
