package com.pcwerk.seck.extractor;
import java.io.File;
import java.io.IOException;

import com.pcwerk.seck.blob.Bloblet;

public abstract class Extractor {
	File file = null;
	
	public Extractor(File file)
	{
		this.file = file;
	}
	
	public abstract Bloblet extract() throws IOException;	
}
