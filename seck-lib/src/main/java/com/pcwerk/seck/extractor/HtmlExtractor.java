package com.pcwerk.seck.extractor;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.pcwerk.seck.store.WebDocument;

public class HtmlExtractor extends Extractor {

	public HtmlExtractor(File file) {
		super(file);
	}

	public WebDocument extract(URL baseUrl)
	{
		return new WebDocument();
	}

	public WebDocument extract() throws IOException {
		return new WebDocument();
	}	
}
