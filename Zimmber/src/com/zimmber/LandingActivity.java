package com.zimmber;

import java.util.Vector;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.sromku.simple.fb.Permission.Type;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.zimmber.adapter.MediaPagerAdapter;
import com.zimmber.asynctask.GetProfileAsynctask;
import com.zimmber.asynctask.SignupAsynctask;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.GetProfileInterface;
import com.zimmber.interfaces.SignupInterface;
import com.zimmber.networkutil.GPSTracker;
import com.zimmber.networkutil.Utils;

@SuppressLint("InflateParams")
public class LandingActivity extends Activity implements SignupInterface,
		GetProfileInterface, ConnectionCallbacks, OnConnectionFailedListener {

	ViewPager pagerContainer;
	LayoutInflater _layoutInflater;

	TextView tv_skip;
	ImageView fbLogin, GplusLogin, img_home1, img_home2, img_home3;
	RelativeLayout rl_signinsignup;
	LinearLayout ll_signup, ll_signin;

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	GPSTracker gpsTracker;
	double current_lat, current_long;
	String current_latitude = "", current_longitude = "";

	String email = "", firstname = "", lastname = "", phone = "";

	SharedPreferenceClass sharedpreference;

	// DBAdapter db;
	// boolean remove;

	// ArrayList<HashMap<String, String>> menuItems = new
	// ArrayList<HashMap<String, String>>();

	// static final String KEY_CHECKOUTLIST_ID = "id";

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

	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.landing_layout);

		((ZimmberApp) getApplication())
				.getTracker(ZimmberApp.TrackerName.APP_TRACKER);

		initialize();

		onclick();

		current_lat = gpsTracker.getLatitude();
		current_long = gpsTracker.getLongitude();

		current_latitude = String.valueOf(current_lat);
		current_longitude = String.valueOf(current_long);

		mGoogleApiClient = new GoogleApiClient.Builder(LandingActivity.this)
				.addConnectionCallbacks(LandingActivity.this)
				.addOnConnectionFailedListener(LandingActivity.this)
				.addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();

		/*
		 * db = new DBAdapter(LandingActivity.this); db.open();
		 * 
		 * menuItems = new ArrayList<HashMap<String, String>>(); menuItems =
		 * db.getRecords();
		 * 
		 * for (int i = 0; i < menuItems.size(); i++) {
		 * 
		 * String checklist_id = menuItems.get(i).get(KEY_CHECKOUTLIST_ID)
		 * .toString();
		 * 
		 * remove = db.deleteRecord(checklist_id);
		 * 
		 * }
		 * 
		 * db.close();
		 */

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(LandingActivity.this).reportActivityStart(
				LandingActivity.this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(LandingActivity.this).reportActivityStop(
				LandingActivity.this);
	}

	private void initialize() {
		// TODO Auto-generated method stub

		sharedpreference = new SharedPreferenceClass(LandingActivity.this);
		gpsTracker = new GPSTracker(LandingActivity.this);

		pagerContainer = (ViewPager) findViewById(R.id.pagercontainer);
		tv_skip = (TextView) findViewById(R.id.tv_skip);
		fbLogin = (ImageView) findViewById(R.id.btnFacebookLogin);
		GplusLogin = (ImageView) findViewById(R.id.btnGooglePlusLogin);
		rl_signinsignup = (RelativeLayout) findViewById(R.id.rl_signinsignup);
		ll_signup = (LinearLayout) findViewById(R.id.ll_signup);
		ll_signin = (LinearLayout) findViewById(R.id.ll_signin);

		_layoutInflater = getLayoutInflater();
		Vector<View> pages = new Vector<View>();

		View page1 = _layoutInflater.inflate(R.layout.page_one, null);
		View page2 = _layoutInflater.inflate(R.layout.page_two, null);
		View page3 = _layoutInflater.inflate(R.layout.page_three, null);

		pages.add(page1);
		pages.add(page2);
		pages.add(page3);

		MediaPagerAdapter adapter = new MediaPagerAdapter(this, pages);
		pagerContainer.setAdapter(adapter);

		img_home1 = (ImageView) page1.findViewById(R.id.img_home1);
		img_home2 = (ImageView) page2.findViewById(R.id.img_home2);
		img_home3 = (ImageView) page3.findViewById(R.id.img_home3);

		pagerContainer.setPageTransformer(false,
				new ViewPager.PageTransformer() {
					@Override
					public void transformPage(View page, float position) {
						// do transformation here

						final float normalizedposition = Math.abs(Math
								.abs(position) - 1);
						page.setAlpha(normalizedposition);
						// page.setScaleX(normalizedposition / 2 + 0.5f);
						// page.setScaleY(normalizedposition / 2 + 0.5f);

					}
				});

		pagerContainer.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pagenumber) {
				// TODO Auto-generated method stub

				if (pagenumber == 3) {

					fbLogin.setVisibility(View.GONE);
					GplusLogin.setVisibility(View.GONE);
					rl_signinsignup.setVisibility(View.GONE);

				} else {

					fbLogin.setVisibility(View.VISIBLE);
					GplusLogin.setVisibility(View.VISIBLE);
					rl_signinsignup.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void onclick() {
		// TODO Auto-generated method stub

		img_home1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(LandingActivity.this,
						HomeMainActivity.class));

				finish();

			}
		});

		img_home2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(LandingActivity.this,
						HomeMainActivity.class));

				finish();

			}
		});

		img_home3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(LandingActivity.this,
						HomeMainActivity.class));

				finish();

			}
		});

		tv_skip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(LandingActivity.this,
						HomeMainActivity.class));

				finish();

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

		ll_signin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(LandingActivity.this,
						SigninActivity.class));

				finish();

			}
		});

		ll_signup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(LandingActivity.this,
						SignupActivity.class));

				finish();

			}
		});

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onResume() {
		AppEventsLogger.activateApp(this);
		simpleFB = SimpleFacebook.getInstance(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		simpleFB.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
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

			Toast.makeText(LandingActivity.this, "Facebook Error",
					Toast.LENGTH_LONG).show();

		}

		@Override
		public void onException(Throwable throwable) {
			// TODO Auto-generated method stub

			Toast.makeText(LandingActivity.this, "Facebook Error",
					Toast.LENGTH_LONG).show();

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

					Toast.makeText(LandingActivity.this, "Facebook Error",
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

					if (Utils.checkConnectivity(LandingActivity.this)) {

						SignupAsynctask get_signup = new SignupAsynctask(
								LandingActivity.this);
						get_signup.signupintf = LandingActivity.this;
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
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
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
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
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
					lastname = "";
					email = Plus.AccountApi.getAccountName(mGoogleApiClient);

					String[] separated = firstname.split(" ");
					firstname = separated[0];
					lastname = separated[1];

					Log.d("firstname", firstname);
					Log.d("lastname", lastname);
					Log.d("email", email);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				if (Utils.checkConnectivity(LandingActivity.this)) {

					SignupAsynctask get_signup = new SignupAsynctask(
							LandingActivity.this);
					get_signup.signupintf = LandingActivity.this;
					get_signup.execute("1", firstname, lastname, phone, email,
							current_latitude, current_longitude);

				}

				else {

					showNetworkDialog("internet");
				}

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStartedSignup() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(LandingActivity.this);
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

			if (Utils.checkConnectivity(LandingActivity.this)) {

				GetProfileAsynctask get_profile = new GetProfileAsynctask(
						LandingActivity.this);
				get_profile.getprofileintf = LandingActivity.this;
				get_profile.execute(sharedpreference.getUserEmail(),
						sharedpreference.getAccessToken());

			}

			else {

				showNetworkDialog("internet");
			}

		}

		else {

			alert_dialog = new Dialog(LandingActivity.this);
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

		pDialog = new ProgressDialog(LandingActivity.this);
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

			startActivity(new Intent(LandingActivity.this,
					HomeMainActivity.class));
			finish();

		}

		else {

			alert_dialog = new Dialog(LandingActivity.this);
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
				LandingActivity.this);
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
