package com.prosper.sbk.twittersampler.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Service
public class TwitterService {
	
	private static final Logger log = LoggerFactory.getLogger(TwitterService.class);
	
	private static final String twitterWordCountUrl = "http://twitter-wordcount-service:8080/twitter/tweet";
	
	private ThreadLocal<TwitterStream> twitterStreamLocal = new ThreadLocal<>();
	
	/**
	 * Automatically start the sampling
	 */
	@PostConstruct
	private void init() {
//		start();
	}
	
	@Scheduled(fixedRate = 5000)
    public void logTop5Words() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.TEXT_PLAIN);
		String body = "You describe a desired state in a Deployment object, and the Deployment controller changes the actual state to the desired state at a controlled rate. You can define Deployments to create new ReplicaSets, or to remove existing Deployments and adopt all their resources with new Deployments.";
		HttpEntity<?> requestEntity = new HttpEntity<Object>(body, requestHeaders);

		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.exchange(twitterWordCountUrl, HttpMethod.POST, requestEntity, String.class);
		log.info("Response: "+response);
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
