package com.pcwerk.seck.crawling.storage;

import com.google.gson.Gson;
import com.pcwerk.seck.crawling.entities.ParsedDocument;
import java.io.*;

public class FileDocumentStorage {
	public void save(ParsedDocument pd){
		Gson gson = new Gson();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("out.txt"));
			bw.write(gson.toJson(pd));
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
