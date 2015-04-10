package com.zimmber;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.zimmber.R;
import com.zimmber.adapter.TabsPagerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.KeyEvent;

public class MyAccountActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Profile", "Address", "Change Password", "Orders" };

	String view_selection = "";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((ZimmberApp) getApplication())
				.getTracker(ZimmberApp.TrackerName.APP_TRACKER);

		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);

		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#87d37c")));
		/*
		 * actionBar.setIcon(new ColorDrawable(getResources().getColor(
		 * android.R.color.transparent)));
		 */
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.setTitle(Html
				.fromHtml("<b><font color='#ffffff'>My Account</font></b>"));

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(MyAccountActivity.this));
		}

		if (getIntent().hasExtra("view_selection")) {

			view_selection = getIntent().getStringExtra("view_selection");

		}

		if (view_selection.equals("1")) {

			viewPager.setCurrentItem(3);
			actionBar.setSelectedNavigationItem(3);

		}

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected

				viewPager.setCurrentItem(position);
				actionBar.setSelectedNavigationItem(position);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(MyAccountActivity.this)
				.reportActivityStart(MyAccountActivity.this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(MyAccountActivity.this).reportActivityStop(
				MyAccountActivity.this);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

		viewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			startActivity(new Intent(MyAccountActivity.this,
					HomeMainActivity.class));
			finish();
		}
		return super.onKeyDown(keyCode, keyEvent);
	}

}
