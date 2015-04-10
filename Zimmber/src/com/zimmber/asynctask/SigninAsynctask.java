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
import com.zimmber.interfaces.SigninInterface;
import com.zimmber.networkutil.NetworkUtil;

public class SigninAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	public SigninInterface signinintf;

	String username = "", password = "", current_latitude = "",
			current_longitude = "";

	String errorcode = "", message = "", email = "", access_token = "";
	InputStream is = null;
	static String json = "";

	public SigninAsynctask(Activity activity) {
		this.activity = activity;

	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub

		String url = NetworkUtil.loginUrl;

		username = args[0];
		password = args[1];
		current_latitude = args[2];
		current_longitude = args[3];

		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("email", username);
			jsonObject.put("password", password);
			jsonObject.put("latVal", current_latitude);
			jsonObject.put("longVal", current_longitude);

			JSONObject emailLogin = new JSONObject();
			emailLogin.put("emailLogin", jsonObject);

			Log.d("Signin", emailLogin.toString());

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", emailLogin
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

					JSONObject emaliLoginObj = dataObj
							.getJSONObject("emailLogin");

					email = emaliLoginObj.getString("email");

					access_token = emaliLoginObj.getString("accessTokn");

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		signinintf.onCompletedSignIn(errorcode, message, email, access_token);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		signinintf.onStartedSignIn();

	}
}
