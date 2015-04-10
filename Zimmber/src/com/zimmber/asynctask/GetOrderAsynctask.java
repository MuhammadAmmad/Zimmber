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
import com.zimmber.bin.OrderItem;
import com.zimmber.interfaces.GetOrderInterface;
import com.zimmber.networkutil.NetworkUtil;

public class GetOrderAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	public GetOrderInterface getorderintf;
	private OrderItem orderitem;
	private ArrayList<OrderItem> listofmyorder;

	String email = "", access_token = "";

	String errorcode = "", message = "";
	InputStream is = null;
	static String json = "";

	public GetOrderAsynctask(Activity activity) {
		this.activity = activity;
		listofmyorder = new ArrayList<OrderItem>();

	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub

		String url = NetworkUtil.getOrderUrl;

		email = args[0];
		access_token = args[1];

		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("email", email);
			jsonObject.put("accessTokn", access_token);

			JSONObject getOrder = new JSONObject();
			getOrder.put("getOrder", jsonObject);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", getOrder
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

					JSONArray getOrderArray = dataObj.getJSONArray("OrderList");

					for (int i = 0; i < getOrderArray.length(); i++) {

						orderitem = new OrderItem();

						JSONObject getOrderObj = getOrderArray.getJSONObject(i);

						orderitem.setBookingId(getOrderObj
								.getString("bookingId"));

						orderitem.setTotalPrice(getOrderObj
								.getString("totalPrice"));

						orderitem.setBookingTime(getOrderObj
								.getString("bookingTime"));

						orderitem.setServiceDate(getOrderObj
								.getString("serviceDate"));

						orderitem.setServiceTime(getOrderObj
								.getString("serviceTime"));

						listofmyorder.add(orderitem);

					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		getorderintf.onCompletedGetOrder(listofmyorder, errorcode, message);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		getorderintf.onStartedGetOrder();

	}
}
