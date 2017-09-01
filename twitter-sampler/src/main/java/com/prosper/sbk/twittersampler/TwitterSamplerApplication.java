package com.prosper.sbk.twittersampler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TwitterSamplerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterSamplerApplication.class, args);
	}
}
