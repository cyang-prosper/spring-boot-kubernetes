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
	
	private static HttpHeaders requestHeaders = new HttpHeaders();
	static {
		requestHeaders.setContentType(MediaType.TEXT_PLAIN);
	}
	
	private ThreadLocal<TwitterStream> twitterStreamLocal = new ThreadLocal<>();
	private RestTemplate restTemplate = new RestTemplate();
	
	/**
	 * Automatically start the sampling
	 */
	@PostConstruct
	private void init() {
		start();
	}
	
	public void sendTweetToWordCount(String tweet) {
		HttpEntity<?> requestEntity = new HttpEntity<Object>(tweet, requestHeaders);
		ResponseEntity<String> response = restTemplate.exchange(twitterWordCountUrl, HttpMethod.POST, requestEntity, String.class);
//		log.info("Response: "+response);
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
	            log.debug(status.getUser().getName() + ": " + status.getText());
	            sendTweetToWordCount(status.getText());
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
	
	
	/**
	 * Print out the top 15 words every 5 seconds
	 */
	@Scheduled(fixedRate = 5000)
    public void logEnvVariables() {
		log.info("oauth.consumerKey: "+System.getenv("oauth.consumerKey"));
	}
	

}
