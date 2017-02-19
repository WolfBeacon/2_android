package com.osh.hackathonbrowser.api;

import com.osh.hackathonbrowser.api.response.HackathonResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ApiInterface {
    @GET("/hackathon/list")
    void listHackathons(
            @Query("start-date") String startDate,
            @Query("end-date") String endDate,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("sort-by") String sortType,
            Callback<HackathonResponse[]> response);
}
