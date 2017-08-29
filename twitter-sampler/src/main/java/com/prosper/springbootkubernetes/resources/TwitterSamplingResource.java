package com.prosper.springbootkubernetes.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prosper.springbootkubernetes.dto.TwitterSamplingCommandDto;
import com.prosper.springbootkubernetes.service.TwitterService;

@RestController
@RequestMapping("/tweets/samplings")
public class TwitterSamplingResource {
	
	@Autowired
	private TwitterService twitterService;
	
	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public void executeSamplingCommand(@RequestBody TwitterSamplingCommandDto samplingCommand) {
		switch(samplingCommand.getCommand()) {
		case START:
			twitterService.start();
			break;
		case STOP:
			twitterService.stop();
			break;
		}
	}
}
