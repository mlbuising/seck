package com.pcwerk.seck.extractor;
import java.io.File;
import java.io.IOException;

import com.pcwerk.seck.store.WebDocument;;

public abstract class Extractor {
	File file = null;
	
	public Extractor(File file)
	{
		this.file = file;
	}
	
	public abstract WebDocument extract() throws IOException;	
}
