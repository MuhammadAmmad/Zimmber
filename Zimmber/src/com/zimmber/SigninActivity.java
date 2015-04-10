package com.zimmber;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.zimmber.R;
import com.zimmber.asynctask.ForgotPasswordAsynctask;
import com.zimmber.asynctask.GetProfileAsynctask;
import com.zimmber.asynctask.SigninAsynctask;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.ForgotPasswordInterface;
import com.zimmber.interfaces.GetProfileInterface;
import com.zimmber.interfaces.SigninInterface;
import com.zimmber.networkutil.GPSTracker;
import com.zimmber.networkutil.Utils;

public class SigninActivity extends Activity implements SigninInterface,
		GetProfileInterface, ForgotPasswordInterface {

	ImageView img_home, img_call;
	EditText et_email, et_password;
	TextView tv_forgot_password, tv_click;
	Button btn_signin;

	String email = "", password = "";

	private ProgressDialog pDialog;
	Dialog alert_dialog, forgot_password_dialog;

	EditText et_forgot_password;
	Button btn_send_password;

	GPSTracker gpsTracker;
	double current_lat, current_long;
	String current_latitude = "", current_longitude = "";

	String login_flag = "0";
	SharedPreferenceClass sharedpreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signin);

		((ZimmberApp) getApplication())
				.getTracker(ZimmberApp.TrackerName.APP_TRACKER);

		initialize();

		if (!sharedpreference.getLastUserEmail().equals("")) {

			et_email.setText(sharedpreference.getLastUserEmail());

		}

		current_lat = gpsTracker.getLatitude();
		current_long = gpsTracker.getLongitude();

		current_latitude = String.valueOf(current_lat);
		current_longitude = String.valueOf(current_long);

		onclick();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(SigninActivity.this).reportActivityStart(
				SigninActivity.this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(SigninActivity.this).reportActivityStop(
				SigninActivity.this);
	}

	private void initialize() {
		// TODO Auto-generated method stub

		gpsTracker = new GPSTracker(SigninActivity.this);
		sharedpreference = new SharedPreferenceClass(SigninActivity.this);

		img_home = (ImageView) findViewById(R.id.img_home);
		img_call = (ImageView) findViewById(R.id.img_call);
		et_email = (EditText) findViewById(R.id.et_email);
		et_password = (EditText) findViewById(R.id.et_password);
		tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
		tv_click = (TextView) findViewById(R.id.tv_click);
		btn_signin = (Button) findViewById(R.id.btn_signin);

	}

	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(SigninActivity.this, LandingActivity.class);
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

				Intent i = new Intent(SigninActivity.this,
						HomeMainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();

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

		tv_forgot_password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				forgot_password_dialog = new Dialog(SigninActivity.this);
				forgot_password_dialog
						.requestWindowFeature(Window.FEATURE_NO_TITLE);
				forgot_password_dialog
						.setContentView(R.layout.dialog_forgot_password);
				forgot_password_dialog.show();

				et_forgot_password = (EditText) forgot_password_dialog
						.findViewById(R.id.et_forgot_password);
				btn_send_password = (Button) forgot_password_dialog
						.findViewById(R.id.btn_send_password);

				btn_send_password.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						String forgot_password_email = et_forgot_password
								.getText().toString();

						if (forgot_password_email.equals("")) {

							Toast.makeText(SigninActivity.this,
									"Please Enter Your Email Id",
									Toast.LENGTH_SHORT).show();
						}

						else {

							if (Utils.checkConnectivity(SigninActivity.this)) {

								ForgotPasswordAsynctask forgot_password = new ForgotPasswordAsynctask(
										SigninActivity.this);
								forgot_password.forgotpasswordintf = SigninActivity.this;
								forgot_password.execute(forgot_password_email);

								et_forgot_password.setText("");

								forgot_password_dialog.dismiss();

							}

							else {

								showNetworkDialog("internet");
							}

						}

					}
				});

			}
		});

		btn_signin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				email = et_email.getText().toString();
				password = et_password.getText().toString();

				if (email.equals("")) {

					alert_dialog = new Dialog(SigninActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please Enter Your Email");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});

				}

				else if (password.equals("")) {

					alert_dialog = new Dialog(SigninActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg.setText("Please Enter Your Password");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							alert_dialog.dismiss();

						}
					});

				}

				else {

					if (Utils.checkConnectivity(SigninActivity.this)) {

						SigninAsynctask get_signin = new SigninAsynctask(
								SigninActivity.this);
						get_signin.signinintf = SigninActivity.this;
						get_signin.execute(email, password, current_latitude,
								current_longitude);

						et_email.setText("");
						et_password.setText("");

					}

					else {

						showNetworkDialog("internet");
					}

				}
			}
		});

		tv_click.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(SigninActivity.this,
						SignupActivity.class));
				finish();

			}
		});

	}

	@Override
	public void onStartedSignIn() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(SigninActivity.this);
		pDialog.setMessage("Login Checking...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedSignIn(String errorcode, String message,
			String email, String access_token) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (errorcode.equals("0")) {

			sharedpreference.saveLoginflag("1");
			sharedpreference.saveUserEmail(email);
			sharedpreference.saveAccessToken(access_token);

			if (Utils.checkConnectivity(SigninActivity.this)) {

				GetProfileAsynctask get_profile = new GetProfileAsynctask(
						SigninActivity.this);
				get_profile.getprofileintf = SigninActivity.this;
				get_profile.execute(sharedpreference.getUserEmail(),
						sharedpreference.getAccessToken());

			}

			else {

				showNetworkDialog("internet");
			}

		}

		else {

			alert_dialog = new Dialog(SigninActivity.this);
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

		pDialog = new ProgressDialog(SigninActivity.this);
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

			startActivity(new Intent(SigninActivity.this,
					HomeMainActivity.class));
			finish();

		}

		else {

			alert_dialog = new Dialog(SigninActivity.this);
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
	public void onStartedForgotPassword() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(SigninActivity.this);
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedForgotPassword(String errorcode, String message) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		alert_dialog = new Dialog(SigninActivity.this);
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

	@SuppressWarnings("deprecation")
	public void showNetworkDialog(final String message) {
		// final exit of application
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SigninActivity.this);
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
