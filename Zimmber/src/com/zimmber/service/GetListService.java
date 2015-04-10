package com.zimmber.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import com.zimmber.Constants;
import com.zimmber.networkutil.NetworkUtil;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class GetListService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		new getServiceListAynctask().execute();

		new getLocationListAynctask().execute();

		return super.onStartCommand(intent, flags, startId);
	}

	public class getServiceListAynctask extends
			AsyncTask<String, String, String> {

		Activity activity;

		String url = "";
		InputStream is = null;
		String json = "";

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub

			url = NetworkUtil.getServiceListUrl;

			// Making HTTP request
			try {
				// defaultHttpClient
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
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "n");
				}
				is.close();
				json = sb.toString();
				Log.d("response", json);
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
					// JSONObject result = new JSONObject(jsonStr);
					String fileDIr = Environment.getExternalStorageDirectory()
							+ File.separator + getPackageName()
							+ File.separator;
					File f = new File(fileDIr);
					if (!f.exists()) {
						f.mkdirs();
					}

					Log.e("fileDIr", fileDIr);

					String filename = f.getAbsolutePath() + File.separator
							+ Constants.JSON_SERVICE_FILE_NAME;
					Log.e("filename", filename);
					File jsonF = new File(filename);

					if (jsonF.exists()) {
						jsonF.createNewFile();
					}

					String data = jsonStr;

					FileOutputStream fos;
					try {
						fos = new FileOutputStream(jsonF.getAbsolutePath());
						fos.write(data.getBytes());
						fos.close();

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

	}

	public class getLocationListAynctask extends
			AsyncTask<String, String, String> {

		Activity activity;

		String url = "";
		InputStream is = null;
		String json = "";

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub

			url = NetworkUtil.getLocationListUrl;

			// Making HTTP request
			try {
				// defaultHttpClient
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
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "n");
				}
				is.close();
				json = sb.toString();
				Log.d("response", json);
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
					// JSONObject result = new JSONObject(jsonStr);
					String fileDIr = Environment.getExternalStorageDirectory()
							+ File.separator + getPackageName()
							+ File.separator;
					File f = new File(fileDIr);
					if (!f.exists()) {
						f.mkdirs();
					}

					Log.e("fileDIr", fileDIr);

					String filename = f.getAbsolutePath() + File.separator
							+ Constants.JSON_LOCATION_FILE_NAME;
					Log.e("filename", filename);
					File jsonF = new File(filename);

					if (jsonF.exists()) {
						jsonF.createNewFile();
					}

					String data = jsonStr;

					FileOutputStream fos;
					try {
						fos = new FileOutputStream(jsonF.getAbsolutePath());
						fos.write(data.getBytes());
						fos.close();

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

	}

}
