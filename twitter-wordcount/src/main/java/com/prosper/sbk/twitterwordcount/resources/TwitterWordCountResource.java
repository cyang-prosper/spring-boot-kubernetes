package com.prosper.sbk.twitterwordcount.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prosper.sbk.twitterwordcount.dto.TweetDto;
import com.prosper.sbk.twitterwordcount.service.TwitterWordCountService;

@RestController
@RequestMapping("/twitter")
public class TwitterWordCountResource {

	@Autowired
	private TwitterWordCountService twcService;
	
	
	@RequestMapping(value="/tweet", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public int processTweet(@RequestBody TweetDto tweet) {
		return twcService.processTweet(tweet.getContent());
	}
	
}
