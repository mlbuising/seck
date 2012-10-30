package com.pcwerk.seck.extractor;

import java.io.File;
public class ExtractorFactory {
	
	public ExtractorFactory()
	{	
		//do nothing
	}

	public Extractor getExtractor(File file)
	{
		System.out.println("ExtractorFactory.getExtractor()-Enter");
		//To do: check fileType to different extractors
		//...
		
		//return new HtmlExtractor(file);
				
		//return new MediaExtractor(file);
		
		System.out.println("ExtractorFactory.getExtractor()-Exit");
		return new DocumentExtractor(file);		
	}	
	
	/*
	public static void main( String[] args )
	{		
		ExtractorFactory efactory = new ExtractorFactory();
		//String fileName = "../downloaded files/Getting Started.pdf";
		//String fileName = "../downloaded files/Book.xlsx";
		String fileName = "../downloaded files/test.docx";
		
		File file = new File(fileName);
		
		if(file.exists())
		{		
			System.out.println("Found file.");
			Extractor ex = efactory.getExtractor(file);	
			try {
				Bloblet b = ex.extract();			
				Metadata m =  b.getMetadata();
				for (String name : m.names()) 
				    System.out.println("metadata: " + name + " - " + m.get(name));				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("done.");		
	}
	*/
}
