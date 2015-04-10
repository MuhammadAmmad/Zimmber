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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.zimmber.interfaces.GetPriceDataInterface;
import com.zimmber.networkutil.NetworkUtil;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class GetPriceDataAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	public GetPriceDataInterface pricedataintf;

	String accessToken = "", var1_id = "", var2_id = "", var3_id = "",
			var4_id = "", activity_id = "", activity_count_id = "",
			service_id = "";

	String errorcode = "", price = "", status = "";
	InputStream is = null;
	static String json = "";

	public GetPriceDataAsynctask(Activity activity) {
		this.activity = activity;

	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub

		String url = NetworkUtil.getPriceDataUrl;

		accessToken = args[0];
		var1_id = args[1];
		var2_id = args[2];
		var3_id = args[3];
		var4_id = args[4];
		activity_id = args[5];
		activity_count_id = args[6];
		service_id = args[7];

		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("accessTokn", accessToken);
			jsonObject.put("var1_Id", var1_id);
			jsonObject.put("var2_Id", var2_id);
			jsonObject.put("var3_Id", var3_id);
			jsonObject.put("var4_Id", var4_id);
			jsonObject.put("activity_Id", activity_id);
			jsonObject.put("activityCount_Id", activity_count_id);
			jsonObject.put("serviceId", service_id);

			JSONArray jsonArray = new JSONArray();
			jsonArray.put(jsonObject);

			JSONObject priceData = new JSONObject();
			priceData.put("priceData", jsonArray);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", priceData
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
			Log.d("json", json);
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

				JSONObject errorNode = jsonObj.getJSONObject("erNode");

				String errorcode = errorNode.getString("erCode");

				if (errorcode.equals("0")) {

					JSONObject data = jsonObj.getJSONObject("data");

					price = data.getString("getPrice");

					status = data.getString("status");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		pricedataintf.onCompletedGetPrice(errorcode, price, status);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pricedataintf.onStartedGetPrice();

	}
}
