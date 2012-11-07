package com.pcwerk.seck.extractor;

import java.io.File;
import org.apache.tika.Tika;
public class ExtractorFactory {
	
	public ExtractorFactory()
	{	
		//do nothing
	}

	public Extractor getExtractor(File file)
	{		
		Tika tika = new Tika();
		String mimeType = "";
		try {
			mimeType = tika.detect(file);			
			if(mimeType.equals("html/text")) 
				return new HtmlExtractor(file);
			else if(mimeType.startsWith("audio") || 
					mimeType.startsWith("image")) 
				return new MediaExtractor(file);
			else if(mimeType.startsWith("application")) 
				return new DocumentExtractor(file);			
			return null;
		} 
		catch (Exception e)
		{
			System.out.println("ExtractorFactory-getExtractor-Error: " + e.getMessage());
		}
		return null;	
	}	
	
/*
	public static void main( String[] args )
	{		
		System.out.println("start");
		ExtractorFactory efactory = new ExtractorFactory();
		String fileName = "";
		//fileName = "C:\\Getting Started.pdf";
		//fileName = "C:\\Book.xlsx";
		fileName = "C:\\test.docx";
		
		File file = new File(fileName);		
		if(file.exists())
		{		
			Extractor ex = efactory.getExtractor(file);	
			try {
				if(ex!=null)
				{ 
					WebDocument webDoc = ex.extract();			
					Metadata m =  webDoc.getMetadata();
					for (String name : m.names()) 
					    System.out.println( "key:" + name + ",value:" + m.get(name));	
				}
			} catch (IOException e) {
			}
		}
		else
		{
			System.out.println("File Not found.");
		}
		System.out.println("end");
	}
	*/
}
