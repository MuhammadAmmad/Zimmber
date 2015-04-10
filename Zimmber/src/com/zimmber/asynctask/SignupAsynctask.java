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
import com.zimmber.interfaces.SignupInterface;
import com.zimmber.networkutil.NetworkUtil;

public class SignupAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	public SignupInterface signupintf;

	String firstname = "", lastname = "", phone = "", email = "",
			password = "", confirmpassword = "", state_id = "", city_id = "",
			location_id = "", pincode = "", current_latitude = "",
			current_longitude = "", isSocial = "";

	String errorcode = "", message = "", user_email = "", user_access_token;
	InputStream is = null;
	static String json = "";

	public SignupAsynctask(Activity activity) {
		this.activity = activity;

	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub

		String url = NetworkUtil.signupUrl;

		isSocial = args[0];

		if (isSocial.equals("0")) {

			isSocial = args[0];
			firstname = args[1];
			lastname = args[2];
			phone = args[3];
			email = args[4];
			current_latitude = args[5];
			current_longitude = args[6];
			password = args[7];
			confirmpassword = args[8];

			try {

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("fName", firstname);
				jsonObject.put("lName", lastname);
				jsonObject.put("phNo", phone);
				jsonObject.put("email", email);
				jsonObject.put("password", password);
				jsonObject.put("confirmpassword", confirmpassword);
				jsonObject.put("dob", "");
				jsonObject.put("gender", "");
				jsonObject.put("latValue", current_latitude);
				jsonObject.put("longVal", current_longitude);
				jsonObject.put("isSocial", isSocial);
				jsonObject.put("deviceId", "");
				jsonObject.put("deviceType", "android");

				JSONObject signUp = new JSONObject();
				signUp.put("signUp", jsonObject);

				Log.d("Signup", signUp.toString());

				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("data", signUp
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

		}

		else {

			isSocial = args[0];
			firstname = args[1];
			lastname = args[2];
			phone = args[3];
			email = args[4];
			current_latitude = args[5];
			current_longitude = args[6];

			try {

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("fName", firstname);
				jsonObject.put("lName", lastname);
				jsonObject.put("phNo", phone);
				jsonObject.put("email", email);
				jsonObject.put("dob", "");
				jsonObject.put("gender", "");
				jsonObject.put("latValue", current_latitude);
				jsonObject.put("longVal", current_longitude);
				jsonObject.put("isSocial", isSocial);
				jsonObject.put("deviceId", "");
				jsonObject.put("deviceType", "android");

				JSONObject signUp = new JSONObject();
				signUp.put("signUp", jsonObject);

				Log.d("Signup", signUp.toString());

				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("data", signUp
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

					JSONObject signupObj = dataObj.getJSONObject("signup");

					user_email = signupObj.getString("user_Id");

					user_access_token = signupObj.getString("acess_token");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		signupintf.onCompletedSignup(errorcode, message, user_email,
				user_access_token);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		signupintf.onStartedSignup();

	}
}
