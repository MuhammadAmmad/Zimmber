package com.zimmber.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.Permission.Type;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.zimmber.HomeMainActivity;
import com.zimmber.R;
import com.zimmber.asynctask.GetProfileAsynctask;
import com.zimmber.asynctask.SigninAsynctask;
import com.zimmber.asynctask.SignupAsynctask;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.GetProfileInterface;
import com.zimmber.interfaces.SigninInterface;
import com.zimmber.interfaces.SignupInterface;
import com.zimmber.networkutil.GPSTracker;
import com.zimmber.networkutil.Utils;

public class CheckoutLoginFragment extends Fragment implements SigninInterface,
		SignupInterface, GetProfileInterface, ConnectionCallbacks,
		OnConnectionFailedListener {

	View rootView;

	EditText et_email, et_password;
	LinearLayout ll_password;
	RadioGroup rdGroup;
	RadioButton rdbtnone, rdbtntwo;
	Button btn_continue;

	String account = "0", email = "", password = "";

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	String login_flag = "0";
	SharedPreferenceClass sharedpreference;

	ImageView fbLogin, GplusLogin;

	GPSTracker gpsTracker;
	double current_lat, current_long;
	String current_latitude = "", current_longitude = "";

	String firstname = "", lastname = "", phone = "";

	public SimpleFacebook simpleFB;

	private static final int RC_SIGN_IN = 0;

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;

	private boolean mSignInClicked;

	private ConnectionResult mConnectionResult;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.checkout_login, container, false);

		initialize();

		onclick();

		current_lat = gpsTracker.getLatitude();
		current_long = gpsTracker.getLongitude();

		current_latitude = String.valueOf(current_lat);
		current_longitude = String.valueOf(current_long);

		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
				.addConnectionCallbacks(CheckoutLoginFragment.this)
				.addOnConnectionFailedListener(CheckoutLoginFragment.this)
				.addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();

		return rootView;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		et_email = (EditText) rootView.findViewById(R.id.et_email);
		et_password = (EditText) rootView.findViewById(R.id.et_password);
		ll_password = (LinearLayout) rootView.findViewById(R.id.ll_password);
		rdGroup = (RadioGroup) rootView.findViewById(R.id.rdGroup);
		rdbtnone = (RadioButton) rootView.findViewById(R.id.rdbtnone);
		rdbtntwo = (RadioButton) rootView.findViewById(R.id.rdbtntwo);
		btn_continue = (Button) rootView.findViewById(R.id.btn_continue);

		fbLogin = (ImageView) rootView.findViewById(R.id.btnFacebookLogin);
		GplusLogin = (ImageView) rootView.findViewById(R.id.btnGooglePlusLogin);

		gpsTracker = new GPSTracker(getActivity());
		sharedpreference = new SharedPreferenceClass(getActivity());

	}

	private void onclick() {
		// TODO Auto-generated method stub

		rdbtnone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				account = "0";

				if (ll_password.getVisibility() == View.VISIBLE) {

					ll_password.setVisibility(View.GONE);
				}

			}
		});

		rdbtntwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				account = "1";

				if (ll_password.getVisibility() == View.GONE) {

					ll_password.setVisibility(View.VISIBLE);
				}

			}
		});

		btn_continue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				email = et_email.getText().toString();
				password = et_password.getText().toString();

				if (account.equals("1")) {

					if (email.equals("")) {

						alert_dialog = new Dialog(getActivity());
						alert_dialog
								.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

						alert_dialog = new Dialog(getActivity());
						alert_dialog
								.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

						current_lat = gpsTracker.getLatitude();
						current_long = gpsTracker.getLongitude();

						current_latitude = String.valueOf(current_lat);
						current_longitude = String.valueOf(current_long);

						if (Utils.checkConnectivity(getActivity())) {

							SigninAsynctask get_signin = new SigninAsynctask(
									getActivity());
							get_signin.signinintf = CheckoutLoginFragment.this;
							get_signin.execute(email, password,
									current_latitude, current_longitude);

						}

						else {

							showNetworkDialog("internet");
						}

					}

				}

				else {

					if (email.equals("")) {

						alert_dialog = new Dialog(getActivity());
						alert_dialog
								.requestWindowFeature(Window.FEATURE_NO_TITLE);
						alert_dialog.setContentView(R.layout.dialog_alert);
						alert_dialog.show();

						TextView alert_msg = (TextView) alert_dialog
								.findViewById(R.id.alert_msg);
						Button alert_ok = (Button) alert_dialog
								.findViewById(R.id.alert_ok);

						alert_msg.setText("Please Enter Your Usename");

						alert_ok.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								alert_dialog.dismiss();

							}
						});

					}

					else {

						sharedpreference.saveUserEmail(email);

						((HomeMainActivity) getActivity())
								.loadCheckoutProcessFragment();

					}

				}

			}

		});

		fbLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				simpleFB.login(onLoginListener);

			}
		});

		GplusLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				signInWithGplus();

			}
		});

	}

	@Override
	public void onResume() {
		AppEventsLogger.activateApp(getActivity());
		simpleFB = SimpleFacebook.getInstance(getActivity());
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(getActivity());
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		simpleFB.onActivityResult(getActivity(), requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			if (resultCode != getActivity().RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	OnLoginListener onLoginListener = new OnLoginListener() {

		@Override
		public void onFail(String reason) {
			// TODO Auto-generated method stub

			Toast.makeText(getActivity(), "Facebook Error", Toast.LENGTH_LONG)
					.show();

		}

		@Override
		public void onException(Throwable throwable) {
			// TODO Auto-generated method stub

			Toast.makeText(getActivity(), "Facebook Error", Toast.LENGTH_LONG)
					.show();

		}

		@Override
		public void onThinking() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNotAcceptingPermissions(Type type) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLogin() {
			// TODO Auto-generated method stub
			simpleFB.getProfile(new OnProfileListener() {

				public void onFail(String reason) {

					Toast.makeText(getActivity(), "Facebook Error",
							Toast.LENGTH_LONG).show();

				}

				public void onException(Throwable throwable) {

				}

				public void onThinking() {

				}

				public void onComplete(
						com.sromku.simple.fb.entities.Profile response) {

					try {

						email = response.getEmail();
						firstname = response.getFirstName();
						lastname = response.getLastName();

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					if (Utils.checkConnectivity(getActivity())) {

						SignupAsynctask get_signup = new SignupAsynctask(
								getActivity());
						get_signup.signupintf = CheckoutLoginFragment.this;
						get_signup.execute("1", firstname, lastname, phone,
								email, current_latitude, current_longitude);

					}

					else {

						showNetworkDialog("internet");
					}

				}
			});
		}
	};

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {

		mGoogleApiClient.connect();
		mSignInClicked = true;
	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(getActivity(),
						RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
					getActivity(), 0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

		mSignInClicked = false;

		// Get user's information
		getProfileInformation();

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

		mGoogleApiClient.connect();

	}

	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {

				try {

					Person currentPerson = Plus.PeopleApi
							.getCurrentPerson(mGoogleApiClient);
					firstname = currentPerson.getDisplayName();
					lastname = " ";
					email = Plus.AccountApi.getAccountName(mGoogleApiClient);

					Log.d("Name", firstname);
					Log.d("email", email);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				if (Utils.checkConnectivity(getActivity())) {

					SignupAsynctask get_signup = new SignupAsynctask(
							getActivity());
					get_signup.signupintf = CheckoutLoginFragment.this;
					get_signup.execute("1", firstname, lastname, phone, email,
							current_latitude, current_longitude);

				}

				else {

					showNetworkDialog("internet");
				}

			} else {
				Toast.makeText(getActivity(), "Person information is null",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStartedSignIn() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
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

			if (Utils.checkConnectivity(getActivity())) {

				GetProfileAsynctask get_profile = new GetProfileAsynctask(
						getActivity());
				get_profile.getprofileintf = CheckoutLoginFragment.this;
				get_profile.execute(sharedpreference.getUserEmail(),
						sharedpreference.getAccessToken());

			}

			else {

				showNetworkDialog("internet");
			}

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

					et_email.setText("");
					et_password.setText("");

				}
			});
		}

	}

	@Override
	public void onStartedSignup() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
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

			if (Utils.checkConnectivity(getActivity())) {

				GetProfileAsynctask get_profile = new GetProfileAsynctask(
						getActivity());
				get_profile.getprofileintf = CheckoutLoginFragment.this;
				get_profile.execute(sharedpreference.getUserEmail(),
						sharedpreference.getAccessToken());

			}

			else {

				showNetworkDialog("internet");
			}

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

	@Override
	public void onStartedGetProfile() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedGetProfile(String errorcode, String message,
			String firstname, String lastname, String email, String phone,
			String dob, String gender, String marital_status, String address_title, String state,
			String city, String location, String landmark, String street,
			String flat_no, String address, String pincode) {
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

			((HomeMainActivity) getActivity())
					.loadCheckoutProcessUserFragment();

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