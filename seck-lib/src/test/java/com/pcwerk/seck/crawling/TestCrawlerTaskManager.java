package com.pcwerk.seck.crawling;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class TestCrawlerTaskManager {

	private final int MAX_THREAD = 5;
	private final int MAX_DEPTH = 1;

	private String[] seedUrls = new String[] { "http://www.calstatela.edu/",
			"http://www.w3.org/", "http://www.yahoo.com/",
			"http://www.microsoft.com", "http://www.google.com",
			"http://www.glendale.edu", "http://www.lausd.net" };

	@Test
	public void test() {
		CrawlerTaskManager ctm = new CrawlerTaskManager(MAX_THREAD, MAX_DEPTH);

		Set<String> seedUrlSet = new HashSet<String>(Arrays.asList(seedUrls));

//		ctm.start(seedUrlSet);
	}

}
