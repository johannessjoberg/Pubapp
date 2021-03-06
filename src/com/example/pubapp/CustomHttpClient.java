package com.example.pubapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.*;

import android.util.Log;

public class CustomHttpClient {

	/** The time it takes for our client to timeout */
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
	/** Single instance of our HttpClient */
	private static HttpClient mHttpClient;

	/**
	 * Gets a JSONArray from the MySQL database through a PhP-script
	 * 
	 * @param sendparameter		parameter for the PhP-script
	 * @param url	 			the URL to the PhP-script
	 * 
	 * @return A JSONArray fetched from the URL
	 * 
	 */
	public static JSONArray getJSON(String sendparameter, String url) {
		// declare parameters that are passed to PHP script i.e. the id "id" and
		// its value
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		// define the parameter
		postParameters.add(new BasicNameValuePair("id", sendparameter));
		String response = null;
		JSONArray JSONContent = null;
		// call executeHttpPost method passing necessary parameters
		try {
			response = executeHttpPost(url, postParameters);

			// store the result returned by PHP script that runs MySQL query
			String result = response.toString();
			if (result.trim().equals("null")) {
				JSONContent = new JSONArray();
			} else {
				JSONContent = new JSONArray(result);
			}
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
			return null;
		}
		return JSONContent;
	}

	/**
	 * Get our single instance of our HttpClient object.
	 * 
	 * @return an HttpClient object with connection parameters set
	 */
	private static HttpClient getHttpClient() {

		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();

			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}

		return mHttpClient;
	}

	/**
	 * Performs an HTTP Post request to the specified url with the specified
	 * parameters.
	 * 
	 * @param url
	 *            The web address to post the request to
	 * @param postParameters
	 *            The parameters to send via the request
	 * @return The result of the request
	 * @throws Exception
	 */

	public static String executeHttpPost(String url,
			ArrayList<NameValuePair> postParameters) throws Exception {

		BufferedReader in = null;

		try {
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);

			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}

			in.close();

			String result = sb.toString();
			return result;

		} finally {

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e("log_tag", "Error converting result " + e.toString());
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * Performs an HTTP GET request to the specified url.
	 * 
	 * @param url	The web address to post the request to
	 * 
	 * @return The result of the request
	 * 
	 * @throws Exception
	 */

	public static String executeHttpGet(String url) throws Exception {

		BufferedReader in = null;

		try {
			HttpClient client = getHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}

			in.close();

			String result = sb.toString();
			return result;

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e("log_tag", "Error converting result " + e.toString());
					e.printStackTrace();
				}
			}
		}
	}
}