package com.pcwerk.seck.extractor;

import java.io.File;
import java.io.IOException;

import com.pcwerk.seck.blob.Bloblet;

public class HtmlExtractor extends Extractor {

	public HtmlExtractor(File file) {
		super(file);
	}

	public Bloblet extract(String baseUrl)
	{
		return new Bloblet();
	}

	public Bloblet extract() throws IOException {
		return new Bloblet();
	}	
}
