package com.pcwerk.seck.crawling.storage;

import com.pcwerk.seck.crawling.entities.ParsedDocument;

public interface DocumentStorage {

	/**
	 * Loads the storage file that contains the saved
	 * 
	 * @param filePath
	 *          Path of the storage file
	 * @return true if loading of storage file is successful; false, otherwise
	 */
	public boolean loadStorage(String filePath);

	/**
	 * Temporarily saves the document into a buffer for later writing to the
	 * individual files and
	 * 
	 * @param document
	 * @return true if saving to buffer is successful; false, otherwise
	 */
	public boolean save(ParsedDocument document);

	/**
	 * Check if the URL a parsed document already exists in the file.
	 * 
	 * @param url
	 * @return true if URL exists; false, otherwise
	 */
	public boolean contains(String url);

	/**
	 * Appends the contents of the parsed document buffer to the storage file and
	 * empties it afterwards.
	 * 
	 * @return
	 */
	public boolean flush();

}