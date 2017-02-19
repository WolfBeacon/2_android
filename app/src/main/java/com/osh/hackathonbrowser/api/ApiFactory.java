package com.osh.hackathonbrowser.api;

import com.auth0.android.result.Credentials;
import com.osh.hackathonbrowser.ApplicationClass;
import com.osh.hackathonbrowser.Constants;
import com.osh.hackathonbrowser.Utilities;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class ApiFactory {
	private static ApiInterface instance;
	private static boolean checkedForToken = false;
	private static String token = null;

	public static ApiInterface getInstance(){
		if (instance != null) return instance;

		RestAdapter adapter = new RestAdapter.Builder()
				.setEndpoint(Constants.URLs.API_URL)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						if(!checkedForToken) {
							Credentials appCreds = Utilities.getCredentials(ApplicationClass.getAppContext());
							if (appCreds != null) {
								token = appCreds.getIdToken();
							}
						}

						if(token != null) request.addHeader("Authorization", "Bearer " + token);
					}
				})
				.build();

		return (instance = adapter.create(ApiInterface.class));
	}

	/**
	 * Rebuild the API instance. Typically good after logout/after login.
	 */
	public static void checkForToken(){
		checkedForToken = false;
	}
}