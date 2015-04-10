package com.zimmber.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import com.zimmber.Constants;
import com.zimmber.HomeMainActivity;
import com.zimmber.R;
import com.zimmber.adapter.DropDownAdapter;
import com.zimmber.asynctask.CheckPincodeAsynctask;
import com.zimmber.asynctask.ExpresscheckoutAsynctask;
import com.zimmber.bin.DropDownItem;
import com.zimmber.interfaces.CheckPincodeInterface;
import com.zimmber.interfaces.ExpressCheckoutInterface;

public class ExpressCheckoutFragment extends Fragment implements
		CheckPincodeInterface, ExpressCheckoutInterface {

	View rootView;

	Spinner spinner1;
	EditText etName, etEmail, etPhone, etPincode;
	ImageView img_call;
	LinearLayout ll_checkout;

	ArrayList<DropDownItem> _spinner1Items;

	DropDownAdapter _spinner1Adapter;

	String filePath;
	String jsonstr = null;

	String service_id = "", service_name = "", name = "", email = "",
			phone = "", pincode = "";

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater
				.inflate(R.layout.express_checkout, container, false);

		filePath = Environment.getExternalStorageDirectory() + File.separator
				+ getActivity().getPackageName() + File.separator
				+ Constants.JSON_SERVICE_FILE_NAME;

		initialize();

		onclick();

		loadDDL();

		return rootView;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
		etName = (EditText) rootView.findViewById(R.id.etName);
		etEmail = (EditText) rootView.findViewById(R.id.etEmail);
		etPhone = (EditText) rootView.findViewById(R.id.etPhone);
		etPincode = (EditText) rootView.findViewById(R.id.etPincode);
		img_call = (ImageView) rootView.findViewById(R.id.img_call);
		ll_checkout = (LinearLayout) rootView.findViewById(R.id.ll_checkout);

	}

	private void onclick() {
		// TODO Auto-generated method stub

		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				service_id = _spinner1Adapter.getItem(position).getStrId();
				service_name = _spinner1Adapter.getItem(position).getName();

				if (service_id.equals("0")) {

					service_id = "";
					service_name = "";

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		etPincode.addTextChangedListener(new TextWatcher() {

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

				pincode = etPincode.getText().toString();

				if (pincode.length() == 6) {

					CheckPincodeAsynctask check_pincode = new CheckPincodeAsynctask(
							getActivity());
					check_pincode.checkpincodeintf = ExpressCheckoutFragment.this;
					check_pincode.execute(pincode);
				}

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

		ll_checkout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				name = etName.getText().toString();
				email = etEmail.getText().toString();
				phone = etPhone.getText().toString();
				pincode = etPincode.getText().toString();

				if (service_name.equals("")) {

					Toast.makeText(getActivity(),
							"Please Enter Required Service Name",
							Toast.LENGTH_SHORT).show();
				}

				else if (name.equals("")) {

					Toast.makeText(getActivity(), "Please Enter Your Name",
							Toast.LENGTH_SHORT).show();
				}

				else if (email.equals("")) {

					Toast.makeText(getActivity(), "Please Enter Your Email",
							Toast.LENGTH_SHORT).show();
				}

				else if (phone.equals("")) {

					Toast.makeText(getActivity(),
							"Please Enter Your Phone Number",
							Toast.LENGTH_SHORT).show();
				}

				else if (phone.length() < 10) {

					Toast.makeText(getActivity(),
							"Phone Number should be 10 digit",
							Toast.LENGTH_SHORT).show();
				}

				else if (pincode.equals("")) {

					Toast.makeText(getActivity(), "Please Enter Your Pincode",
							Toast.LENGTH_SHORT).show();
				}

				else if (pincode.length() < 6) {

					Toast.makeText(getActivity(), "Pincode should be 6 digit",
							Toast.LENGTH_SHORT).show();

				}

				else {

					ExpresscheckoutAsynctask express_checkout = new ExpresscheckoutAsynctask(
							getActivity());
					express_checkout.expresscheckoutintf = ExpressCheckoutFragment.this;
					express_checkout.execute(service_name, name, email, phone,
							pincode);

				}
			}
		});

	}

	public void loadDDL() {

		_spinner1Items = new ArrayList<DropDownItem>();
		_spinner1Adapter = new DropDownAdapter(getActivity(), _spinner1Items);
		spinner1.setAdapter(_spinner1Adapter);

		new DDLAsync().execute("");
	}

	class DDLAsync extends AsyncTask<String, Long, String> {

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

					JSONObject _ddlData = _response.getJSONObject("data");

					JSONArray _servicesArray = _ddlData
							.getJSONArray("getServiceList");

					DropDownItem _select = new DropDownItem();
					_select.setStrId("0");
					_select.setName("Select Service");

					_spinner1Items.add(_select);

					for (int i = 0; i < _servicesArray.length(); i++) {

						JSONObject serviceobj = _servicesArray.getJSONObject(i);

						DropDownItem _item = new DropDownItem();
						_item.setStrId(serviceobj.getString("id"));
						_item.setName(serviceobj.getString("name"));

						_spinner1Items.add(_item);

						_spinner1Adapter.notifyDataSetChanged();

					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

		public String convertStreamToString(InputStream is) throws Exception {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
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

			//Toast.makeText(getActivity(), "Service Available in your city",
					//Toast.LENGTH_SHORT).show();

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

					etPincode.setText("");

				}
			});
		}

	}

	@Override
	public void onStartedExpreeCheckout() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedExpreeCheckout(String errorcode, String message) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (errorcode.equals("0")) {

			alert_dialog = new Dialog(getActivity());
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

					((HomeMainActivity) getActivity()).loadHomeFragment();
				}
			});

		}

		else {

			alert_dialog = new Dialog(getActivity());
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
