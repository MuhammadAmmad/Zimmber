package com.zimmber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.networkutil.Utils;
import com.zimmber.service.GetListService;

public class SplashScreenActivity extends Activity {

	SharedPreferenceClass sharedpreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);

		((ZimmberApp) getApplication())
				.getTracker(ZimmberApp.TrackerName.APP_TRACKER);

		sharedpreference = new SharedPreferenceClass(SplashScreenActivity.this);

		if (Utils.checkConnectivity(SplashScreenActivity.this)) {

			startService(new Intent(SplashScreenActivity.this,
					GetListService.class));

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					if (sharedpreference.getLoginFlag().equals("1")) {

						startActivity(new Intent(SplashScreenActivity.this,
								HomeMainActivity.class));
						finish();
					}

					else {

						startActivity(new Intent(SplashScreenActivity.this,
								LandingActivity.class));
						finish();

					}

				}
			}, 3000);

		}

		else {

			showNetworkDialog("internet");

		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(SplashScreenActivity.this)
				.reportActivityStart(SplashScreenActivity.this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(SplashScreenActivity.this)
				.reportActivityStop(SplashScreenActivity.this);
	}

	@SuppressWarnings("deprecation")
	public void showNetworkDialog(final String message) {
		// final exit of application
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SplashScreenActivity.this);
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