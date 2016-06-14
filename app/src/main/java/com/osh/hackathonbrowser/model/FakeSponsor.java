package com.osh.hackathonbrowser.model;

import com.osh.hackathonbrowser.Constants;

public class FakeSponsor {
    private String name;

    //@SerializedName("logo_url")
    private String logoUrl;

    private String description;

    //@SerializedName("jobs_url")
    private String jobsUrl;

    public FakeSponsor(String name, String logoUrl, String description, String jobsUrl) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
        this.jobsUrl = jobsUrl;
    }

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getJobsUrl() {
        return jobsUrl;
    }

    public static FakeSponsor getFakeSponsor(){
        return new FakeSponsor("Tech Company", Constants.DEFAULT_FAKE_COMPANY, "We make innovative, dynamic, exciting technology for the future.", Constants.DEFAULT_FAKE_URL);
    }
}
