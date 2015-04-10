package com.zimmber.asynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.zimmber.interfaces.GetProfileInterface;
import com.zimmber.networkutil.NetworkUtil;

public class GetProfileAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	public GetProfileInterface getprofileintf;

	String email = "", access_token = "";

	String errorcode = "", message = "", firstname = "", lastname = "",
			phone = "", dob = "", gender = "", marital_status = "",
			address_title = "", state = "", city = "", location = "",
			landmark = "", street = "", flat_no = "", address = "",
			pincode = "";
	InputStream is = null;
	static String json = "";

	public GetProfileAsynctask(Activity activity) {
		this.activity = activity;

	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub

		String url = NetworkUtil.getProfileUrl;

		email = args[0];
		access_token = args[1];

		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("email", email);
			jsonObject.put("acessToken", access_token);

			JSONObject getProfile = new JSONObject();
			getProfile.put("getProfile", jsonObject);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", getProfile
					.toString()));
			Log.d("url", nameValuePairs.toString());
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "n");
			}
			is.close();
			json = sb.toString();
			Log.d("json Response", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		return json;

	}

	@Override
	protected void onPostExecute(String jsonStr) {
		// TODO Auto-generated method stub
		super.onPostExecute(jsonStr);

		if (jsonStr != null) {

			try {

				JSONObject jsonObj = new JSONObject(jsonStr);

				JSONObject dataObj = jsonObj.getJSONObject("data");

				errorcode = dataObj.getString("errorcode");

				message = dataObj.getString("massage");

				if (errorcode.equals("0")) {

					JSONObject getProfileObj = dataObj
							.getJSONObject("getProfile");

					firstname = getProfileObj.getString("fName");

					if (firstname.equalsIgnoreCase("null")) {
						firstname = "";
					}

					lastname = getProfileObj.getString("lName");

					if (lastname.equalsIgnoreCase("null")) {
						lastname = "";
					}

					email = getProfileObj.getString("email");

					if (email.equalsIgnoreCase("null")) {
						email = "";
					}

					phone = getProfileObj.getString("phNo");

					if (phone.equalsIgnoreCase("null")) {
						phone = "";
					}

					dob = getProfileObj.getString("dob");

					if (dob.equalsIgnoreCase("null")) {
						dob = "";
					}

					gender = getProfileObj.getString("gender");

					if (gender.equalsIgnoreCase("null")) {
						gender = "";
					}

					marital_status = getProfileObj.getString("maritalStatus");

					if (marital_status.equalsIgnoreCase("null")) {
						marital_status = "";
					}

					address_title = getProfileObj.getString("addressHeader");

					if (address_title.equalsIgnoreCase("null")) {
						address_title = "";
					}

					state = getProfileObj.getJSONObject("state").getString(
							"name");

					if (state.equalsIgnoreCase("null")) {
						state = "";
					}

					city = getProfileObj.getJSONObject("city")
							.getString("name");

					if (city.equalsIgnoreCase("null")) {
						city = "";
					}

					location = getProfileObj.getJSONObject("locality")
							.getString("name");

					if (location.equalsIgnoreCase("null")) {
						location = "";
					}

					landmark = getProfileObj.getString("landmark");

					if (landmark.equalsIgnoreCase("null")) {
						landmark = "";
					}

					street = getProfileObj.getString("street");

					if (street.equalsIgnoreCase("null")) {
						street = "";
					}

					flat_no = getProfileObj.getString("flatNo");

					if (flat_no.equalsIgnoreCase("null")) {
						flat_no = "";
					}

					address = getProfileObj.getString("address");

					if (address.equalsIgnoreCase("null")) {
						address = "";
					}

					pincode = getProfileObj.getString("pincode");

					if (pincode.equalsIgnoreCase("null")) {
						pincode = "";
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		getprofileintf.onCompletedGetProfile(errorcode, message, firstname,
				lastname, email, phone, dob, gender, marital_status,
				address_title, state, city, location, landmark, street,
				flat_no, address, pincode);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		getprofileintf.onStartedGetProfile();

	}
}
