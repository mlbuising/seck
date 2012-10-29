package com.pcwerk.seck.crawling.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCrawlerUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger("TestCrawlerUtils");

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz");

	private final String[] url = new String[] { "http://www.yahoo.com",
			"http://www.w3.org/robots.txt", "http://jsoup.org/robots.txt" };

	private final String ROBOTS_PATH = "C:/Users/Mick/Desktop/CS454IR/workspace/seck/seck-lib/src/test/java/com/pcwerk/seck/crawling/utils/robots.txt";

	// @Test
	public void testParsedDocuments() throws ClientProtocolException,
			IOException, ParseException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url[1]);

		HttpResponse response = httpclient.execute(httpget);

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String body = EntityUtils.toString(entity);
			// LOGGER.info(body);
			Set<String> disallowedPatterns = CrawlerUtils
					.getDisallowedUriSet(body);

			// for (Iterator iterator = disallowedPatterns.iterator(); iterator
			// .hasNext();) {
			// String string = (String) iterator.next();
			// LOGGER.info(string);
			// }
		}
	}

	@Test
	public void testParsedDocumentsLocal() throws IOException {

		BufferedReader br = new BufferedReader(
				new FileReader(new File(ROBOTS_PATH)));
		String line = "";

		StringBuilder body = new StringBuilder();

		while ((line = br.readLine()) != null) {
			body.append(line + "\n");
		}

		Pattern disallowedPatterns = CrawlerUtils
				.getDisallowedUriPattern(new String(body));

		Matcher m = disallowedPatterns.matcher("Disallow: /blog/my-feed/feed/");
		if(m.matches()) {
			LOGGER.info("Matched");
		}
	}
}
