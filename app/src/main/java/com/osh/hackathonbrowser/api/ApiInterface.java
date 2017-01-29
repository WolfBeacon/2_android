package com.osh.hackathonbrowser.api;

import com.osh.hackathonbrowser.api.response.HackathonResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ApiInterface {
	@GET("/cms/get/{id}")
	void fetchHackathon(@Path("id") int hackathonId, Callback<HackathonResponse> callback);

	//TODO: This route doesn't have params; fake ones are here for now

	@GET("/hackathons/list")
	void getAllHackathons(@Query("type") String type, Callback<HackathonResponse[]> response);
}
