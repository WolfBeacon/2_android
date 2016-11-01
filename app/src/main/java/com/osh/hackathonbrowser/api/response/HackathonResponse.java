package com.osh.hackathonbrowser.api.response;

import com.google.gson.annotations.SerializedName;

public class HackathonResponse {
	private int id;

	@SerializedName("ownerid")
	private int ownerId;

	private String name;

	private LocationPart location;

	@SerializedName("startdate")
	private long startDate;

	@SerializedName("enddate")
	private long endDate;

	@SerializedName("currentstate")
	private int currentState;

	private PrizePart[] prizes;

	@SerializedName("reimbursements")
	private boolean reimbursementsOffered;

	@SerializedName("busesoffered")
	private boolean busesOffered;

	@SerializedName("buslocations")
	private BusPart[] busLocations;

	@SerializedName("sociallinks")
	private SocialLinkPart[] socialLinks;

	private HardwarePart[] hardware;

	private String map;

	private String metadata;

	public int getId() {
		return id;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public String getName() {
		return name;
	}

	public LocationPart getLocation() {
		return location;
	}

	public long getStartDate() {
		return startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public int getCurrentState() {
		return currentState;
	}

	public PrizePart[] getPrizes() {
		return prizes;
	}

	public boolean isReimbursementsOffered() {
		return reimbursementsOffered;
	}

	public boolean isBusesOffered() {
		return busesOffered;
	}

	public BusPart[] getBusLocations() {
		return busLocations;
	}

	public SocialLinkPart[] getSocialLinks() {
		return socialLinks;
	}

	public HardwarePart[] getHardware() {
		return hardware;
	}

	public String getMap() {
		return map;
	}

	public String getMetadata() {
		return metadata;
	}
}
