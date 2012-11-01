package com.pcwerk.seck.crawling;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.pcwerk.seck.crawling.entities.ParsedDocument;

public class CrawlerTask implements Runnable {
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz");

	private ParsedDocument parsedDocument;

	private CrawlerTaskManager crawlerTaskManager;

	public CrawlerTask(ParsedDocument parsedDocument,
			CrawlerTaskManager crawlerTaskManager) {
		super();
		this.crawlerTaskManager = crawlerTaskManager;
		this.parsedDocument = parsedDocument;
	}

	public ParsedDocument getParsedDocument() {
		return parsedDocument;
	}

	public void run() {

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpHead httpHead = new HttpHead(this.parsedDocument.getUrl().toString());

			HttpResponse response = httpclient.execute(httpHead);

			// Retrieve just the last modified header value.
			Header lastModified = response.getFirstHeader("last-modified");
			parsedDocument.setDateLastModified(lastModified.getValue());

			// Retrieve body of the document
			Document jdoc = Jsoup.connect(this.parsedDocument.getUrl().toString())
					.get();
			parsedDocument.merge(jdoc);

			// Extract the links, create children documents, "stamp" with incremented
			// depth and add to frontier
			if (parsedDocument.getDepth() < crawlerTaskManager.getMaxDepth()) {
				for (String url : parsedDocument.keySet()) {
					try {
						ParsedDocument newDoc = new ParsedDocument(url,
								parsedDocument.getDepth() + 1);
						crawlerTaskManager.getFrontier().add(newDoc);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		CrawlerTaskManager.currentThreadCount--;
		return;
	}
}
