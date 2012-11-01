package com.pcwerk.seck.crawling.entities;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParsedDocument extends ConcurrentSkipListMap<String, String>
		implements Comparable<ParsedDocument> {
	private static final long serialVersionUID = 1L;

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz");

	private URL url;
	private Date dateLastModified;
	private String title;
	private String body;
	private int depth;

	public int getDepth() {
		return depth;
	}

	public ParsedDocument(URL url, Date dateLastModified, String title,
			String body) {
		super();
		this.url = url;
		this.dateLastModified = dateLastModified;
		this.title = title;
		this.body = body;
	}

	public ParsedDocument(String url, String dateLastModified, String title,
			String body) throws ParseException, MalformedURLException {
		this(new URL(url), sdf.parse(dateLastModified), title, body);
	}

	public ParsedDocument(String url) throws MalformedURLException {
			this.url = new URL(url);
	}
	
	public ParsedDocument(String url, int depth) throws MalformedURLException {
		this(url);
		this.depth = depth;
}

	public URL getUrl() {
		return url;
	}

	public Date getDateLastModified() {
		return dateLastModified;
	}

	public String getDateLastModifiedString() {
		if(dateLastModified == null) {
			return "";
		}
		return sdf.format(dateLastModified);
	}

	public String getTitle() {
		return title;
	}

	public void merge(ParsedDocument pd) {
		if (this.url.equals(pd.url)) {
			this.title = pd.title;
			this.dateLastModified = pd.dateLastModified;
			this.body = pd.body;
		}
	}

	public void merge(Document d) {

		if (this.url.toString().equals(d.baseUri())) {

			this.title = d.title();
			this.body = d.body().text();

			Elements links = d.select("a");

			for (Element link : links) {
				this.put(link.attr("abs:href"), link.text());
			}
		}
	}

	public String getBody() {
		return body;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public void setUrl(String url) throws MalformedURLException {
		this.url = new URL(url);
	}

	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public void setDateLastModified(String date) throws ParseException {
		this.dateLastModified = sdf.parse(date);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "ParsedDocument [url=" + url + ", dateLastModified="
				+ getDateLastModifiedString() + ", title=" + title + ", keySet="
				+ this.keySet().toString() + "]";
	}

	public int compareTo(ParsedDocument o) {
		return this.url.toString().compareTo(o.url.toString());
	}

	public boolean equals(ParsedDocument o) {
		return this.getUrl().equals(o.getUrl());

	}

}