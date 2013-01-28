package com.example.drag_dropexample;

public class DDTag {

	private int probablyIndex		= -1;
	private int rImage				= 0;
	private String rDescription		= "";

	public DDTag(int probablyIndex, int rImage, String rDescription){
		this.probablyIndex	= probablyIndex;
		this.rImage			= rImage;
		this.rDescription	= rDescription;
	}

	public DDTag(int rImage, String rDescription){
		this.rImage			= rImage;
		this.rDescription	= rDescription;
	}

	public DDTag(int possiblyIndex){
		this.probablyIndex = possiblyIndex;
	}	

	public String getrDescription() {
		return rDescription;
	}
	public void setrDescription(String rDescription) {
		this.rDescription = rDescription;
	}
	public int getrImage() {
		return rImage;
	}
	public void setrImage(int rImage) {
		this.rImage = rImage;
	}

	public int getProbablyInt() {
		return probablyIndex;
	}

	public void setProbablyInt(int probablyInt) {
		this.probablyIndex = probablyInt;
	}

}
