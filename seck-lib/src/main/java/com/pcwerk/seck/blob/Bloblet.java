package com.pcwerk.seck.blob;

import java.util.ArrayList;
import java.util.List;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.sax.Link;

public class Bloblet {
	private String url;
	private String hash;
	private String location;
	private String type;
	private List<Link> links;
	private Metadata metadata;
	
	public Bloblet()
	{
		url = "";
		hash = "";
		location = "";
		type = "";
		links = new ArrayList<Link>();
	    metadata = null;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
}
