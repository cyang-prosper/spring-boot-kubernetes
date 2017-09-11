package com.prosper.sbk.twitterwordcount.service;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.prosper.sbk.twitterwordcount.util.MapUtil;

@Service
public class TwitterWordCountService {
	
	private static Logger log = LoggerFactory.getLogger(TwitterWordCountService.class);

	private static final Set<String> suffixSymbols = Stream.of(",", ".", ":", ";", "?", "\"", "'", "%", ")", "}", "]", ">", "-")
			.collect(Collectors.toSet());
	
	private static final List<String> stopwords;
	static {
		try {
			File stopwordsFile = new ClassPathResource("stopwords.txt").getFile();
			stopwords = Files.readAllLines(stopwordsFile.toPath());
		}
		catch(Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
	}
	
	private Map<String, Integer> wordCountMap = new LinkedHashMap<>();
	
	
	
	/**
	 * 
	 * @param tweet
	 */
	public int processTweet(String tweet) {
//		log.debug("Processing tweet: "+tweet);
		
		if(tweet == null) {
			log.error("NULL tweet, ignored");
			return 0;
		}
		
//		tweet = "Prosper "+tweet; // Purposely added this line for demo rollback
		
		// Split words by space(s), filter irrelevant words and create a word list
		List<String> words = Stream
	            .of(tweet)
	            .map(w -> w.split("\\s+"))
	            .flatMap(Arrays::stream)
	            .filter(w -> w.length()>1) // filter out 1-character words
	            .map(w -> w.toLowerCase()) // force everything to lowercase
	            .filter(w -> { // filter out words not starting with an alphabet, @ or #
            			char firstChar = w.charAt(0);
            		 	return firstChar==35 || (firstChar>=64 && firstChar<=90) || (firstChar>=97 && firstChar<=122);
	            })
	            .map(w -> suffixSymbols.contains(w.substring(w.length()-1))? w.substring(0, w.length()-1): w) // trim off suffix symbols
//	            .filter(w -> !StopWords.stopWords.contains(w)) // filter out stop words
	            .filter(w -> !stopwords.contains(w))
	            .collect(Collectors.toList());
		
		// Populate the word count map
		synchronized(wordCountMap) {
			words.stream().forEach(w -> {
				if(wordCountMap.containsKey(w)) {
					wordCountMap.put(w, wordCountMap.get(w)+1);
				}
				else {
					wordCountMap.put(w, 1);
				}
			});
		}
		
		return words.size();
	}
	
	/**
	 * Print out the top 15 words every 5 seconds
	 */
	@Scheduled(fixedRate = 5000)
    public void logTop5Words() {
		processTweet("this is a test. test this if you can.");
		System.out.println(getTopNWords(15).toString());
	}
	
	
	/**
	 * 
	 * @param n
	 * @return
	 */
	public List<String> getTopNWords(int n) {
		
		synchronized(wordCountMap) {
			wordCountMap = MapUtil.sortByValue(wordCountMap);
			List<String> results = new ArrayList<>(n);
			int count=0;
			for(Map.Entry<String, Integer> entry: wordCountMap.entrySet()) {
				results.add(entry.getKey()+": "+entry.getValue());
				if(++count>=n) {
					break;
				}
			}
			return results;
		}
	}
	
	
}
