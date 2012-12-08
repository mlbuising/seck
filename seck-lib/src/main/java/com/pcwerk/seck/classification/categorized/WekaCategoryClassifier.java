package com.pcwerk.seck.classification.categorized;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;

import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.UnassignedClassException;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pcwerk.seck.classification.Classifier;
import com.pcwerk.seck.classification.categorized.hierarchy.DirectoryCrawlerTaskManager;
import com.pcwerk.seck.classification.categorized.hierarchy.ParsedDocument;
import com.pcwerk.seck.search.HBaseImpl;

public class WekaCategoryClassifier implements Classifier {

	private J48 trainedClassifier;

	public void initCategories(String hierarchySource) {
		DirectoryCrawlerTaskManager dctm = new DirectoryCrawlerTaskManager(5, 1);
		try {
			dctm.start(hierarchySource);
		} catch (MalformedURLException e) {
			System.out.println("[i]    Category initialization failed.");
		}
	}

	public void setTrainingData(String categoryRoot) throws Exception {
		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setDirectory(new File(categoryRoot));
		Instances dataRaw = loader.getDataSet();

		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(dataRaw);
		Instances dataFiltered = Filter.useFilter(dataRaw, filter);

		this.trainedClassifier = new J48();
		this.trainedClassifier.buildClassifier(dataFiltered);
		System.out.println(trainedClassifier);

		System.out.println(dataFiltered.numClasses());
		// for (int i = 0; i < dataFiltered.numClasses(); i++) {
		//
		// }
	}

	public void classify(String documentsPath, String categoryRoot)
			throws Exception {
		setTrainingData(categoryRoot);

		HBaseImpl hbase = new HBaseImpl();
		try {
			hbase.creatTable("classifiedDocs", new String[] { "classify" });
		} catch (Exception e) {
			e.printStackTrace();
		}

		File storageFile = new File(documentsPath);

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();

		BufferedReader br = null;

		try {
			String line;
			int count = 0;
			br = new BufferedReader(new FileReader("./" + storageFile));

			while ((line = br.readLine()) != null) {
				ParsedDocument doc = gson.fromJson(line, ParsedDocument.class);

				try {
					// FastVector atts = new FastVector(2);

					FastVector atts = new FastVector(1);
					atts.addElement(new Attribute("url", (FastVector) null));
					atts.addElement(new Attribute("body", (FastVector) null));

					Instances dataRaw = new Instances("crawledDocs", atts, 0);

					double[] newInst = new double[2];
					newInst[0] = (double) dataRaw.attribute(0).addStringValue(
							doc.getUrl().toString());
					newInst[1] = (double) dataRaw.attribute(1).addStringValue(
							doc.getBody());

					// newInst[0] = (double) dataRaw.attribute(0).addStringValue(
					// doc.getBody());

					dataRaw.add(new Instance(1.0, newInst));

					StringToWordVector filter = new StringToWordVector();
					filter.setInputFormat(dataRaw);
					Instances dataFiltered = Filter.useFilter(dataRaw, filter);

					// System.out.println(dataFiltered.toString());

					dataFiltered.setClassIndex(dataFiltered.numAttributes() - 1);

					Instances labeled = new Instances(dataFiltered);

					try {

						double clsLabel = trainedClassifier.classifyInstance(dataFiltered
								.instance(0));
						labeled.instance(0).setClassValue(clsLabel);
					} catch (IndexOutOfBoundsException e) {
					}

//					System.out.println(doc.getUrl().toString() + ": "
//							+ labeled.instance(0).classValue());
					hbase.put("classifiedDocs", doc.getUrl().toString(), "classify",
							"category", "" + labeled.instance(0).classValue());
				} catch (UnassignedClassException e) {
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				count++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}