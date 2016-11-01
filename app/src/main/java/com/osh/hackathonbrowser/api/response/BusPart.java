package com.osh.hackathonbrowser.api.response;

import com.google.gson.annotations.SerializedName;

public class BusPart {
	private String name;

	private long time;

	@SerializedName("coords")
	private float[] coordinates;

	public String getName() {
		return name;
	}

	public long getTime() {
		return time;
	}

	public float[] getCoordinates() {
		return coordinates;
	}
}
