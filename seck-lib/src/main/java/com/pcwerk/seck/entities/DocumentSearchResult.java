package com.pcwerk.seck.entities;

import com.google.gson.annotations.SerializedName;

public class DocumentSearchResult implements Comparable<DocumentSearchResult> {

	@SerializedName("File")
	private String filePath;

	@SerializedName("Score")	
	private double score;

	@SerializedName("Count")
	private int tf;

	public DocumentSearchResult(String filePath) {
		this.filePath = filePath;
	}

	public DocumentSearchResult(String filePath, int tf) {
		this(filePath);
		this.tf = tf;
	}

	public int getTf() {
		return tf;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return "DocumentSearchResult [score=" + score + ", tf=" + tf
				+ ", filePath=" + filePath + "]";
	}

	public int compareTo(DocumentSearchResult o) {

		if (this.score == o.getScore() && this.filePath == o.getFilePath()) {
			return 0;
		} else if (this.score < o.getScore()) {
			return 1;
		} else {
			return -1;
		}
	}

}
