package com.pcwerk.seck.crawling;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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
		System.out.println("[i]   Current thread count: "
				+ (++CrawlerTaskManager.currentThreadCount));

		System.out.println("[i]   Starting crawler thread ...");

		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(this.parsedDocument.getUrl().toString());

			HttpResponse response = httpclient.execute(httpget);

			// System.out.println("Last modified: "
			// + ((String)response.getHeaders("last-modified")));
			// Date dateLastModified =
			// sdf.parse(response.getHeaders("last-modified").);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				System.out.println("Parsing Document");

				Document jdoc = Jsoup.connect(this.parsedDocument.getUrl().toString())
						.get();

				// Document jdoc = Jsoup.parse(EntityUtils.toString(entity));
				System.out.println(jdoc.body().text());

				System.out.println(jdoc.baseUri());

				parsedDocument.merge(jdoc);
				System.out.println(parsedDocument);
				// parsedDocument.setDateLastModified(dateLastModified);
			}
			for (String url : parsedDocument.keySet()) {
				try {
					ParsedDocument newDoc = new ParsedDocument(url);
					crawlerTaskManager.getFrontier().add(newDoc);
				} catch (MalformedURLException e) {

				}
				System.out.println("[i]   Added " + url + " to frontier.");
			}

		} catch (IOException e) {
			// e.printStackTrace();
			// // } catch (ParseException e) {
			// // e.printStackTrace();
		}

		CrawlerTaskManager.currentThreadCount--;

		return;
		// try {
		// crawlerTaskManager.getFrontier().add(
		// new ParsedDocument("http://www.acm.org"));
		// } catch (MalformedURLException e) {
		// System.out.println("Invalid URL to crawl.");
		// }

		// System.out.println("[" + sdf.format(new Date())
		// + "] Crawler task finished...");

	}
}
