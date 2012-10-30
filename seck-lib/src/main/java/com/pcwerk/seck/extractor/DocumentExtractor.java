package com.pcwerk.seck.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import com.pcwerk.seck.blob.Bloblet;

public class DocumentExtractor extends Extractor {

	public DocumentExtractor(File file) {
		super(file);
	}
	
	public Bloblet extract() throws IOException {
		System.out.println("DocumentExtractor-Enter");
		FileInputStream is = null;
		Bloblet b = new Bloblet();
	    try {
	      is = new FileInputStream(this.file);
	      ContentHandler contenthandler = new BodyContentHandler();
	      Metadata metadata = new Metadata();
	      //metadata.set(Metadata.RESOURCE_NAME_KEY, this.file.getName());
	      ParseContext parseContext = new ParseContext();
	      Parser parser = new AutoDetectParser();
	      parser.parse(is, contenthandler, metadata, parseContext);			
		   b.setMetadata(metadata);
		   
	    }catch(Exception e)
	    {
	    	System.out.println("DocumentExtractor-Error");
	    }
		System.out.println("DocumentExtractor-Exit");
		return b;
	}	
}
