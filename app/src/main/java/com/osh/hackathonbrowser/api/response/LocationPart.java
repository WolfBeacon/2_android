package com.osh.hackathonbrowser.api.response;

import com.google.gson.annotations.SerializedName;

public class LocationPart {
	private String name;

	private String city;

	private String state;

	private String country;

	@SerializedName("coords")
	private float[] coordinates;

	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	public float[] getCoordinates() {
		return coordinates;
	}
}
