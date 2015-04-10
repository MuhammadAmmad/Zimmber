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
import com.zimmber.interfaces.GetFeedbackInterface;
import com.zimmber.networkutil.NetworkUtil;

public class GetFeedbackAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	public GetFeedbackInterface getfeedbackintf;

	String access_token = "", email = "", booking_id = "";

	String errorcode = "", message = "", status = "", quality_rate = "",
			timely_rate = "", behaviour_rate = "", cleanliness_rate = "",
			value_of_money_rate = "", remarks = "";;
	InputStream is = null;
	static String json = "";

	public GetFeedbackAsynctask(Activity activity) {
		this.activity = activity;

	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub

		String url = NetworkUtil.getFeedbackUrl;

		access_token = args[0];
		email = args[1];
		booking_id = args[2];

		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("AcessToken", access_token);
			jsonObject.put("email", email);
			jsonObject.put("orderId", booking_id);

			JSONObject getFeedBack = new JSONObject();
			getFeedBack.put("getFeedBack", jsonObject);

			Log.d("getFeedBack", getFeedBack.toString());

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", getFeedBack
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

				status = dataObj.getString("status");

				if (status.equals("1")) {

					JSONObject getfeedbacklistObj = dataObj
							.getJSONObject("getfeedbacklist");

					quality_rate = getfeedbacklistObj.getString("quality");

					timely_rate = getfeedbacklistObj.getString("timely");

					behaviour_rate = getfeedbacklistObj.getString("behaviour");

					cleanliness_rate = getfeedbacklistObj
							.getString("clienliness");

					value_of_money_rate = getfeedbacklistObj
							.getString("valueofmoeny");

					remarks = getfeedbacklistObj.getString("remarks");

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		getfeedbackintf.onCompletedGetFeedback(errorcode, message, status,
				quality_rate, timely_rate, behaviour_rate, cleanliness_rate,
				value_of_money_rate, remarks);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		getfeedbackintf.onStartedGetFeedback();

	}
}
