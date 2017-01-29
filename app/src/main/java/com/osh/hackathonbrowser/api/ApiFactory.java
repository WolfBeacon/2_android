package com.osh.hackathonbrowser.api;

import com.osh.hackathonbrowser.Constants;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class ApiFactory {
	private static ApiInterface instance;

	public static ApiInterface getInstance(){
		if (instance != null) return instance;

		RestAdapter adapter = new RestAdapter.Builder()
				.setEndpoint(Constants.URLs.API_URL)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						//TODO: If we ever need to add authentication to each request, this is the place to add it
						//Just be careful about not authenticating non-authenticated routes! Can't auth /sign_up or /signin
					}
				})
				.build();

		return (instance = adapter.create(ApiInterface.class));
	}
}