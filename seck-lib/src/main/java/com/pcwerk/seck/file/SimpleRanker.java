package com.pcwerk.seck.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pcwerk.seck.entities.DocumentSearchResult;

public class SimpleRanker implements Ranker {

	private List<File> fileList;

	private Stemmer stemmer;

	private int totalDocsN;
	private int docsWithTermN;

	public SimpleRanker(List<File> fileList, Stemmer stemmer) {
		this.fileList = fileList;
		this.stemmer = stemmer;
		this.totalDocsN = fileList.size();
		this.docsWithTermN = 0;
	}

	public List<DocumentSearchResult> getRankedSearchList(String searchTerm) {
		List<DocumentSearchResult> rankedSearchResults = doRankedSearch(searchTerm);

		for (DocumentSearchResult result : rankedSearchResults) {
			double tfIdf = calcTfIdf(result);
			if (tfIdf > 0d) {
				result.setScore(tfIdf);
			} else {
				rankedSearchResults.remove(result);
			}
		}

		Collections.sort(rankedSearchResults);

		return rankedSearchResults;
	}

	private List<DocumentSearchResult> doRankedSearch(String searchTerm) {
		List<DocumentSearchResult> rankedSearchResults = new ArrayList<DocumentSearchResult>();

		if (!stemmer.getStemPatterns().isEmpty()) {
			for (File file : fileList) {
				StemmedFileSearch rfs = new StemmedFileSearch(stemmer);
				int docTf = rfs.count(stemmer.stem(searchTerm), file);
				if (docTf > 0) {
					String parentPath = File.separator.equals("/") ? "../../../../"
							: "..\\..\\..\\..\\";

					DocumentSearchResult result = new DocumentSearchResult(file
							.getPath()
							.substring(file.getPath().indexOf(parentPath))
							.replace(parentPath, "." + File.separator), docTf);

					rankedSearchResults.add(result);
					docsWithTermN++;
				}
			}
		}

		return rankedSearchResults;
	}

	private double calcTfIdf(DocumentSearchResult result) {
		double tfIdf = 0d;

		try {
			tfIdf = result.getTf()
					/ (Math.log10((double) totalDocsN / (double) docsWithTermN));
		} catch (Exception e) {
			tfIdf = -1d;
		}
		return tfIdf;

	}

	public int getDocsWithTermN() {
		return docsWithTermN;
	}

}
