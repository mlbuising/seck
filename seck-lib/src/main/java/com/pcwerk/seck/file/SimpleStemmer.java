package com.pcwerk.seck.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleStemmer implements Stemmer {

	@SuppressWarnings("unused")
	private static Logger LOGGER = LoggerFactory.getLogger("SimpleStemmer");

	private List<String[]> stemPatterns;

	public List<String[]> getStemPatterns() {
		return stemPatterns;
	}

	public SimpleStemmer() {
		stemPatterns = new ArrayList<String[]>();

	}

	public String stem(final String searchTerm) {
		String stemmedTerm = searchTerm.toLowerCase();

		if (!stemPatterns.isEmpty()) {
			for (String[] rule : stemPatterns) {
				Pattern p = Pattern.compile(rule[0]);
				Matcher m = p.matcher(stemmedTerm);
				stemmedTerm = m.replaceFirst(rule[1]);
			}
		}

		return stemmedTerm;
	}

	public boolean loadStemmingRules(File stemFile) {

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(stemFile));

			String line = "";

			while ((line = br.readLine()) != null) {

				if (line.matches("\\^[A-Za-z]+")) {
					stemPatterns
							.add(new String[] { "^" + line.substring(1), "" });
				} else if (line.matches("&[A-Za-z]+\\=[A-Za-z]+")) {
					stemPatterns.add(new String[] {
							line.substring(line.indexOf('=') + 1) + "$",
							line.substring(1, line.indexOf('=')) });
				} else if (line.matches("\\$[A-Za-z]+")) {
					stemPatterns
							.add(new String[] { line.substring(1) + "$", "" });
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}
}
