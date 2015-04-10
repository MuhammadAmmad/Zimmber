package com.zimmber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.zimmber.adapter.DropDownAdapter;
import com.zimmber.asynctask.GetProfileAsynctask;
import com.zimmber.asynctask.SignupAsynctask;
import com.zimmber.bin.DropDownItem;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.GetProfileInterface;
import com.zimmber.interfaces.SignupInterface;
import com.zimmber.networkutil.GPSTracker;
import com.zimmber.networkutil.Utils;

public class SignupActivity extends Activity implements SignupInterface,
		GetProfileInterface {

	ImageView img_home, img_call;
	EditText et_firstname, et_lastname, et_phone, et_pincode, et_email,
			et_password, et_confirmpassword;
	Spinner spin_state, spin_city, spin_location;
	Button btn_signup;

	ArrayList<DropDownItem> _stateItems;
	ArrayList<DropDownItem> _cityItems;
	ArrayList<DropDownItem> _locationItems;

	DropDownAdapter _stateAdapter;
	DropDownAdapter _cityAdapter;
	DropDownAdapter _locationAdapter;

	String firstname = "", lastname = "", phone = "", email = "",
			password = "", confirmpassword = "", gender = "", dob = "",
			state_id = "", city_id = "", location_id = "", pincode = "";

	String filePath;
	String jsonstr = null;

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	GPSTracker gpsTracker;
	double current_lat, current_long;
	String current_latitude = "", current_longitude = "";

	SharedPreferenceClass sharedpreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.signup);

		((ZimmberApp) getApplication())
				.getTracker(ZimmberApp.TrackerName.APP_TRACKER);

		filePath = Environment.getExternalStorageDirectory() + File.separator
				+ getPackageName() + File.separator
				+ Constants.JSON_LOCATION_FILE_NAME;

		initialize();

		onclick();

		// loadLocation();

		current_lat = gpsTracker.getLatitude();
		current_long = gpsTracker.getLongitude();

		current_latitude = String.valueOf(current_lat);
		current_longitude = String.valueOf(current_long);

		Log.d("current_latitude", current_latitude);
		Log.d("current_longitude", current_longitude);
		Log.d("country", gpsTracker.getCountryName());
		Log.d("state", gpsTracker.getStateName());
		Log.d("city", gpsTracker.getCityName());

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(SignupActivity.this).reportActivityStart(
				SignupActivity.this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(SignupActivity.this).reportActivityStop(
				SignupActivity.this);
	}

	private void initialize() {
		// TODO Auto-generated method stub

		img_home = (ImageView) findViewById(R.id.img_home);
		img_call = (ImageView) findViewById(R.id.img_call);
		et_firstname = (EditText) findViewById(R.id.et_firstname);
		et_lastname = (EditText) findViewById(R.id.et_lastname);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_pincode = (EditText) findViewById(R.id.et_pincode);
		et_email = (EditText) findViewById(R.id.et_email);
		et_password = (EditText) findViewById(R.id.et_password);
		et_confirmpassword = (EditText) findViewById(R.id.et_confirmpassword);
		btn_signup = (Button) findViewById(R.id.btn_signup);

		spin_state = (Spinner) findViewById(R.id.spin_state);
		spin_city = (Spinner) findViewById(R.id.spin_city);
		spin_location = (Spinner) findViewById(R.id.spin_location);

		sharedpreference = new SharedPreferenceClass(SignupActivity.this);
		gpsTracker = new GPSTracker(SignupActivity.this);

	}

	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(SignupActivity.this, LandingActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, keyEvent);
	}

	private void onclick() {
		// TODO Auto-generated method stub

		img_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(SignupActivity.this,
						HomeMainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();

			}
		});

		spin_state.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				_cityItems.clear();

				_locationItems.clear();

				state_id = _stateAdapter.getItem(position).getStrId();

				if (!state_id.equals("0")) {

					DropDownItem _selectcity = new DropDownItem();
					_selectcity.setStrId("0");
					_selectcity.setName("Select City");
					_cityItems.add(_selectcity);

					_cityAdapter.notifyDataSetChanged();

					DropDownItem _selectlocation = new DropDownItem();
					_selectlocation.setStrId("0");
					_selectlocation.setName("Select Location");
					_locationItems.add(_selectlocation);

					_locationAdapter.notifyDataSetChanged();

					new CityAsync().execute("");

				}

				else {

					DropDownItem _selectcity = new DropDownItem();
					_selectcity.setStrId("0");
					_selectcity.setName("Select City");
					_cityItems.add(_selectcity);

					_cityAdapter.notifyDataSetChanged();

					DropDownItem _selectlocation = new DropDownItem();
					_selectlocation.setStrId("0");
					_selectlocation.setName("Select Location");
					_locationItems.add(_selectlocation);

					_locationAdapter.notifyDataSetChanged();

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spin_city.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				_locationItems.clear();

				city_id = _cityAdapter.getItem(position).getStrId();

				if (!city_id.equals("0")) {

					DropDownItem _selectlocation = new DropDownItem();
					_selectlocation.setStrId("0");
					_selectlocation.setName("Select Location");
					_locationItems.add(_selectlocation);

					_locationAdapter.notifyDataSetChanged();

					new LocationAsync().execute("");

				}

				else {

					DropDownItem _selectlocation = new DropDownItem();
					_selectlocation.setStrId("0");
					_selectlocation.setName("Select Location");
					_locationItems.add(_selectlocation);

					_locationAdapter.notifyDataSetChanged();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spin_location.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				location_id = _locationAdapter.getItem(position).getStrId();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		img_call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String number = "8080 824 824";

				Intent dial = new Intent();
				dial.setAction("android.intent.action.DIAL");
				dial.setData(Uri.parse("tel:" + number));
				startActivity(dial);

			}
		});

		btn_signup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				firstname = et_firstname.getText().toString();
				lastname = et_lastname.getText().toString();
				phone = et_phone.getText().toString();
				pincode = et_pincode.getText().toString();
				email = et_email.getText().toString();
				password = et_password.getText().toString();
				confirmpassword = et_confirmpassword.getText().toString();

				if (firstname.equals("") || lastname.equals("")
						|| phone.equals("") || email.equals("")
						|| password.equals("") || confirmpassword.equals("")) {

					alert_dialog = new Dialog(SignupActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please Fill Up All the Fields");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});
				}

				else if (phone.length() < 10) {

					Toast.makeText(SignupActivity.this,
							"Phone Number should be 10 digit",
							Toast.LENGTH_SHORT).show();
				}

				else if (!password.equals(confirmpassword)) {

					alert_dialog = new Dialog(SignupActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg
							.setText("Password and confirm Password not matched");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});

				}

				/*
				 * else if (state_id.equals("0")) {
				 * 
				 * alert_dialog = new Dialog(SignupActivity.this);
				 * alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				 * alert_dialog.setContentView(R.layout.dialog_alert);
				 * alert_dialog.show();
				 * 
				 * TextView alert_msg = (TextView) alert_dialog
				 * .findViewById(R.id.alert_msg); Button alert_ok = (Button)
				 * alert_dialog .findViewById(R.id.alert_ok);
				 * 
				 * alert_msg.setText("Please select Your State");
				 * 
				 * alert_ok.setOnClickListener(new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { // TODO
				 * Auto-generated method stub
				 * 
				 * alert_dialog.dismiss();
				 * 
				 * } });
				 * 
				 * }
				 * 
				 * else if (city_id.equals("0")) {
				 * 
				 * alert_dialog = new Dialog(SignupActivity.this);
				 * alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				 * alert_dialog.setContentView(R.layout.dialog_alert);
				 * alert_dialog.show();
				 * 
				 * TextView alert_msg = (TextView) alert_dialog
				 * .findViewById(R.id.alert_msg); Button alert_ok = (Button)
				 * alert_dialog .findViewById(R.id.alert_ok);
				 * 
				 * alert_msg.setText("Please select Your City");
				 * 
				 * alert_ok.setOnClickListener(new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { // TODO
				 * Auto-generated method stub
				 * 
				 * alert_dialog.dismiss();
				 * 
				 * } });
				 * 
				 * }
				 * 
				 * else if (location_id.equals("0")) {
				 * 
				 * alert_dialog = new Dialog(SignupActivity.this);
				 * alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				 * alert_dialog.setContentView(R.layout.dialog_alert);
				 * alert_dialog.show();
				 * 
				 * TextView alert_msg = (TextView) alert_dialog
				 * .findViewById(R.id.alert_msg); Button alert_ok = (Button)
				 * alert_dialog .findViewById(R.id.alert_ok);
				 * 
				 * alert_msg.setText("Please select Your Location");
				 * 
				 * alert_ok.setOnClickListener(new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { // TODO
				 * Auto-generated method stub
				 * 
				 * alert_dialog.dismiss();
				 * 
				 * } });
				 * 
				 * }
				 */

				else {

					if (Utils.checkConnectivity(SignupActivity.this)) {

						SignupAsynctask get_signup = new SignupAsynctask(
								SignupActivity.this);
						get_signup.signupintf = SignupActivity.this;
						get_signup.execute("0", firstname, lastname, phone,
								email, current_latitude, current_longitude,
								password, confirmpassword);

					}

					else {

						showNetworkDialog("internet");
					}
				}

			}
		});

	}

	private void loadLocation() {
		// TODO Auto-generated method stub

		_stateItems = new ArrayList<DropDownItem>();
		_stateAdapter = new DropDownAdapter(this, _stateItems);
		spin_state.setAdapter(_stateAdapter);

		_cityItems = new ArrayList<DropDownItem>();
		_cityAdapter = new DropDownAdapter(this, _cityItems);
		spin_city.setAdapter(_cityAdapter);

		_locationItems = new ArrayList<DropDownItem>();
		_locationAdapter = new DropDownAdapter(this, _locationItems);
		spin_location.setAdapter(_locationAdapter);

		new StateAsync().execute("");

	}

	public class StateAsync extends AsyncTask<String, Long, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				jsonstr = getStringFromFile(filePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return jsonstr;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null) {
				try {
					Log.e("result", result);

					JSONObject _response = new JSONObject(result);

					JSONObject _locationData = _response.getJSONObject("data");

					JSONArray _stateArray = _locationData.getJSONArray("state");

					DropDownItem _selectState = new DropDownItem();
					_selectState.setStrId("0");
					_selectState.setName("Select State");
					_stateItems.add(_selectState);

					for (int i = 0; i < _stateArray.length(); i++) {

						JSONObject _stateObj = _stateArray.getJSONObject(i);

						DropDownItem _stateitem = new DropDownItem();
						_stateitem.setStrId(_stateObj.getString("id"));
						_stateitem.setName(_stateObj.getString("name"));

						_stateItems.add(_stateitem);

					}

					_stateAdapter.notifyDataSetChanged();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

	}

	public class CityAsync extends AsyncTask<String, Long, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				jsonstr = getStringFromFile(filePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return jsonstr;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null) {
				try {
					Log.e("result", result);

					JSONObject _response = new JSONObject(result);

					JSONObject _locationData = _response.getJSONObject("data");

					JSONArray _cityArray = _locationData.getJSONArray("city");

					for (int i = 0; i < _cityArray.length(); i++) {

						JSONObject _cityObj = _cityArray.getJSONObject(i);

						String get_state_id = _cityObj.getString("stateid");

						if (get_state_id.equals(state_id)) {

							DropDownItem _cityitem = new DropDownItem();
							_cityitem.setStrId(_cityObj.getString("id"));
							_cityitem.setName(_cityObj.getString("name"));

							_cityItems.add(_cityitem);

						}

					}

					_cityAdapter.notifyDataSetChanged();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

	}

	public class LocationAsync extends AsyncTask<String, Long, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				jsonstr = getStringFromFile(filePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return jsonstr;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null) {
				try {
					Log.e("result", result);

					JSONObject _response = new JSONObject(result);

					JSONObject _locationData = _response.getJSONObject("data");

					JSONArray _locationArray = _locationData
							.getJSONArray("location");

					for (int i = 0; i < _locationArray.length(); i++) {

						JSONObject _locationObj = _locationArray
								.getJSONObject(i);

						String get_city_id = _locationObj.getString("cityid");

						if (get_city_id.equals(city_id)) {

							DropDownItem _locationitem = new DropDownItem();
							_locationitem
									.setStrId(_locationObj.getString("id"));
							_locationitem.setName(_locationObj
									.getString("name"));

							_locationItems.add(_locationitem);

						}

					}

					_locationAdapter.notifyDataSetChanged();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

	}

	public String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

	public String getStringFromFile(String filePath) throws Exception {
		File fl = new File(filePath);
		FileInputStream fin = new FileInputStream(fl);
		String ret = convertStreamToString(fin);
		// Make sure you close all streams.
		fin.close();
		return ret;
	}

	@Override
	public void onStartedSignup() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(SignupActivity.this);
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedSignup(String errorcode, String message,
			String email, String access_token) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (errorcode.equals("0")) {

			sharedpreference.saveLoginflag("1");
			sharedpreference.saveUserEmail(email);
			sharedpreference.saveAccessToken(access_token);

			String user_email = sharedpreference.getUserEmail();
			String user_access_token = sharedpreference.getAccessToken();

			if (Utils.checkConnectivity(SignupActivity.this)) {

				GetProfileAsynctask get_profile = new GetProfileAsynctask(
						SignupActivity.this);
				get_profile.getprofileintf = SignupActivity.this;
				get_profile.execute(user_email, user_access_token);

			}

			else {

				showNetworkDialog("internet");
			}

		}

		else {

			alert_dialog = new Dialog(SignupActivity.this);
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_alert);
			alert_dialog.show();

			TextView alert_msg = (TextView) alert_dialog
					.findViewById(R.id.alert_msg);
			Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

			alert_msg.setText(message);

			alert_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					alert_dialog.dismiss();

				}
			});
		}

	}

	@Override
	public void onStartedGetProfile() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(SignupActivity.this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedGetProfile(String errorcode, String message,
			String firstname, String lastname, String email, String phone,
			String dob, String gender, String marital_status,
			String address_title, String state, String city, String location,
			String landmark, String street, String flat_no, String address,
			String pincode) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (errorcode.equals("0")) {

			sharedpreference.saveFirstName(firstname);
			sharedpreference.saveLastName(lastname);
			sharedpreference.savePhone(phone);
			sharedpreference.saveDOB(dob);
			sharedpreference.saveGender(gender);
			sharedpreference.saveMaritalStatus(marital_status);
			sharedpreference.saveAddressTitle(address_title);
			sharedpreference.saveState(state);
			sharedpreference.saveCity(city);
			sharedpreference.saveLocation(location);
			sharedpreference.savelandmark(landmark);
			sharedpreference.saveStreet(street);
			sharedpreference.saveFlatNo(flat_no);
			sharedpreference.saveAddress(address);
			sharedpreference.savePincode(pincode);

			startActivity(new Intent(SignupActivity.this,
					HomeMainActivity.class));
			finish();

		}

		else {

			alert_dialog = new Dialog(SignupActivity.this);
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_alert);
			alert_dialog.show();

			TextView alert_msg = (TextView) alert_dialog
					.findViewById(R.id.alert_msg);
			Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

			alert_msg.setText(message);

			alert_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					alert_dialog.dismiss();

				}
			});
		}

	}

	@SuppressWarnings("deprecation")
	public void showNetworkDialog(final String message) {
		// final exit of application
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SignupActivity.this);
		builder.setTitle(getResources().getString(R.string.app_name));
		if (message.equals("gps")) {
			builder.setMessage(getResources().getString(R.string.gps_message));
		} else if (message.equals("internet")) {
			builder.setMessage(getResources().getString(R.string.net_message));
		}
		AlertDialog alert = builder.create();
		alert.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (message.equals("gps")) {
					Intent i = new Intent(
							android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivityForResult(i, 1);
				} else if (message.equals("internet")) {
					Intent i = new Intent(
							android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					startActivityForResult(i, 2);
					Intent i1 = new Intent(
							android.provider.Settings.ACTION_WIFI_SETTINGS);
					startActivityForResult(i1, 3);
				} else {
					// do nothing
				}
			}
		});
		// Showing Alert Message
		alert.show();
	}

}