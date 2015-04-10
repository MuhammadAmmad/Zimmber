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
import com.zimmber.bin.OrderDetailsItem;
import com.zimmber.interfaces.GetOrderDetailsInterface;
import com.zimmber.networkutil.NetworkUtil;

public class GetOrderDetailsAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	public GetOrderDetailsInterface getorderdetailsintf;
	private OrderDetailsItem orderdetailsitem;
	private ArrayList<OrderDetailsItem> listoforderdetails;

	String email = "", access_token = "", booking_id = "";

	String errorcode = "", message = "";
	InputStream is = null;
	static String json = "";

	public GetOrderDetailsAsynctask(Activity activity) {
		this.activity = activity;
		listoforderdetails = new ArrayList<OrderDetailsItem>();

	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub

		String url = NetworkUtil.getOrderUrl;

		email = args[0];
		access_token = args[1];
		booking_id = args[2];

		Log.d("booking_id", booking_id);

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

						JSONObject getOrderObj = getOrderArray.getJSONObject(i);

						String get_booking_id = getOrderObj
								.getString("bookingId");

						Log.d("get_booking_id", get_booking_id);

						if (get_booking_id.equals(booking_id)) {

							JSONArray getjobsArray = getOrderObj
									.getJSONArray("jobsList");

							for (int j = 0; j < getjobsArray.length(); j++) {

								orderdetailsitem = new OrderDetailsItem();

								JSONObject getJobsObj = getjobsArray
										.getJSONObject(j);

								orderdetailsitem.setServiceName((getJobsObj
										.getString("serviceName")));

								orderdetailsitem.setVar1Name((getJobsObj
										.getString("var1Name")));

								orderdetailsitem.setVar2Name((getJobsObj
										.getString("var2Name")));

								orderdetailsitem.setVar3Name((getJobsObj
										.getString("var3Name")));

								orderdetailsitem.setVar4Name((getJobsObj
										.getString("var4Name")));

								orderdetailsitem.setActivityName((getJobsObj
										.getString("activityName")));

								orderdetailsitem.setActivityCount((getJobsObj
										.getString("activityCount")));

								orderdetailsitem.setActivityPrice((getJobsObj
										.getString("activityPrice")));

								listoforderdetails.add(orderdetailsitem);

							}

						}

					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		getorderdetailsintf.onCompletedOrderDetails(listoforderdetails, errorcode,
				message);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		getorderdetailsintf.onStartedOrderDetails();

	}
}
