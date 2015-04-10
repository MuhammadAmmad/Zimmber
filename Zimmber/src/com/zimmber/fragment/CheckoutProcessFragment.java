package com.zimmber.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.viewpagerindicator.LinePageIndicator;
import com.zimmber.Constants;
import com.zimmber.CustomTimePickerDialog;
import com.zimmber.HomeMainActivity;
import com.zimmber.R;
import com.zimmber.adapter.DropDownAdapter;
import com.zimmber.adapter.MediaPagerAdapter;
import com.zimmber.asynctask.CheckPincodeAsynctask;
import com.zimmber.asynctask.CheckoutAsynctask;
import com.zimmber.bin.DropDownItem;
import com.zimmber.database.DBAdapter;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.CheckPincodeInterface;
import com.zimmber.interfaces.CheckoutInterface;
import com.zimmber.networkutil.Utils;

public class CheckoutProcessFragment extends Fragment implements
		CheckPincodeInterface, CheckoutInterface {

	View rootView;

	private ViewPager pagerContainer;
	private LinePageIndicator indicator;
	private LayoutInflater _layoutInflater;

	Vector<View> pages;
	View page1, page2;
	MediaPagerAdapter pagerAdapter;

	ImageView img_menu, img_home;

	Boolean menuOpen = false;
	PopupWindow popupWindow;

	String email = "";

	EditText et_firstname, et_lastname, et_phone, et_landmark, et_street,
			et_flatno, et_address, et_pincode;
	Spinner spin_state, spin_city, spin_location;
	TextView tv_next;
	String firstname = "", lastname = "", phone = "", state_id = "",
			city_id = "", location_id = "", landmark = "", street = "",
			flatno = "", address = "", pincode = "", total_cost = "";

	EditText et_select_date, et_select_time;
	String select_date = "", select_time = "";
	RadioGroup rdGroup;
	RadioButton rdbtnone, rdbtntwo, rdbtnthree;
	Button btn_checkout;

	String payment_type = "1", payment_status = "0";

	ArrayList<DropDownItem> _stateItems;
	ArrayList<DropDownItem> _cityItems;
	ArrayList<DropDownItem> _locationItems;

	DropDownAdapter _stateAdapter;
	DropDownAdapter _cityAdapter;
	DropDownAdapter _locationAdapter;

	String filePath;
	String jsonstr = null;

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	SharedPreferenceClass sharedpreference;

	DBAdapter db;
	boolean remove;

	ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();

	static final String KEY_CHECKOUTLIST_ID = "id";

	static final String KEY_SERVICE_ID = "service_id";

	static final String KEY_SERVICE_NAME = "service_name";

	static final String KEY_VAR1_ID = "var1_id";

	static final String KEY_VAR1_NAME = "var1_name";

	static final String KEY_VAR2_ID = "var2_id";

	static final String KEY_VAR2_NAME = "var2_name";

	static final String KEY_VAR3_ID = "var3_id";

	static final String KEY_VAR3_NAME = "var3_name";

	static final String KEY_VAR4_ID = "var4_id";

	static final String KEY_VAR4_NAME = "var4_name";

	static final String KEY_ACTIVITY_ID = "activity_id";

	static final String KEY_ACTIVITY_NAME = "activity_name";

	static final String KEY_ACTIVITY_COUNT = "activity_count";

	static final String KEY_SERVICE_STATUS = "service_status";

	static final String KEY_SERVICE_PRICE = "service_price";

	static final String KEY_CUST_VAR1 = "cust_var1";

	static final String KEY_CUST_VAR2 = "cust_var2";

	static final String KEY_CUST_VAR3 = "cust_var3";

	static final String KEY_CUST_VAR4 = "cust_var4";

	static final String KEY_COUPON_CODE = "coupon_code";

	String checkout_data = "";

	StringBuilder service_details = new StringBuilder();

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater
				.inflate(R.layout.checkout_process, container, false);

		filePath = Environment.getExternalStorageDirectory() + File.separator
				+ getActivity().getPackageName() + File.separator
				+ Constants.JSON_LOCATION_FILE_NAME;

		initialize();

		Onclick();

		loadLocation();

		email = sharedpreference.getUserEmail();
		Log.d("email", email);

		total_cost = sharedpreference.getTotalServicePrice();
		Log.d("total_cost", total_cost);

		sharedpreference.saveBookingId("");

		return rootView;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		pagerContainer = (ViewPager) rootView.findViewById(R.id.pagercontainer);
		indicator = (LinePageIndicator) rootView.findViewById(R.id.indicator);

		_layoutInflater = getActivity().getLayoutInflater();
		pages = new Vector<View>();

		page1 = _layoutInflater.inflate(R.layout.checkout_your_details, null);
		page2 = _layoutInflater.inflate(R.layout.checkout_schedule_payment,
				null);

		pages.add(page1);

		pagerAdapter = new MediaPagerAdapter(getActivity(), pages);
		pagerContainer.setAdapter(pagerAdapter);

		indicator.setViewPager(pagerContainer);
		indicator.setClickable(true);

		et_firstname = (EditText) page1.findViewById(R.id.et_firstname);
		et_lastname = (EditText) page1.findViewById(R.id.et_lastname);
		et_landmark = (EditText) page1.findViewById(R.id.et_landmark);
		et_street = (EditText) page1.findViewById(R.id.et_street);
		et_flatno = (EditText) page1.findViewById(R.id.et_flatno);
		et_address = (EditText) page1.findViewById(R.id.et_address);
		et_pincode = (EditText) page1.findViewById(R.id.et_pincode);
		et_phone = (EditText) page1.findViewById(R.id.et_phone);
		spin_state = (Spinner) page1.findViewById(R.id.spin_state);
		spin_city = (Spinner) page1.findViewById(R.id.spin_city);
		spin_location = (Spinner) page1.findViewById(R.id.spin_location);
		tv_next = (TextView) page1.findViewById(R.id.tv_next);

		et_select_date = (EditText) page2.findViewById(R.id.et_select_date);
		et_select_time = (EditText) page2.findViewById(R.id.et_select_time);

		rdGroup = (RadioGroup) page2.findViewById(R.id.rdGroup);
		rdbtnone = (RadioButton) page2.findViewById(R.id.rdbtnone);
		rdbtntwo = (RadioButton) page2.findViewById(R.id.rdbtntwo);
		rdbtnthree = (RadioButton) page2.findViewById(R.id.rdbtnthree);
		btn_checkout = (Button) page2.findViewById(R.id.btn_checkout);

		sharedpreference = new SharedPreferenceClass(getActivity());

		tv_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				firstname = et_firstname.getText().toString();
				lastname = et_lastname.getText().toString();
				landmark = et_landmark.getText().toString();
				pincode = et_pincode.getText().toString();
				phone = et_phone.getText().toString();

				if (firstname.equals("")) {

					Toast.makeText(getActivity(), "Please Enter First Name",
							Toast.LENGTH_SHORT).show();
				}

				else if (lastname.equals("")) {

					Toast.makeText(getActivity(), "Please Enter Last Name",
							Toast.LENGTH_SHORT).show();
				}

				else if (phone.equals("")) {

					Toast.makeText(getActivity(), "Please Enter Phone Number",
							Toast.LENGTH_SHORT).show();
				}

				else if (phone.length() < 10) {

					Toast.makeText(getActivity(),
							"Phone Number should be 10 digit",
							Toast.LENGTH_SHORT).show();
				}

				else if (state_id.equals("0")) {

					Toast.makeText(getActivity(), "Please Enter State",
							Toast.LENGTH_SHORT).show();
				}

				else if (city_id.equals("0")) {

					Toast.makeText(getActivity(), "Please Enter City",
							Toast.LENGTH_SHORT).show();
				}

				else if (location_id.equals("0")) {

					Toast.makeText(getActivity(), "Please Enter Location",
							Toast.LENGTH_SHORT).show();
				}

				else if (pincode.equals("")) {

					Toast.makeText(getActivity(), "Please Enter Pincode",
							Toast.LENGTH_SHORT).show();
				}

				else if (pincode.length() < 6) {

					Toast.makeText(getActivity(), "Pincode should be 6 digit",
							Toast.LENGTH_SHORT).show();

				}

				else {

					if (!pages.contains(page2)) {

						pages.add(page2);

						pagerAdapter = new MediaPagerAdapter(getActivity(),
								pages);
						pagerContainer.setAdapter(pagerAdapter);

						indicator.setViewPager(pagerContainer);
						indicator.setClickable(true);

						pagerContainer.setCurrentItem(1);

					}

					else {

						pagerContainer.setCurrentItem(1);
					}

				}

			}
		});

	}

	private void Onclick() {
		// TODO Auto-generated method stub

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

		et_pincode.addTextChangedListener(new TextWatcher() {

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

				pincode = et_pincode.getText().toString();

				if (pincode.length() == 6) {

					CheckPincodeAsynctask check_pincode = new CheckPincodeAsynctask(
							getActivity());
					check_pincode.checkpincodeintf = CheckoutProcessFragment.this;
					check_pincode.execute(pincode);
				}

			}
		});

		et_select_date.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ShowDatePickerDialog();

			}
		});

		et_select_time.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ShowTimePickerDialog();

			}
		});

		rdbtnone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				payment_type = "1";

			}
		});

		rdbtntwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				payment_type = "2";

			}
		});

		rdbtnthree.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				payment_type = "0";

			}
		});

		btn_checkout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (select_date.equals("")) {

					Toast.makeText(getActivity(), "Please Enter Order Date",
							Toast.LENGTH_SHORT).show();
				}

				else if (select_time.equals("")) {

					Toast.makeText(getActivity(), "Please Enter Order time",
							Toast.LENGTH_SHORT).show();
				}

				else {

					db = new DBAdapter(getActivity());
					db.open();

					menuItems = db.getRecords();

					try {

						JSONArray jobService = new JSONArray();

						String service[] = new String[menuItems.size()];
						String present_flag = "0";
						int j = 0;

						for (int val = 0; val < menuItems.size(); val++) {

							service[val] = " ";
						}

						for (int i = 0; i < menuItems.size(); i++) {

							JSONObject orderjsonObject = new JSONObject();
							orderjsonObject.put("service_id", menuItems.get(i)
									.get(KEY_SERVICE_ID));
							orderjsonObject.put("var_1",
									menuItems.get(i).get(KEY_VAR1_ID));
							orderjsonObject.put("customvar_1", menuItems.get(i)
									.get(KEY_CUST_VAR1));
							orderjsonObject.put("var_2",
									menuItems.get(i).get(KEY_VAR2_ID));
							orderjsonObject.put("customvar_2", menuItems.get(i)
									.get(KEY_CUST_VAR2));
							orderjsonObject.put("var_3",
									menuItems.get(i).get(KEY_VAR3_ID));
							orderjsonObject.put("customvar_3", menuItems.get(i)
									.get(KEY_CUST_VAR3));
							orderjsonObject.put("var_4",
									menuItems.get(i).get(KEY_VAR4_ID));
							orderjsonObject.put("customvar_4", menuItems.get(i)
									.get(KEY_CUST_VAR4));
							orderjsonObject.put("activity_id", menuItems.get(i)
									.get(KEY_ACTIVITY_ID));
							orderjsonObject.put("activity_count", menuItems
									.get(i).get(KEY_ACTIVITY_COUNT));
							orderjsonObject.put("price",
									menuItems.get(i).get(KEY_SERVICE_PRICE));
							orderjsonObject.put("status",
									menuItems.get(i).get(KEY_SERVICE_STATUS));
							orderjsonObject.put("couponCode", menuItems.get(i)
									.get(KEY_COUPON_CODE));

							jobService.put(orderjsonObject);

							for (int k = 0; k <= j; k++) {

								if (service[k].equals(menuItems.get(i).get(
										KEY_SERVICE_ID))) {

									present_flag = "1";
									k = j + 1;

								}

							}

							if (present_flag.equals("0")) {

								service[j] = menuItems.get(i).get(
										KEY_SERVICE_ID);
								service_details.append(menuItems.get(i).get(
										KEY_SERVICE_NAME)
										+ ",");
								j++;
							}

						}

						service_details.replace(service_details.length() - 1,
								service_details.length(), "");

						JSONObject userjsonObject = new JSONObject();
						userjsonObject.put("userEmail", email);
						userjsonObject.put("scheduleDate", select_date);
						userjsonObject.put("scheduleTime", select_time);
						userjsonObject.put("first_name", firstname);
						userjsonObject.put("last_name", lastname);
						userjsonObject.put("phone_no", phone);
						userjsonObject.put("addressId", "");
						userjsonObject.put("state", state_id);
						userjsonObject.put("city", city_id);
						userjsonObject.put("location", location_id);
						userjsonObject.put("land_mark", landmark);
						userjsonObject.put("street", street);
						userjsonObject.put("flat_no", flatno);
						userjsonObject.put("address", address);
						userjsonObject.put("pincode", pincode);
						userjsonObject.put("totalCost", total_cost);
						userjsonObject.put("paymentType", payment_type);
						userjsonObject.put("paymentStatus", payment_status);

						JSONArray checkOutService = new JSONArray();
						checkOutService.put(userjsonObject);

						JSONObject jsonObject = new JSONObject();
						jsonObject.put("checkOutService", checkOutService);
						jsonObject.put("jobService", jobService);

						JSONArray checkoutArray = new JSONArray();
						checkoutArray.put(jsonObject);

						JSONObject checkout = new JSONObject();
						checkout.put("checkout", checkoutArray);

						checkout_data = checkout.toString();
						Log.d("checkout", checkout_data);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (Utils.checkConnectivity(getActivity())) {

						CheckoutAsynctask get_checkout = new CheckoutAsynctask(
								getActivity());
						get_checkout.checkoutintf = CheckoutProcessFragment.this;
						get_checkout.execute(checkout_data);

						for (int i = 0; i < menuItems.size(); i++) {

							String checklist_id = menuItems.get(i)
									.get(KEY_CHECKOUTLIST_ID).toString();

							remove = db.deleteRecord(checklist_id);

						}

					}

					else {

						showNetworkDialog("internet");
					}

					db.close();

				}

			}
		});

	}

	protected void ShowTimePickerDialog() {
		// TODO Auto-generated method stub

		final Calendar c = Calendar.getInstance();
		int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMinute = c.get(Calendar.MINUTE);

		CustomTimePickerDialog timepicker = new CustomTimePickerDialog(
				getActivity(), timePickerListener, mHour, mMinute, false);
		timepicker.show();

	}

	private CustomTimePickerDialog.OnTimeSetListener timePickerListener = new CustomTimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {

			String hour = "";
			String minute = "";
			String ampm = "";

			if (selectedHour >= 12) {
				selectedHour -= 12;
				ampm = "PM";
			} else if (selectedHour == 0) {
				ampm = "AM";
			} else if (selectedHour == 12)
				ampm = "PM";
			else {
				ampm = "AM";
			}

			if (selectedHour < 10) {
				hour = "0" + String.valueOf(selectedHour);
			} else {
				hour = String.valueOf(selectedHour);
			}

			if (selectedMinute < 10) {
				minute = "0" + String.valueOf(selectedMinute);
			} else {
				minute = String.valueOf(selectedMinute);
			}

			if ((selectedHour < 9 && ampm.equals("AM"))
					|| (selectedHour >= 9 && selectedMinute > 0 && ampm
							.equals("PM"))) {

				alert_dialog = new Dialog(getActivity());
				alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alert_dialog.setContentView(R.layout.dialog_alert);
				alert_dialog.show();

				TextView alert_msg = (TextView) alert_dialog
						.findViewById(R.id.alert_msg);
				Button alert_ok = (Button) alert_dialog
						.findViewById(R.id.alert_ok);

				alert_msg
						.setText("Please call us at 8080 824 824 to avail services before 9 A.M and after 9 P.M. Thank You.");

				alert_ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						alert_dialog.dismiss();
						et_select_time.setText("Set Time");
						select_time = "";

					}
				});

			}

			else {

				select_time = hour + " : " + minute + " : " + ampm;
				et_select_time.setText(select_time);
			}

		}
	};

	private void ShowDatePickerDialog() {
		// TODO Auto-generated method stub

		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog datepicker = new DatePickerDialog(getActivity(),
				datePickerListener, mYear, mMonth, mDay);
		datepicker.getDatePicker()
				.setMinDate(System.currentTimeMillis() - 1000);
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

			select_date = year + "-" + month + "-" + day;

			et_select_date.setText(select_date);

		}
	};

	private void loadLocation() {
		// TODO Auto-generated method stub

		_stateItems = new ArrayList<DropDownItem>();
		_stateAdapter = new DropDownAdapter(getActivity(), _stateItems);
		spin_state.setAdapter(_stateAdapter);

		_cityItems = new ArrayList<DropDownItem>();
		_cityAdapter = new DropDownAdapter(getActivity(), _cityItems);
		spin_city.setAdapter(_cityAdapter);

		_locationItems = new ArrayList<DropDownItem>();
		_locationAdapter = new DropDownAdapter(getActivity(), _locationItems);
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
	public void onStartedCheckout() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedCheckout(String errorcode, String message,
			String booking_id) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (errorcode.equals("0")) {

			sharedpreference.saveBookingId(booking_id);

			if (payment_type.equals("1")) {

				((HomeMainActivity) getActivity()).loadPayuMoneyFragment();

			}

			else {

				alert_dialog = new Dialog(getActivity());
				alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alert_dialog.setContentView(R.layout.dialog_checkout);
				alert_dialog.show();

				TextView alert_header = (TextView) alert_dialog
						.findViewById(R.id.alert_header);
				TextView alert_msg = (TextView) alert_dialog
						.findViewById(R.id.alert_msg);
				Button alert_ok = (Button) alert_dialog
						.findViewById(R.id.alert_ok);

				alert_header.setText("Thank you for choosing "
						+ service_details + " on Zimmber App");
				alert_msg.setText("Zimmber Champ will be at your address on "
						+ select_date + ", " + select_time);

				alert_ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						alert_dialog.dismiss();

						startActivity(new Intent(getActivity(),
								HomeMainActivity.class));

						((HomeMainActivity) getActivity()).finish();

					}
				});

			}

		}

		else {

			alert_dialog = new Dialog(getActivity());
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_checkout);
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

					startActivity(new Intent(getActivity(),
							HomeMainActivity.class));

					((HomeMainActivity) getActivity()).finish();

				}
			});
		}

	}

	@SuppressWarnings("deprecation")
	public void showNetworkDialog(final String message) {
		// final exit of application
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
	public void onStartedCheckPincode() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Checking Service Availability...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedCheckPincode(String errorcode, String message) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (errorcode.equals("0")) {

			// Toast.makeText(getActivity(), "Service Available in your city",
			// Toast.LENGTH_SHORT).show();

		}

		else {

			alert_dialog = new Dialog(getActivity());
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_alert);
			alert_dialog.show();

			TextView alert_msg = (TextView) alert_dialog
					.findViewById(R.id.alert_msg);
			Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

			alert_msg
					.setText("Pray for us, we will be in your city pretty soon");

			alert_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					alert_dialog.dismiss();

					et_pincode.setText("");

					if (pages.contains(page2)) {

						pages.remove(page2);

						pagerAdapter = new MediaPagerAdapter(getActivity(),
								pages);
						pagerContainer.setAdapter(pagerAdapter);
						pagerAdapter.notifyDataSetChanged();

						indicator.setViewPager(pagerContainer);
						indicator.setClickable(true);

						pagerContainer.setCurrentItem(0);

					}

				}
			});
		}

	}

}
