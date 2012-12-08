package com.pcwerk.seck.classification.categorized.hierarchy;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DirectoryCrawlerTask implements Runnable {
	public static final SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz");

	private ParsedDocument parsedDocument;

	private DirectoryCrawlerTaskManager crawlerTaskManager;

	public DirectoryCrawlerTask(ParsedDocument parsedDocument,
			DirectoryCrawlerTaskManager crawlerTaskManager) {
		super();
		this.crawlerTaskManager = crawlerTaskManager;
		this.parsedDocument = parsedDocument;
	}

	public ParsedDocument getParsedDocument() {
		return parsedDocument;
	}

	public void run() {
		try {
			// Retrieve body of the document
			Document jdoc = Jsoup.connect(this.parsedDocument.getUrl().toString())
					.get();
			parsedDocument.merge(jdoc);

			String[] fragments = parsedDocument.getUrl().getPath().split("/");

			try {
				System.out.println(fragments[1]);
				File rootdir = new File("./categories");
				if (!(rootdir.exists())) {
					rootdir.mkdir();
				}

				File d = new File("./categories/" + fragments[1]);
				if (!d.exists()) {
					d.mkdir();
				}
			} catch (IndexOutOfBoundsException e) {
			}

			if (parsedDocument.getDepth() < crawlerTaskManager.getMaxDepth()) {
				// Extract the links, create children documents, "stamp" with
				// incremented depth and add to frontier
				for (String strUrl : parsedDocument.getLinks().keySet()) {
					try {
						URL url = new URL(strUrl);

						if (url.getHost().equals(crawlerTaskManager.getRootUrl().getHost())
								&& !crawlerTaskManager.getSeenUrls().contains(strUrl)
								&& url.getQuery() == null) {

							ParsedDocument newDoc = new ParsedDocument(url,
									parsedDocument.getDepth() + 1);
							crawlerTaskManager.getFrontier().add(newDoc);
						}
					} catch (MalformedURLException e) {

					} catch (Exception e) {
						e.printStackTrace();
						System.exit(0);
					}
				}
			}

			crawlerTaskManager.getSeenUrls().add(
					this.parsedDocument.getUrl().toString());
		} catch (IOException e) {
		} catch (Exception e) {
		} finally {
			DirectoryCrawlerTaskManager.currentThreadCount--;
		}
		return;
	}
}
