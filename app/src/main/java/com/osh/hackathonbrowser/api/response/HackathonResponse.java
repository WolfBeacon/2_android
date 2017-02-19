package com.osh.hackathonbrowser.api.response;

import com.google.gson.annotations.SerializedName;

public class HackathonResponse {
	private long id;

	private String title;

	private String startDate;

	private String endDate;

	private String lastUpdatedDate;

	private int year;

	private String location;

	private String host;

	@SerializedName("length")
	private int duration;

	private String size;

	@SerializedName("travel")
	private boolean travelProvided;

	@SerializedName("prize")
	private boolean prizesProvided;

	@SerializedName("highSchoolers")
	private boolean highSchoolersAllowed;

	private String cost;

	private String facebookLink;

	private String twitterLink;

	private String googlePlusLink;

	@SerializedName("imageLink")
	private String iconUrl;

	private double latitude;

	private double longitude;

	private String notes;

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public int getYear() {
		return year;
	}

	public String getLocation() {
		return location;
	}

	public String getHost() {
		return host;
	}

	public int getDuration() {
		return duration;
	}

	public String getSize() {
		return size;
	}

	public boolean isTravelProvided() {
		return travelProvided;
	}

	public boolean arePrizesProvided() {
		return prizesProvided;
	}

	public boolean areHighSchoolersAllowed() {
		return highSchoolersAllowed;
	}

	public String getCost() {
		return cost;
	}

	public String getFacebookLink() {
		return facebookLink;
	}

	public String getTwitterLink() {
		return twitterLink;
	}

	public String getGooglePlusLink() {
		return googlePlusLink;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getNotes() {
		return notes;
	}
}