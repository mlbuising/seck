package com.pcwerk.seck.classification.categorized.hierarchy;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.annotations.Expose;

//public class ParsedDocument extends ConcurrentSkipListMap<String, String>
public class ParsedDocument implements Comparable<ParsedDocument> {
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz");

	@Expose
	private URL url;
	@Expose
	private Date dateLastModified;
	@Expose
	private String title;
	@Expose
	private String body;
	@Expose
	private Map<String, String> links;
	private int depth;

	public ParsedDocument() {
		this.title = "";
		this.body = "";
	}

	public ParsedDocument(String url) throws MalformedURLException {
		this(new URL(url), 0);
	}

	public ParsedDocument(String url, int depth) throws MalformedURLException {
		this(new URL(url), depth);
	}

	public ParsedDocument(URL url) {
		this(url, 0);
	}

	public ParsedDocument(URL url, int depth) {
		this.url = url;
		this.title = "";
		this.body = "";
		this.links = new ConcurrentSkipListMap<String, String>();
		this.depth = depth;
	}

	public ParsedDocument(String url, String dateLastModified, String title,
			String body) throws ParseException, MalformedURLException {
		this(new URL(url), sdf.parse(dateLastModified), title, body);
	}

	public ParsedDocument(URL url, Date dateLastModified, String title,
			String body) {
		super();
		this.url = url;
		this.dateLastModified = dateLastModified;
		this.title = (title == null || title.equals("null")) ? "" : title;
		this.body = (body == null) ? "" : body;
		this.links = new ConcurrentSkipListMap<String, String>();
	}

	public URL getUrl() {
		return url;
	}

	public Date getDateLastModified() {
		return dateLastModified;
	}

	public String getDateLastModifiedString() {
		if (dateLastModified == null) {
			return "";
		}
		return sdf.format(dateLastModified);
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public Map<String, String> getLinks() {
		return links;
	}

	public int getDepth() {
		return depth;
	}

	public void merge(ParsedDocument pd) {
		if (this.url.equals(pd.url)) {
			this.title = (pd.getTitle() == null || pd.getTitle().equals("null")) ? ""
					: pd.title;
			this.dateLastModified = pd.dateLastModified;
			this.body = pd.body == null ? "" : pd.body;
		}
	}

	public void merge(Document d) {

		if (this.url.toString().equals(d.baseUri())) {
			this.title = (d.title() == null || d.title().equals("null")) ? ""
					: d.title();
			// this.body = d.body().toString() == null ? "" : body;
			this.body = d.body().toString();

			Elements links = d.select("a");

			for (Element link : links) {
				if (!link.attr("abs:href").isEmpty()) {
					String absHref = link.attr("abs:href");
					absHref = absHref.charAt(absHref.length() - 1) == '/' ? absHref
							.substring(0, absHref.length() - 1) : absHref;
					this.links.put(absHref, link.text());
				}
			}
		}
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
		this.title = (title == null || title.equals("null")) ? "" : title;
	}

	public void setBody(String body) {
		this.body = (body == null) ? "" : body;
	}

	@Override
	public String toString() {
		return "ParsedDocument [\n\turl=" + url + "\n\tdateLastModified="
				+ getDateLastModifiedString() + "\n\ttitle=" + this.getTitle()
				+ "\n\tbody=" + this.getBody() + "\n\tlinks=\n"
				+ this.linksToString() + "\n]";
	}

	public int compareTo(ParsedDocument o) {
		return this.url.toString().compareTo(o.url.toString());
	}

	public boolean equals(ParsedDocument o) {
		return this.getUrl().equals(o.getUrl());
	}

	private String linksToString() {
		StringBuilder sb = new StringBuilder();

		Iterator<String> it = this.links.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			sb.append("\t\t{" + key + " : " + this.links.get(key) + "}");
			if (it.hasNext()) {
				sb.append("\n");
			}
		}

		return sb.toString();
	}

}
