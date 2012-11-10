package com.pcwerk.seck.util;

import java.net.MalformedURLException;
import java.net.URL;

public class URLNormalizationUtil {

	public static String getNormalizedUrl(URL baseUrl, String uri)
	{
		if(uri.toLowerCase().startsWith("javascript")||
				uri.toLowerCase().startsWith("mailto"))
		{
		     return uri.toLowerCase();
		}
		else
		{
			try {
			   //System.out.println("\n\n\nBASE URL: " + baseUrl.toString());
			   //System.out.println("BEFORE NORMALIZED URL: " + uri);
	 		   URL normalizedUrl = new URL(baseUrl, uri);
	          // System.out.println("NORMALIZED URL:"  + normalizedUrl.toString());
	           return normalizedUrl.toString().toLowerCase();
			} catch (MalformedURLException e) {
			   return uri.toLowerCase();
			}
		}
	}
	
	public static URL getBaseUrl(URL pageUrl) 
	{	
		URL baseUrl;
		try {
			baseUrl = new URL( pageUrl.getProtocol() + "://" + pageUrl.getHost() );
		} catch (MalformedURLException e) {
			return pageUrl;
		}
		return baseUrl;
	}
}
