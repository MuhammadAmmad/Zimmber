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
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.zimmber.bin.AddressItem;
import com.zimmber.interfaces.GetAddressInterface;
import com.zimmber.networkutil.NetworkUtil;

public class GetAddressAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	public GetAddressInterface getaddressintf;
	private AddressItem addressitem;
	private ArrayList<AddressItem> listofmyaddress;

	String email = "", access_token = "";

	String errorcode = "", message = "";
	InputStream is = null;
	static String json = "";

	public GetAddressAsynctask(Activity activity) {
		this.activity = activity;
		listofmyaddress = new ArrayList<AddressItem>();

	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub

		String url = NetworkUtil.getAddressUrl;

		email = args[0];
		access_token = args[1];

		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("email", email);
			jsonObject.put("acessToken", access_token);

			JSONObject getAddress = new JSONObject();
			getAddress.put("getAddress", jsonObject);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", getAddress
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

					JSONArray getAddressArray = dataObj
							.getJSONArray("addressList");

					for (int i = 0; i < getAddressArray.length(); i++) {

						addressitem = new AddressItem();

						JSONObject getAddressObj = getAddressArray
								.getJSONObject(i);

						addressitem.setAddressId(getAddressObj.getString("id"));

						addressitem.setAddressTitle(getAddressObj
								.getString("addressHeader"));

						addressitem.setStateId(getAddressObj.getJSONObject(
								"state").getString("id"));

						addressitem.setStateName(getAddressObj.getJSONObject(
								"state").getString("name"));

						addressitem.setCityId(getAddressObj.getJSONObject(
								"city").getString("id"));

						addressitem.setCityName(getAddressObj.getJSONObject(
								"city").getString("name"));

						addressitem.setLocationId(getAddressObj.getJSONObject(
								"location").getString("id"));

						addressitem.setLocationName(getAddressObj
								.getJSONObject("location").getString("name"));

						addressitem.setLandmark(getAddressObj
								.getString("landmark"));

						addressitem
								.setStreet(getAddressObj.getString("street"));

						addressitem.setFlatNo(getAddressObj
								.getString("flat_No"));

						addressitem.setAddress(getAddressObj
								.getString("address"));

						addressitem.setPincode(getAddressObj
								.getString("pincode"));

						listofmyaddress.add(addressitem);

					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		getaddressintf.onCompletedGetAddress(listofmyaddress, errorcode,
				message);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		getaddressintf.onStartedGetAddress();

	}
}
