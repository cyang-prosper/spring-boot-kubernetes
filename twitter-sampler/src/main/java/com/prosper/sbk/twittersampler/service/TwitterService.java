package com.prosper.sbk.twittersampler.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Service
public class TwitterService {
	
	private static final Logger log = LoggerFactory.getLogger(TwitterService.class);
	
	private ThreadLocal<TwitterStream> twitterStreamLocal = new ThreadLocal<>();
	
	/**
	 * Automatically start the sampling
	 */
	@PostConstruct
	private void init() {
		start();
	}
	
	/**
	 * Start sampling
	 */
	public void start() {
		if(twitterStreamLocal.get()!=null) {
			stop();
		}
		
	    StatusListener listener = new StatusListener(){
	        public void onStatus(Status status) {
	            log.info(status.getUser().getName() + ": " + status.getText());
	        }
	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
	        public void onException(Exception ex) {
	            ex.printStackTrace();
	        }
			public void onScrubGeo(long userId, long upToStatusId) {}
			public void onStallWarning(StallWarning warning) {}
	        
	    };
	    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	    twitterStream.addListener(listener);
	    // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
	    twitterStream.sample("en");
	    twitterStreamLocal.set(twitterStream);
	}
	
	/**
	 * Stop sampling
	 */
	public void stop() {
		twitterStreamLocal.get().shutdown();
	}
	
}
