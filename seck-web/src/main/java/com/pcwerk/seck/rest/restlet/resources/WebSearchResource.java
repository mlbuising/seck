package com.pcwerk.seck.rest.restlet.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pcwerk.seck.entities.DocumentSearchResult;
import com.pcwerk.seck.file.FileWalker;
import com.pcwerk.seck.file.Ranker;
import com.pcwerk.seck.file.SimpleRanker;
import com.pcwerk.seck.file.SimpleStemmer;
import com.pcwerk.seck.file.Stemmer;
import com.pcwerk.seck.rest.restlet.SeckWebRestletApp;

public class WebSearchResource extends ServerResource {

	private static Logger LOGGER = LoggerFactory.getLogger("WebSearch");

	private final String STEM_FILE = "/stemming.txt";
	private String startingSearchPath;
	private String stemFilePath;

	@Get
	public Representation processGetRequest(Representation r) {
		// Get servlet container of Restlet
		ServletContext sc = (ServletContext) getContext().getAttributes().get(
				"org.restlet.ext.servlet.ServletContext");

		// Return data for HTML Freemarker representation
		Map<String, Object> map = new HashMap<String, Object>();

		long startTime = System.currentTimeMillis();

		// Set the starting search path as 4 levels up from the servlet path.
		startingSearchPath = sc.getRealPath("../../../..");
		stemFilePath = startingSearchPath + STEM_FILE;

		Stemmer stemmer = new SimpleStemmer();
		stemmer.loadStemmingRules(new File(stemFilePath));

		// Get parameter from URI template /search/{searchTerm}
		String searchTerm = (String) getRequest().getAttributes().get(
				"searchTerm");
		map.put("searchTerm", searchTerm);
		// Set default value of searchTerm to empty String if null
		searchTerm = (searchTerm == null) ? "" : stemmer.stem(searchTerm);
		
		
		if (searchTerm.isEmpty()) {
			return toRepresentation(map, "search.html", MediaType.TEXT_HTML);
		}

		LOGGER.info(searchTerm);

		// File list that Filewalker will populate
		ArrayList<File> fileList = new ArrayList<File>();

		// Set the tomcat directory as the root path for crawler
		FileWalker fw = new FileWalker();
		fw.walk(startingSearchPath, fileList);
		// fw.walk(sc.getRealPath("."), fileList);

		// Set up ranker object
		Ranker rankedSearch = new SimpleRanker(fileList, stemmer);

		// TODO: Must format output as an HTML that resembles Google's search
		// results page. For now, search results are in JSON format.
		// return new JsonRepresentation(gson.toJson(rankedSearch
		// .getRankedSearchList(searchTerm)));
		List<DocumentSearchResult> rankedSearchList = rankedSearch
				.getRankedSearchList(searchTerm);

		long endTime = System.currentTimeMillis();
		double elapsedTime = ((double) endTime - (double) startTime) / 1000d;

		map.put("elapsedTime", elapsedTime);
		map.put("resultSize", rankedSearchList.size());
		map.put("rankedSearchResults", rankedSearchList);

		return toRepresentation(map, "searchresult.html", MediaType.TEXT_HTML);
	}

	public SeckWebRestletApp getApplication() {
		return (SeckWebRestletApp) super.getApplication();
	}

	private Representation toRepresentation(Map<String, Object> map,
			String templateName, MediaType mediaType) {
		return new TemplateRepresentation(templateName, getApplication()
				.getConfiguration(), map, mediaType);
	}
}
