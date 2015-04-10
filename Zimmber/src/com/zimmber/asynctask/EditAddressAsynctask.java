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
import com.zimmber.interfaces.EditAddressInterface;
import com.zimmber.networkutil.NetworkUtil;

public class EditAddressAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	public EditAddressInterface editaddressintf;

	String email = "", access_token = "", addresss_id = "", address_title = "",
			state_id = "", city_id = "", location_id = "", landmark = "",
			street = "", flat_no = "", address = "", pincode = "";

	String errorcode = "", message = "";
	InputStream is = null;
	static String json = "";

	public EditAddressAsynctask(Activity activity) {
		this.activity = activity;

	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub

		String url = NetworkUtil.addAddressUrl;

		email = args[0];
		access_token = args[1];
		state_id = args[2];
		city_id = args[3];
		location_id = args[4];
		landmark = args[5];
		street = args[6];
		flat_no = args[7];
		address = args[8];
		pincode = args[9];
		addresss_id = args[10];
		address_title = args[11];

		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("email", email);
			jsonObject.put("accessTokn", access_token);
			jsonObject.put("addressId", addresss_id);
			jsonObject.put("addressHeader", address_title);
			jsonObject.put("state", state_id);
			jsonObject.put("city", city_id);
			jsonObject.put("location", location_id);
			jsonObject.put("landmark", landmark);
			jsonObject.put("street", street);
			jsonObject.put("flat_No", flat_no);
			jsonObject.put("address", address);
			jsonObject.put("pincode", pincode);

			JSONObject addAddress = new JSONObject();
			addAddress.put("addAddress", jsonObject);

			Log.d("addAddress", addAddress.toString());

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", addAddress
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

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		editaddressintf.onCompletedEditAddress(errorcode, message);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		editaddressintf.onStartedEditAddress();

	}
}
