package com.zimmber.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.zimmber.R;
import com.zimmber.asynctask.ChangePasswordAsynctask;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.ChangePasswordinterface;
import com.zimmber.networkutil.Utils;

public class ChangePasswordFragment extends Fragment implements
		ChangePasswordinterface {

	View rootView;

	EditText et_password, et_new_password, et_confirm_password;
	Button btn_save_changes;

	String email = "", access_token = "", password = "", new_password = "",
			confirm_password = "";

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	SharedPreferenceClass sharedpreference;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.change_password, container, false);

		initialize();

		onclick();

		email = sharedpreference.getUserEmail();
		access_token = sharedpreference.getAccessToken();

		return rootView;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		et_password = (EditText) rootView.findViewById(R.id.et_password);
		et_new_password = (EditText) rootView
				.findViewById(R.id.et_new_password);
		et_confirm_password = (EditText) rootView
				.findViewById(R.id.et_confirm_password);
		btn_save_changes = (Button) rootView
				.findViewById(R.id.btn_save_changes);

		sharedpreference = new SharedPreferenceClass(getActivity());

	}

	private void onclick() {
		// TODO Auto-generated method stub

		btn_save_changes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				password = et_password.getText().toString();
				new_password = et_new_password.getText().toString();
				confirm_password = et_confirm_password.getText().toString();

				if (password.equals("")) {

					alert_dialog = new Dialog(getActivity());
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please Enter Password");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});

				}

				else if (new_password.equals("")) {

					alert_dialog = new Dialog(getActivity());
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please Enter New Password");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});

				}

				else if (confirm_password.equals("")) {

					alert_dialog = new Dialog(getActivity());
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please Enter Confirm Password");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});

				}

				else if (!new_password.equals(confirm_password)) {

					alert_dialog = new Dialog(getActivity());
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

				else {

					if (Utils.checkConnectivity(getActivity())) {

						ChangePasswordAsynctask change_password = new ChangePasswordAsynctask(
								getActivity());
						change_password.changepasswordintf = ChangePasswordFragment.this;
						change_password.execute(email, access_token, password,
								new_password);

						et_password.setText("");
						et_new_password.setText("");
						et_confirm_password.setText("");

					}

					else {

						showNetworkDialog("internet");
					}

				}
			}
		});

	}

	@Override
	public void onStartedChangePassword() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedChangePassword(String errorcode, String message) {
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

			alert_msg.setText("Password changed Successful");

			alert_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					alert_dialog.dismiss();

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

			alert_msg.setText("Password not Changed");

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
}