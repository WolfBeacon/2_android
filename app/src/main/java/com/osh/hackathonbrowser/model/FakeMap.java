package com.osh.hackathonbrowser.model;

import com.google.gson.annotations.SerializedName;
import com.osh.hackathonbrowser.Constants;

public class FakeMap {
    @SerializedName("map_url")
    private String mapImage;

    private String title;

    private String description;

    public FakeMap(String mapImage, String title, String description) {
        this.mapImage = mapImage;
        this.title = title;
        this.description = description;
    }

    public String getMapImage() {
        return mapImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static FakeMap getFakeMap(){
        return new FakeMap(Constants.DEFAULT_FAKE_MAP, "A Location Map", "The ground floor of the facility.");
    }
}
