package com.pcwerk.seck.crawling.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pcwerk.seck.crawling.entities.ParsedDocument;

public class CrawlerUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger("CrawlerUtils");

	public static Set<String> getDisallowedUriSet(String rules) {
		Set<String> patternStrings = new HashSet<String>();

		Pattern pattern = Pattern.compile("Disallow: (.*)");
		Matcher matcher;

		String[] ruleEntries = rules.split("\\r?\\n");

		for (int i = 0; i < ruleEntries.length; i++) {
			matcher = pattern.matcher(ruleEntries[i]);

			if (matcher.matches()) {
				patternStrings.add(matcher.group());
			}
		}

		return patternStrings;
	}

	public static Pattern getDisallowedUriPattern(String rules) {
		return getDisallowedUriPattern(getDisallowedUriSet(rules));
	}

	public static Pattern getDisallowedUriPattern(Set<String> uriSet) {
		Pattern pattern;
		StringBuilder strPattern = new StringBuilder();

		for (String uri : uriSet) {
			// strPattern.append("\\Q");
			uri = uri.replace("*", ".*");
			// LOGGER.info(uri);
			strPattern.append(uri);
			// strPattern.append("\\E");
			strPattern.append("|");
		}

		strPattern.deleteCharAt(strPattern.length() - 1);

		LOGGER.info(strPattern.toString());

		pattern = Pattern.compile(strPattern.toString());

		return pattern;
	}
}
