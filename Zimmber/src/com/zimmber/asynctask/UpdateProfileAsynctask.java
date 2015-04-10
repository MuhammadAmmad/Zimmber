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
import com.zimmber.interfaces.UpdateprofileInterface;
import com.zimmber.networkutil.NetworkUtil;

public class UpdateProfileAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	public UpdateprofileInterface updateprofileintf;

	String email = "", access_token = "", firstname = "", lastname = "",
			phone = "", dob = "", gender_id = "", gender = "",
			marital_status_id = "", marital_status = "", address_title = "", state_id = "",
			city_id = "", location_id = "", landmark = "", street = "",
			flat_no = "", address = "", pincode = "";

	String errorcode = "", message = "";
	InputStream is = null;
	static String json = "";

	public UpdateProfileAsynctask(Activity activity) {
		this.activity = activity;

	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub

		String url = NetworkUtil.updateProfileUrl;

		email = args[0];
		access_token = args[1];
		firstname = args[2];
		lastname = args[3];
		phone = args[4];
		dob = args[5];
		gender = args[6];
		marital_status = args[7];
		state_id = args[8];
		city_id = args[9];
		location_id = args[10];
		landmark = args[11];
		street = args[12];
		flat_no = args[13];
		address = args[14];
		pincode = args[15];
		address_title = args[16];

		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("email", email);
			jsonObject.put("acessToken", access_token);
			jsonObject.put("fName", firstname);
			jsonObject.put("lName", lastname);
			jsonObject.put("phNo", phone);
			jsonObject.put("dob", dob);
			jsonObject.put("gender", gender);
			jsonObject.put("maritalStatus", marital_status);
			jsonObject.put("addressHeader", address_title);
			jsonObject.put("state", state_id);
			jsonObject.put("city", city_id);
			jsonObject.put("location", location_id);
			jsonObject.put("landmark", landmark);
			jsonObject.put("street", street);
			jsonObject.put("flate_no", flat_no);
			jsonObject.put("address", address);
			jsonObject.put("pin", pincode);

			JSONObject updateProfile = new JSONObject();
			updateProfile.put("updateProfile", jsonObject);

			Log.d("updateProfile", updateProfile.toString());

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("data", updateProfile.toString()));
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

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		updateprofileintf.onCompletedUpdateProfile(errorcode, message);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		updateprofileintf.onStartedUpdateProfile();

	}
}
