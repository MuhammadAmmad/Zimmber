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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.zimmber.R;
import com.zimmber.adapter.DropDownAdapter;
import com.zimmber.asynctask.AddAddressAsynctask;
import com.zimmber.bin.DropDownItem;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.AddAddressInterface;
import com.zimmber.networkutil.Utils;

public class AddAddressActivity extends Activity implements AddAddressInterface {

	ImageView img_home;
	EditText et_address_title, et_landmark, et_street, et_flatno, et_address,
			et_pincode;
	Spinner spin_title, spin_state, spin_city, spin_location;
	Button btn_save_address;

	private String[] titlelist = { "Select Title", "My Pad",
			"Dad & Mom's Place", "The In-Law's", "The Secret Place", "Other" };

	String email = "", access_token = "", address_id = "", address_title = "",
			state_id = "", city_id = "", location_id = "", landmark = "",
			street = "", flat_no = "", address = "", pincode = "";

	String filePath;
	String jsonstr = null;;

	ArrayList<DropDownItem> _titleItems;
	ArrayList<DropDownItem> _stateItems;
	ArrayList<DropDownItem> _cityItems;
	ArrayList<DropDownItem> _locationItems;

	DropDownAdapter _titleAdapter;
	DropDownAdapter _stateAdapter;
	DropDownAdapter _cityAdapter;
	DropDownAdapter _locationAdapter;

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	SharedPreferenceClass sharedpreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.add_address);

		((ZimmberApp) getApplication())
				.getTracker(ZimmberApp.TrackerName.APP_TRACKER);

		filePath = Environment.getExternalStorageDirectory() + File.separator
				+ getPackageName() + File.separator
				+ Constants.JSON_LOCATION_FILE_NAME;

		initialize();

		onclick();

		loadLocation();

		email = sharedpreference.getUserEmail();
		access_token = sharedpreference.getAccessToken();

		

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(AddAddressActivity.this)
				.reportActivityStart(AddAddressActivity.this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(AddAddressActivity.this)
				.reportActivityStop(AddAddressActivity.this);
	}

	private void initialize() {
		// TODO Auto-generated method stub

		img_home = (ImageView) findViewById(R.id.img_home);

		et_address_title = (EditText) findViewById(R.id.et_address_title);
		et_landmark = (EditText) findViewById(R.id.et_landmark);
		et_street = (EditText) findViewById(R.id.et_street);
		et_flatno = (EditText) findViewById(R.id.et_flatno);
		et_address = (EditText) findViewById(R.id.et_address);
		et_pincode = (EditText) findViewById(R.id.et_pincode);

		spin_title = (Spinner) findViewById(R.id.spin_title);
		spin_state = (Spinner) findViewById(R.id.spin_state);
		spin_city = (Spinner) findViewById(R.id.spin_city);
		spin_location = (Spinner) findViewById(R.id.spin_location);

		btn_save_address = (Button) findViewById(R.id.btn_save_address);

		sharedpreference = new SharedPreferenceClass(AddAddressActivity.this);

	}

	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(AddAddressActivity.this,
					MyAccountActivity.class);
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

				Intent i = new Intent(AddAddressActivity.this,
						HomeMainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();

			}
		});

		spin_title.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				address_id = _titleAdapter.getItem(position).getStrId();
				address_title = _titleAdapter.getItem(position).getName();

				if (address_id.equals("0")) {

					address_title = "";
				}

				if (address_id.equals("5")) {

					address_title = "";
					spin_title.setVisibility(View.GONE);
					et_address_title.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		et_address_title.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
				

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
				address_title = et_address_title.getText().toString();

				if (address_title.length() == 0) {

					et_address_title.setVisibility(View.GONE);
					spin_title.setVisibility(View.VISIBLE);
					spin_title.setSelection(0);
				}

				

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

		btn_save_address.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				landmark = et_landmark.getText().toString();
				street = et_street.getText().toString();
				flat_no = et_flatno.getText().toString();
				address = et_address.getText().toString();
				pincode = et_pincode.getText().toString();

				if (address_title.equals("")) {

					alert_dialog = new Dialog(AddAddressActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please Add a Title for Address");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});
				}

				else if (state_id.equals("0")) {

					alert_dialog = new Dialog(AddAddressActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please select Your State");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});

				}

				else if (city_id.equals("0")) {

					alert_dialog = new Dialog(AddAddressActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please select Your City");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});

				}

				else if (location_id.equals("0")) {

					alert_dialog = new Dialog(AddAddressActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please select Your Location");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});

				}

				else if (pincode.equals("")) {

					alert_dialog = new Dialog(AddAddressActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please Enter Pincode");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});
				}

				else {

					if (Utils.checkConnectivity(AddAddressActivity.this)) {

						AddAddressAsynctask add_address = new AddAddressAsynctask(
								AddAddressActivity.this);
						add_address.addaddressintf = AddAddressActivity.this;
						add_address.execute(email, access_token, state_id,
								city_id, location_id, landmark, street,
								flat_no, address, pincode, address_title);

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
		
		_titleItems = new ArrayList<DropDownItem>();

		for (int i = 0; i < titlelist.length; i++) {

			DropDownItem _title = new DropDownItem();
			_title.setStrId(String.valueOf(i));
			_title.setName(titlelist[i]);

			_titleItems.add(_title);
		}

		_titleAdapter = new DropDownAdapter(AddAddressActivity.this,
				_titleItems);
		spin_title.setAdapter(_titleAdapter);

		_stateItems = new ArrayList<DropDownItem>();
		_stateAdapter = new DropDownAdapter(AddAddressActivity.this,
				_stateItems);
		spin_state.setAdapter(_stateAdapter);

		_cityItems = new ArrayList<DropDownItem>();
		_cityAdapter = new DropDownAdapter(AddAddressActivity.this, _cityItems);
		spin_city.setAdapter(_cityAdapter);

		_locationItems = new ArrayList<DropDownItem>();
		_locationAdapter = new DropDownAdapter(AddAddressActivity.this,
				_locationItems);
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
	public void onStartedAddAddress() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(AddAddressActivity.this);
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedAddAddress(String errorcode, String message) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (errorcode.equals("0")) {

			alert_dialog = new Dialog(AddAddressActivity.this);
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_alert);
			alert_dialog.show();

			TextView alert_msg = (TextView) alert_dialog
					.findViewById(R.id.alert_msg);
			Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

			alert_msg.setText("New Address Added Successfully");

			alert_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					alert_dialog.dismiss();

					Intent i = new Intent(AddAddressActivity.this,
							MyAccountActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();

				}
			});

		}

		else {

			alert_dialog = new Dialog(AddAddressActivity.this);
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

					Intent i = new Intent(AddAddressActivity.this,
							MyAccountActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();

				}
			});

		}

	}

	@SuppressWarnings("deprecation")
	public void showNetworkDialog(final String message) {
		// final exit of application
		AlertDialog.Builder builder = new AlertDialog.Builder(
				AddAddressActivity.this);
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
