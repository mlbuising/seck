package com.pcwerk.seck.file;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;

public class StemmedFileSearch extends FileSearch {
	private static String REGEX = "[,.!\\s+<>/]";
	private Pattern pattern = Pattern.compile(REGEX);
	private Stemmer stemmer;

	public StemmedFileSearch(Stemmer stemmer) {
		this.stemmer = stemmer;
	}

	@SuppressWarnings("resource")
	public int count(String needle, File haystack) {
		int count = 0;
		Scanner input;

		try {
			input = new Scanner(haystack);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

		while (input.hasNext()) {
			String line = input.nextLine();
			String[] words = pattern.split(line);
			for (String word : words) {
				if (stemmer.stem(word).equalsIgnoreCase(needle)) {
					count++;
				}
			}
		}

		return count;
	}
}
