package com.zimmber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.zimmber.R;
import com.zimmber.adapter.DropDownAdapter;
import com.zimmber.asynctask.GetProfileAsynctask;
import com.zimmber.asynctask.UpdateProfileAsynctask;
import com.zimmber.bin.DropDownItem;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.GetProfileInterface;
import com.zimmber.interfaces.UpdateprofileInterface;
import com.zimmber.networkutil.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class UpdateProfileActivity extends Activity implements
		UpdateprofileInterface, GetProfileInterface {

	ImageView img_home;
	EditText et_firstname, et_lastname, et_phone, et_dob, et_address_title,
			et_landmark, et_street, et_flatno, et_address, et_pincode;
	Spinner spin_gender, spin_marital_status, spin_title, spin_state,
			spin_city, spin_location;
	Button btn_save_profile;

	private String[] genderlist = { "Select Gender", "Male", "Female" };

	private String[] maritallist = { "Select Marital Status", "Married",
			"UnMarried" };

	private String[] titlelist = { "Select Title", "My Pad",
			"Dad & Mom's Place", "The In-Law's", "The Secret Place", "Other" };

	String email = "", access_token = "", firstname = "", lastname = "",
			phone = "", dob = "", gender_id = "", gender = "",
			marital_status_id = "", marital_status = "", address_id = "",
			address_title = "", state_id = "", city_id = "", location_id = "",
			landmark = "", street = "", flat_no = "", address = "",
			pincode = "";

	int state_flag = 0, city_flag = 0, location_flag = 0;

	String filePath;
	String jsonstr = null;;

	ArrayList<DropDownItem> _genderItems;
	ArrayList<DropDownItem> _maritalItems;
	ArrayList<DropDownItem> _titleItems;
	ArrayList<DropDownItem> _stateItems;
	ArrayList<DropDownItem> _cityItems;
	ArrayList<DropDownItem> _locationItems;

	DropDownAdapter _genderAdapter;
	DropDownAdapter _maritalAdapter;
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
		setContentView(R.layout.update_profile);

		((ZimmberApp) getApplication())
				.getTracker(ZimmberApp.TrackerName.APP_TRACKER);

		filePath = Environment.getExternalStorageDirectory() + File.separator
				+ getPackageName() + File.separator
				+ Constants.JSON_LOCATION_FILE_NAME;

		initialize();

		onclick();

		loadLocation();

		// ////////////////////////////////////////////////////////////////

		_genderItems = new ArrayList<DropDownItem>();

		for (int i = 0; i < genderlist.length; i++) {

			DropDownItem _gender = new DropDownItem();
			_gender.setStrId(String.valueOf(i));
			_gender.setName(genderlist[i]);

			_genderItems.add(_gender);

		}

		_genderAdapter = new DropDownAdapter(UpdateProfileActivity.this,
				_genderItems);
		spin_gender.setAdapter(_genderAdapter);

		// //////////////////////////////////////////////////////////////////

		_maritalItems = new ArrayList<DropDownItem>();

		for (int i = 0; i < maritallist.length; i++) {

			DropDownItem _marital = new DropDownItem();
			_marital.setStrId(String.valueOf(i));
			_marital.setName(maritallist[i]);

			_maritalItems.add(_marital);

		}

		_maritalAdapter = new DropDownAdapter(UpdateProfileActivity.this,
				_maritalItems);
		spin_marital_status.setAdapter(_maritalAdapter);

		// ////////////////////////////////////////////////////////////////////

		email = sharedpreference.getUserEmail();
		access_token = sharedpreference.getAccessToken();
		et_firstname.setText(sharedpreference.getFirstName());
		et_lastname.setText(sharedpreference.getLastName());
		et_phone.setText(sharedpreference.getPhone());
		et_dob.setText(sharedpreference.getDOB());
		et_landmark.setText(sharedpreference.getLandmark());
		et_street.setText(sharedpreference.getStreet());
		et_flatno.setText(sharedpreference.getFlatNo());
		et_address.setText(sharedpreference.getAddress());
		et_pincode.setText(sharedpreference.getPincode());

		if (sharedpreference.getAddressTitle().length() > 0) {

			et_address_title.setText(sharedpreference.getAddressTitle());

			spin_title.setVisibility(View.GONE);
			et_address_title.setVisibility(View.VISIBLE);

		}

		for (int gender_pos = 0; gender_pos < _genderItems.size(); gender_pos++) {
			if (_genderItems.get(gender_pos).getName()
					.equalsIgnoreCase(sharedpreference.getGender())) {
				spin_gender.setSelection(gender_pos);
			}
		}

		for (int marital_pos = 0; marital_pos < _maritalItems.size(); marital_pos++) {

			if (_maritalItems.get(marital_pos).getName()
					.equalsIgnoreCase(sharedpreference.getMaritalStatus())) {
				spin_marital_status.setSelection(marital_pos);
			}
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(UpdateProfileActivity.this)
				.reportActivityStart(UpdateProfileActivity.this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(UpdateProfileActivity.this)
				.reportActivityStop(UpdateProfileActivity.this);
	}

	private void initialize() {
		// TODO Auto-generated method stub

		img_home = (ImageView) findViewById(R.id.img_home);

		et_firstname = (EditText) findViewById(R.id.et_firstname);
		et_lastname = (EditText) findViewById(R.id.et_lastname);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_dob = (EditText) findViewById(R.id.et_dob);
		et_address_title = (EditText) findViewById(R.id.et_address_title);
		et_landmark = (EditText) findViewById(R.id.et_landmark);
		et_street = (EditText) findViewById(R.id.et_street);
		et_flatno = (EditText) findViewById(R.id.et_flatno);
		et_address = (EditText) findViewById(R.id.et_address);
		et_pincode = (EditText) findViewById(R.id.et_pincode);

		spin_gender = (Spinner) findViewById(R.id.spin_gender);
		spin_marital_status = (Spinner) findViewById(R.id.spin_marital_status);
		spin_title = (Spinner) findViewById(R.id.spin_title);
		spin_state = (Spinner) findViewById(R.id.spin_state);
		spin_city = (Spinner) findViewById(R.id.spin_city);
		spin_location = (Spinner) findViewById(R.id.spin_location);

		btn_save_profile = (Button) findViewById(R.id.btn_save_profile);

		sharedpreference = new SharedPreferenceClass(UpdateProfileActivity.this);

	}

	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(UpdateProfileActivity.this,
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

				Intent i = new Intent(UpdateProfileActivity.this,
						HomeMainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();

			}
		});

		spin_gender.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				gender_id = _genderAdapter.getItem(position).getStrId();
				gender = _genderAdapter.getItem(position).getName();

				if (gender_id.equals("0")) {

					gender = "";
				}

				if (gender_id.equals("1")) {

					gender = "male";
				}

				if (gender_id.equals("2")) {

					gender = "female";
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spin_marital_status
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub

						marital_status_id = _maritalAdapter.getItem(position)
								.getStrId();
						marital_status = _maritalAdapter.getItem(position)
								.getName();

						if (marital_status_id.equals("0")) {

							marital_status = "";

						}

						if (marital_status_id.equals("1")) {

							marital_status = "married";

						}

						if (marital_status_id.equals("2")) {

							marital_status = "unmarried";

						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

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

					city_flag++;

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

					location_flag++;

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

		et_dob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ShowDatePickerDialog();

			}
		});

		btn_save_profile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				firstname = et_firstname.getText().toString();
				lastname = et_lastname.getText().toString();
				phone = et_phone.getText().toString();
				dob = et_dob.getText().toString();
				landmark = et_landmark.getText().toString();
				street = et_street.getText().toString();
				flat_no = et_flatno.getText().toString();
				address = et_address.getText().toString();
				pincode = et_pincode.getText().toString();

				if (firstname.equals("")) {

					alert_dialog = new Dialog(UpdateProfileActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please Enter First Name");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});
				}

				else if (lastname.equals("")) {

					alert_dialog = new Dialog(UpdateProfileActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please Enter Last Name");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});
				}

				else if (phone.equals("")) {

					alert_dialog = new Dialog(UpdateProfileActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please Enter Phone Number");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});
				}

				else if (phone.length() < 10) {

					Toast.makeText(UpdateProfileActivity.this,
							"Phone Number should be 10 digit",
							Toast.LENGTH_SHORT).show();
				}

				else if (address_title.equals("")) {

					alert_dialog = new Dialog(UpdateProfileActivity.this);
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

					alert_dialog = new Dialog(UpdateProfileActivity.this);
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

					alert_dialog = new Dialog(UpdateProfileActivity.this);
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

					alert_dialog = new Dialog(UpdateProfileActivity.this);
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

					alert_dialog = new Dialog(UpdateProfileActivity.this);
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

					if (Utils.checkConnectivity(UpdateProfileActivity.this)) {

						UpdateProfileAsynctask update_profile = new UpdateProfileAsynctask(
								UpdateProfileActivity.this);
						update_profile.updateprofileintf = UpdateProfileActivity.this;
						update_profile.execute(email, access_token, firstname,
								lastname, phone, dob, gender, marital_status,
								state_id, city_id, location_id, landmark,
								street, flat_no, address, pincode,
								address_title);

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

		_titleAdapter = new DropDownAdapter(UpdateProfileActivity.this,
				_titleItems);
		spin_title.setAdapter(_titleAdapter);

		_stateItems = new ArrayList<DropDownItem>();
		_stateAdapter = new DropDownAdapter(UpdateProfileActivity.this,
				_stateItems);
		spin_state.setAdapter(_stateAdapter);

		_cityItems = new ArrayList<DropDownItem>();
		_cityAdapter = new DropDownAdapter(UpdateProfileActivity.this,
				_cityItems);
		spin_city.setAdapter(_cityAdapter);

		_locationItems = new ArrayList<DropDownItem>();
		_locationAdapter = new DropDownAdapter(UpdateProfileActivity.this,
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

					for (int state_pos = 0; state_pos < _stateItems.size(); state_pos++) {
						if (_stateItems.get(state_pos).getName()
								.equalsIgnoreCase(sharedpreference.getState())) {
							spin_state.setSelection(state_pos);
						}
					}

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

					if (city_flag == 1) {

						for (int city_pos = 0; city_pos < _cityItems.size(); city_pos++) {

							if (_cityItems
									.get(city_pos)
									.getName()
									.equalsIgnoreCase(
											sharedpreference.getCity())) {

								spin_city.setSelection(city_pos);

							}

						}

					}

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

					_locationItems.clear();

					DropDownItem _selectlocation = new DropDownItem();
					_selectlocation.setStrId("0");
					_selectlocation.setName("Select Location");
					_locationItems.add(_selectlocation);

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

					if (location_flag == 1 || location_flag == 2) {

						for (int location_pos = 0; location_pos < _locationItems
								.size(); location_pos++) {
							if (_locationItems
									.get(location_pos)
									.getName()
									.equalsIgnoreCase(
											sharedpreference.getLocation())) {
								spin_location.setSelection(location_pos);
							}
						}

					}

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

	private void ShowDatePickerDialog() {
		// TODO Auto-generated method stub

		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog datepicker = new DatePickerDialog(
				UpdateProfileActivity.this, datePickerListener, mYear, mMonth,
				mDay);
		datepicker.show();

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			String year = String.valueOf(selectedYear);
			String month = "";
			String day = "";

			if (selectedMonth < 9) {
				month = "0" + String.valueOf(selectedMonth + 1);
			} else {
				month = String.valueOf(selectedMonth + 1);
			}

			if (selectedDay < 10) {
				day = "0" + String.valueOf(selectedDay);
			} else {
				day = String.valueOf(selectedDay);
			}

			dob = year + "-" + month + "-" + day;

			et_dob.setText(dob);

		}
	};

	@Override
	public void onStartedUpdateProfile() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(UpdateProfileActivity.this);
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedUpdateProfile(String errorcode, String message) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		alert_dialog = new Dialog(UpdateProfileActivity.this);
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

				if (Utils.checkConnectivity(UpdateProfileActivity.this)) {

					GetProfileAsynctask get_profile = new GetProfileAsynctask(
							UpdateProfileActivity.this);
					get_profile.getprofileintf = UpdateProfileActivity.this;
					get_profile.execute(email, access_token);

				}

				else {

					showNetworkDialog("internet");
				}

			}
		});

	}

	@SuppressWarnings("deprecation")
	public void showNetworkDialog(final String message) {
		// final exit of application
		AlertDialog.Builder builder = new AlertDialog.Builder(
				UpdateProfileActivity.this);
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

	@Override
	public void onStartedGetProfile() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(UpdateProfileActivity.this);
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

			Intent i = new Intent(UpdateProfileActivity.this,
					MyAccountActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();

		}

		else {

			alert_dialog = new Dialog(UpdateProfileActivity.this);
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

}
