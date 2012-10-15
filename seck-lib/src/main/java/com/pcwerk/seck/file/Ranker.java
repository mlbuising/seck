package com.pcwerk.seck.file;

import java.util.List;

import com.pcwerk.seck.entities.DocumentSearchResult;

public interface Ranker {
	
	public List<DocumentSearchResult> getRankedSearchList(String searchTerm);
	
}
