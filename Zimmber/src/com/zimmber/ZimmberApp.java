package com.zimmber;

import java.util.HashMap;
import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.utils.Logger;
import com.zimmber.R;

public class ZimmberApp extends Application {

	private static String APP_NAMESPACE = "com.zimmber";

	public static int count = 0;

	// The following line should be changed to include the correct property id.
	private static final String PROPERTY_ID = "UA-58430267-1";

	public static int GENERAL_TRACKER = 0;

	public enum TrackerName {
		APP_TRACKER, // Tracker used only in this app.
		GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg:
						// roll-up tracking.
		ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
							// company.
	}

	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	public ZimmberApp() {
		super();
	}

	synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {

			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

			Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics
					.newTracker(R.xml.app_tracker)
					: (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics
							.newTracker(PROPERTY_ID) : analytics
							.newTracker(R.xml.ecommerce_tracker);

			mTrackers.put(trackerId, t);

		}
		return mTrackers.get(trackerId);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		// ACRA.init(this);

		Logger.DEBUG_WITH_STACKTRACE = true;

		Permission[] permissions = new Permission[] { Permission.EMAIL,
				Permission.PUBLIC_PROFILE };

		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
				.setAppId(getString(R.string.facebook_app_id))
				.setNamespace(APP_NAMESPACE).setPermissions(permissions)
				.build();

		SimpleFacebook.setConfiguration(configuration);
	}
}
