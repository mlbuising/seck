package com.pcwerk.seck.crawling.entities;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestParsedDocument {
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz");
		
	private final String[] url = new String[] { "http://www.yahoo.com",
			"http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html",
			"http://jsoup.org/download" };

	@Test
	public void testParsedDocuments() throws ClientProtocolException, IOException, ParseException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url[1]);

		HttpResponse response = httpclient.execute(httpget);

//		System.out.println(Arrays.toString(response.getAllHeaders()));

		Date dateLastModified = sdf.parse(response.getHeaders("last-modified").toString());

		
		
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// System.out.println(EntityUtils.toString(entity));
		}

	}
}
