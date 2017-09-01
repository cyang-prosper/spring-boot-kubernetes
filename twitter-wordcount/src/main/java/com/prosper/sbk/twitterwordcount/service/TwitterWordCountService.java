package com.prosper.sbk.twitterwordcount.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.prosper.sbk.twitterwordcount.util.MapUtil;

@Service
public class TwitterWordCountService {
	
	private static Logger log = LoggerFactory.getLogger(TwitterWordCountServiceTest.class);
	
	private static final Set<String> stopWords = Stream.of("a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the")
			.collect(Collectors.toSet());
	
	private static final Set<String> suffixSymbols = Stream.of(",", ".", ":", ";", "?", "\"", "'", "%", ")", "}", "]", ">", "-")
			.collect(Collectors.toSet());
	
	
	private Map<String, Integer> wordCountMap = new LinkedHashMap<>();
	
	/**
	 * 
	 * @param tweet
	 */
	public int processTweet(String tweet) {
		log.info("Processing tweet: "+tweet);
		
		if(tweet == null) {
			log.error("NULL tweet, ignored");
			return 0;
		}
		
		// Split words by space(s), filter irrelevant words and create a word list
		List<String> words = Stream
	            .of(tweet)
	            .map(w -> w.split("\\s+"))
	            .flatMap(Arrays::stream)
	            .map(w -> w.trim())
	            .filter(w -> w.length()>1) // filter out 1-character words
	            .map(w -> w.toLowerCase()) // force everything to lowercase
	            .filter(w -> { // filter out words not starting with an alphabet, @ or #
            			char firstChar = w.charAt(0);
            		 	return firstChar==35 || (firstChar>=64 && firstChar<=90) || (firstChar>=97 && firstChar<=122);
	            })
	            .map(w -> suffixSymbols.contains(w.substring(w.length()-1))? w.substring(0, w.length()-1): w) // trim off suffix symbols
	            .filter(w -> !stopWords.contains(w)) // filter out stop words
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
	 * Print out the top 5 words every 5 seconds
	 */
	@Scheduled(fixedRate = 5000)
    public void logTop5Words() {
		System.out.println(getTopNWords(5).toString());
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
