package com.pcwerk.seck.file;

import java.io.File;
import java.util.List;

public interface Stemmer {

	public String stem(String searchTerm);

	public boolean loadStemmingRules(File stemFile);

	public List<String[]> getStemPatterns();

}
