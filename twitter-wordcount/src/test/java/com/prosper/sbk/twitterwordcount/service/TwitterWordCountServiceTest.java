package com.prosper.sbk.twitterwordcount.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TwitterWordCountServiceTest {
	
	private static Logger log = LoggerFactory.getLogger(TwitterWordCountServiceTest.class);
	
	@Autowired
	private TwitterWordCountService twcService;
	
	@Test
	public void testProcessTweet() {
		String tweet = "Kubernetes is an open-source system for automating deployment, scaling, and management of containerized applications. Kubernetes is open source giving you the freedom to take advantage of on-premises, hybrid, or public cloud infrastructure, letting you effortlessly move workloads to where it matters to you";
		twcService.processTweet(tweet);
		log.info(twcService.getTopNWords(3).toString());
	}
	
}
