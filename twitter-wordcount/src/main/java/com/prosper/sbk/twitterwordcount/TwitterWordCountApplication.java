package com.prosper.sbk.twitterwordcount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TwitterWordCountApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterWordCountApplication.class, args);
	}
	
}
