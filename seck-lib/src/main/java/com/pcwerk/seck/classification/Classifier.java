package com.pcwerk.seck.classification;

import java.io.IOException;
import java.net.URL;

import weka.core.Instance;

public interface Classifier {
	public void initCategories(String hierarchySource);
	public void setTrainingData(String filePath) throws Exception;
	public void classify(String documentsPath, String categoryRoot) throws Exception;
}
