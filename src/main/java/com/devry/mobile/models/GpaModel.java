package com.devry.mobile.models;

public class GpaModel {
	private String dsi;
	private String gpa;
	
	public GpaModel(String dsi, String gpa) {
		this.dsi = dsi;
		this.gpa = gpa;				
	}

	public String getDsi() {
		return dsi;
	}

	public void setDsi(String dsi) {
		this.dsi = dsi;
	}

	public String getGpa() {
		return gpa;
	}

	public void setGpa(String gpa) {
		this.gpa = gpa;
	}
}
