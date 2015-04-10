package com.zimmber.fragment;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.zimmber.R;
import com.zimmber.AddAddressActivity;
import com.zimmber.EditAddressActivity;
import com.zimmber.asynctask.GetAddressAsynctask;
import com.zimmber.bin.AddressItem;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.GetAddressInterface;
import com.zimmber.networkutil.Utils;

public class AddressFragment extends Fragment implements GetAddressInterface {

	View rootView;

	ListView list_my_address;
	Button btn_add_address;

	String email = "", access_token = "";

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	ArrayList<AddressItem> listofmyaddress;
	AddressAdapter adapter;

	SharedPreferenceClass sharedpreference;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.address, container, false);

		initialize();

		onclick();

		email = sharedpreference.getUserEmail();
		access_token = sharedpreference.getAccessToken();

		if (Utils.checkConnectivity(getActivity())) {

			GetAddressAsynctask get_address = new GetAddressAsynctask(
					getActivity());
			get_address.getaddressintf = AddressFragment.this;
			get_address.execute(email, access_token);

		}

		else {

			showNetworkDialog("internet");
		}

		return rootView;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		list_my_address = (ListView) rootView
				.findViewById(R.id.list_my_address);
		btn_add_address = (Button) rootView.findViewById(R.id.btn_add_address);

		listofmyaddress = new ArrayList<AddressItem>();
		sharedpreference = new SharedPreferenceClass(getActivity());

	}

	private void onclick() {
		// TODO Auto-generated method stub

		btn_add_address.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(getActivity(),
						AddAddressActivity.class));

				getActivity().finish();

			}
		});

	}

	@Override
	public void onStartedGetAddress() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedGetAddress(ArrayList<AddressItem> listofmyaddress,
			String errorcode, String message) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (listofmyaddress.size() > 0) {

			adapter = new AddressAdapter(getActivity(), listofmyaddress);
			list_my_address.setAdapter(adapter);

		}

		else {

			alert_dialog = new Dialog(getActivity());
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_alert);
			alert_dialog.show();

			TextView alert_msg = (TextView) alert_dialog
					.findViewById(R.id.alert_msg);
			Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

			alert_msg.setText("No Address Records Available");

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

	public class AddressAdapter extends ArrayAdapter<AddressItem> {

		private LayoutInflater inflater;
		private Context mContext;

		public AddressAdapter(Context context,
				ArrayList<AddressItem> listofmybuilding) {
			// TODO Auto-generated constructor stub
			super(context, R.layout.address_row, R.id.tv_state,
					listofmybuilding);
			this.mContext = context;
			inflater = LayoutInflater.from(context);
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final AddressItem surveyList = (AddressItem) this.getItem(position);

			ViewHolder holder;
			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.address_row, null);

			holder.tv_state = (TextView) convertView
					.findViewById(R.id.tv_state);
			holder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
			holder.tv_location = (TextView) convertView
					.findViewById(R.id.tv_location);
			holder.tv_landmark = (TextView) convertView
					.findViewById(R.id.tv_landmark);
			holder.tv_street = (TextView) convertView
					.findViewById(R.id.tv_street);
			holder.tv_flatno = (TextView) convertView
					.findViewById(R.id.tv_flatno);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.tv_pincode = (TextView) convertView
					.findViewById(R.id.tv_pincode);
			holder.btn_edit = (Button) convertView.findViewById(R.id.btn_edit);

			holder.btn_edit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent edit_address = new Intent(getActivity(),
							EditAddressActivity.class);

					edit_address.putExtra("address_id",
							surveyList.getAddressId());
					edit_address.putExtra("address_title",
							surveyList.getAddressTitle());
					edit_address.putExtra("state_id", surveyList.getStateId());
					edit_address.putExtra("city_id", surveyList.getCityId());
					edit_address.putExtra("location_id",
							surveyList.getLocationId());
					edit_address.putExtra("landmark", surveyList.getLandmark());
					edit_address.putExtra("street", surveyList.getStreet());
					edit_address.putExtra("flat_no", surveyList.getFlatNo());
					edit_address.putExtra("address", surveyList.getAddress());
					edit_address.putExtra("pincode", surveyList.getPincode());

					startActivity(edit_address);

					getActivity().finish();

				}
			});

			convertView.setTag(holder);
			holder = (ViewHolder) convertView.getTag();

			holder.tv_state.setText(surveyList.getStateName());
			holder.tv_city.setText(surveyList.getCityName());
			holder.tv_location.setText(surveyList.getLocationName());
			holder.tv_landmark.setText(surveyList.getLandmark());
			holder.tv_street.setText(surveyList.getStreet());
			holder.tv_flatno.setText(surveyList.getFlatNo());
			holder.tv_address.setText(surveyList.getAddress());
			holder.tv_pincode.setText(surveyList.getPincode());

			return convertView;
		}

		public class ViewHolder {

			TextView tv_state;
			TextView tv_city;
			TextView tv_location;
			TextView tv_landmark;
			TextView tv_street;
			TextView tv_flatno;
			TextView tv_address;
			TextView tv_pincode;
			Button btn_edit;

		}
	}
}
