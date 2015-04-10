package com.zimmber.asynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.zimmber.bin.MenuListItem;
import com.zimmber.interfaces.GetMenuListInterface;
import com.zimmber.networkutil.NetworkUtil;

public class GetMenuListAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	public GetMenuListInterface menuintf;
	private MenuListItem menulistitem;
	private ArrayList<MenuListItem> listofmenu;

	String errorcode = "", message = "";
	InputStream is = null;
	static String json = "";

	public GetMenuListAsynctask(Activity activity) {
		this.activity = activity;
		listofmenu = new ArrayList<MenuListItem>();

	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub

		String url = NetworkUtil.getMenuListUrl;

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

					JSONArray getMenuListArray = dataObj
							.getJSONArray("getMenuList");

					for (int i = 0; i < getMenuListArray.length(); i++) {

						menulistitem = new MenuListItem();

						JSONObject getMenuObj = getMenuListArray
								.getJSONObject(i);

						menulistitem.setServiceId((getMenuObj.getString("id")));

						menulistitem.setServiceName(getMenuObj
								.getString("name"));

						menulistitem.setServiceIcon(getMenuObj
								.getString("icon").replaceAll(" ", "%20"));

						listofmenu.add(menulistitem);

					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		menuintf.onCompletedMenu(errorcode, message, listofmenu);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		menuintf.onStartedMenu();

	}
}
