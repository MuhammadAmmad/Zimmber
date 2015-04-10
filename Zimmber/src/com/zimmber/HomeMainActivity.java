package com.zimmber;

import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zimmber.adapter.ActivityTermsAdapter;
import com.zimmber.asynctask.LogoutAsynctask;
import com.zimmber.bin.CheckoutItem;
import com.zimmber.database.DBAdapter;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.fragment.AboutUsFragment;
import com.zimmber.fragment.CheckoutLoginFragment;
import com.zimmber.fragment.CheckoutPayuMoneyFragment;
import com.zimmber.fragment.CheckoutProcessFragment;
import com.zimmber.fragment.CheckoutProcessUserFragment;
import com.zimmber.fragment.ExpressCheckoutFragment;
import com.zimmber.fragment.HomeMenuFragment;
import com.zimmber.fragment.HowZimmberWorksFragment;
import com.zimmber.fragment.PrivacyPolicyFragment;
import com.zimmber.fragment.ServiceFragment;
import com.zimmber.fragment.TermsofUseFragment;
import com.zimmber.interfaces.LogoutInterface;
import com.zimmber.networkutil.Utils;

public class HomeMainActivity extends SlidingFragmentActivity implements
		LogoutInterface {

	private SlidingMenu slidingMenu;
	private ImageView toggleMenu, btnHome;

	String access_token = "", email = "";

	LinearLayout ll_tap_to_view, ll_view_profile, ll_checkout, ll_login,
			ll_logout, ll_view_order, ll_express_checkout,
			ll_how_zimmber_works, ll_terms_of_use, ll_privacy_policy,
			ll_about_us;
	TextView tv_welcome;

	String home_flag = "0", view_selection = "";

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	Dialog dialog, activity_terms_dialog;
	ImageView img_dialog_checkout;
	ListView list_checkout, list_activity_terms;
	TextView tv_total_price, tv_policy;
	CheckBox chk_agree;
	Button btn_ok;

	int total_price = 0;
	String total_service_price = "0";

	ArrayList<CheckoutItem> listofcheckout;
	CheckoutItem checkoutitem;
	CheckoutAdapter checkoutadapter;
	ActivityTermsAdapter activitytermsadapter;

	SharedPreferenceClass sharedpreference;

	DBAdapter db;
	long id;
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

	static final String KEY_SERVICE_TERMS = "service_terms";

	static final String KEY_CUST_VAR1 = "cust_var1";

	static final String KEY_CUST_VAR2 = "cust_var2";

	static final String KEY_CUST_VAR3 = "cust_var3";

	static final String KEY_CUST_VAR4 = "cust_var4";

	static final String KEY_COUPON_CODE = "coupon_code";

	static final String KEY_OFFER_PRICE = "offer_price";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.homeactivity_layout);

		((ZimmberApp) getApplication())
				.getTracker(ZimmberApp.TrackerName.APP_TRACKER);

		setBehindContentView(R.layout.left_menu);
		slidingMenu = getSlidingMenu();
		slidingMenu.setFadeEnabled(true);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		initialize();

		onclick();

		loadHomeFragment();

		if (sharedpreference.getLoginFlag().equals("1")) {

			ll_login.setVisibility(View.GONE);
			ll_logout.setVisibility(View.VISIBLE);
			ll_view_profile.setVisibility(View.VISIBLE);
			ll_view_order.setVisibility(View.VISIBLE);

			tv_welcome.setText("Welcome " + sharedpreference.getFirstName()
					+ " " + sharedpreference.getLastName());

			access_token = sharedpreference.getAccessToken();
			email = sharedpreference.getUserEmail();
		}

		else {

			ll_login.setVisibility(View.VISIBLE);
			ll_logout.setVisibility(View.GONE);
			ll_view_profile.setVisibility(View.GONE);
			ll_view_order.setVisibility(View.GONE);

			tv_welcome.setText("Welcome User");

		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(HomeMainActivity.this).reportActivityStart(
				HomeMainActivity.this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(HomeMainActivity.this).reportActivityStop(
				HomeMainActivity.this);
	}

	private void initialize() {
		// TODO Auto-generated method stub

		toggleMenu = (ImageView) findViewById(R.id.toggleMenu);
		btnHome = (ImageView) findViewById(R.id.btnHome);

		ll_tap_to_view = (LinearLayout) slidingMenu
				.findViewById(R.id.ll_tap_to_view);
		ll_view_profile = (LinearLayout) slidingMenu
				.findViewById(R.id.ll_view_profile);
		ll_checkout = (LinearLayout) slidingMenu.findViewById(R.id.ll_checkout);
		ll_login = (LinearLayout) slidingMenu.findViewById(R.id.ll_login);
		ll_logout = (LinearLayout) slidingMenu.findViewById(R.id.ll_logout);
		ll_view_order = (LinearLayout) slidingMenu
				.findViewById(R.id.ll_view_order);
		ll_express_checkout = (LinearLayout) slidingMenu
				.findViewById(R.id.ll_express_checkout);
		ll_how_zimmber_works = (LinearLayout) slidingMenu
				.findViewById(R.id.ll_how_zimmber_works);
		ll_terms_of_use = (LinearLayout) slidingMenu
				.findViewById(R.id.ll_terms_of_use);
		ll_privacy_policy = (LinearLayout) slidingMenu
				.findViewById(R.id.ll_privacy_policy);
		ll_about_us = (LinearLayout) slidingMenu.findViewById(R.id.ll_about_us);

		tv_welcome = (TextView) slidingMenu.findViewById(R.id.tv_welcome);

		sharedpreference = new SharedPreferenceClass(HomeMainActivity.this);

	}

	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent keyEvent) { if (keyCode ==
	 * KeyEvent.KEYCODE_BACK) {
	 * 
	 * finish(); } return super.onKeyDown(keyCode, keyEvent); }
	 */

	private static long back_pressed;

	public void onBackPressed() {
		super.onBackPressed();
		if (back_pressed + 3000 > System.currentTimeMillis()) {
			android.os.Process.killProcess(android.os.Process.myPid());
			finish();

		} else {
			Toast.makeText(getBaseContext(), "Press once again to exit!", 1000)
					.show();
			back_pressed = System.currentTimeMillis();

			loadHomeFragment();

		}
	}

	public void onclick() {
		// TODO Auto-generated method stub

		toggleMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				slidingMenu.toggle();
			}
		});

		btnHome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (home_flag.equals("1")) {

					loadHomeFragment();

				}

			}
		});

		ll_tap_to_view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (sharedpreference.getLoginFlag().equals("1")) {

					Intent intent = (new Intent(HomeMainActivity.this,
							MyAccountActivity.class));
					intent.putExtra("view_selection", "0");
					startActivity(intent);
					finish();

				}

			}
		});

		ll_view_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = (new Intent(HomeMainActivity.this,
						MyAccountActivity.class));
				intent.putExtra("view_selection", "1");
				startActivity(intent);
				finish();

			}
		});

		ll_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(HomeMainActivity.this,
						SigninActivity.class));
				finish();

			}
		});

		ll_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				slidingMenu.toggle();

				if (Utils.checkConnectivity(HomeMainActivity.this)) {

					LogoutAsynctask get_logout = new LogoutAsynctask(
							HomeMainActivity.this);
					get_logout.logoutintf = HomeMainActivity.this;
					get_logout.execute(access_token, email);

				}

				else {

					showNetworkDialog("internet");
				}

			}
		});

		ll_checkout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				slidingMenu.toggle();

				showCheckoutDialog();

			}

		});

		ll_express_checkout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				slidingMenu.toggle();

				loadExpressCheckoutFragment();

			}
		});

		ll_how_zimmber_works.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				slidingMenu.toggle();

				loadHowZimmberWorksFragment();

			}
		});

		ll_terms_of_use.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				slidingMenu.toggle();

				loadTermsofUseFragment();

			}
		});

		ll_privacy_policy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				slidingMenu.toggle();

				loadPrivacyPolicyFragment();

			}
		});

		ll_about_us.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				slidingMenu.toggle();

				loadAboutUsFragment();

			}
		});

	}

	private void showCheckoutDialog() {
		// TODO Auto-generated method stub

		dialog = new Dialog(HomeMainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.setContentView(R.layout.dialog_review_order);

		img_dialog_checkout = (ImageView) dialog
				.findViewById(R.id.img_dialog_checkout);
		list_checkout = (ListView) dialog.findViewById(R.id.list_checkout);
		tv_total_price = (TextView) dialog.findViewById(R.id.tv_total_price);
		tv_policy = (TextView) dialog.findViewById(R.id.tv_policy);
		chk_agree = (CheckBox) dialog.findViewById(R.id.chk_agree);

		db = new DBAdapter(HomeMainActivity.this);
		db.open();

		menuItems = new ArrayList<HashMap<String, String>>();
		listofcheckout = new ArrayList<CheckoutItem>();
		total_price = 0;

		menuItems = db.getRecords();

		for (int i = 0; i < menuItems.size(); i++) {

			checkoutitem = new CheckoutItem();

			checkoutitem.setServiceId(menuItems.get(i).get(KEY_SERVICE_ID));
			checkoutitem.setServiceName(menuItems.get(i).get(KEY_SERVICE_NAME));
			checkoutitem.setVar1Id(menuItems.get(i).get(KEY_VAR1_ID));
			checkoutitem.setVar1Name(menuItems.get(i).get(KEY_VAR1_NAME));
			checkoutitem.setCustVar1(menuItems.get(i).get(KEY_CUST_VAR1));
			checkoutitem.setVar2Id(menuItems.get(i).get(KEY_VAR2_ID));
			checkoutitem.setVar2Name(menuItems.get(i).get(KEY_VAR2_NAME));
			checkoutitem.setCustVar2(menuItems.get(i).get(KEY_CUST_VAR2));
			checkoutitem.setVar3Id(menuItems.get(i).get(KEY_VAR3_ID));
			checkoutitem.setVar3Name(menuItems.get(i).get(KEY_VAR3_NAME));
			checkoutitem.setCustVar3(menuItems.get(i).get(KEY_CUST_VAR3));
			checkoutitem.setVar4Id(menuItems.get(i).get(KEY_VAR4_ID));
			checkoutitem.setVar4Name(menuItems.get(i).get(KEY_VAR4_NAME));
			checkoutitem.setCustVar4(menuItems.get(i).get(KEY_CUST_VAR4));
			checkoutitem.setActivityId(menuItems.get(i).get(KEY_ACTIVITY_ID));
			checkoutitem.setActivityName(menuItems.get(i)
					.get(KEY_ACTIVITY_NAME));
			checkoutitem.setActivityCount(menuItems.get(i).get(
					KEY_ACTIVITY_COUNT));
			checkoutitem.setServiceStatus(menuItems.get(i).get(
					KEY_SERVICE_STATUS));
			checkoutitem.setServicePrice(menuItems.get(i)
					.get(KEY_SERVICE_PRICE));
			checkoutitem.setServiceTerms(menuItems.get(i)
					.get(KEY_SERVICE_TERMS));
			checkoutitem.setCouponCode(menuItems.get(i).get(KEY_COUPON_CODE));
			checkoutitem.setOfferPrice(menuItems.get(i).get(KEY_OFFER_PRICE));

			listofcheckout.add(checkoutitem);
			Log.d("listofcheckout size", String.valueOf(listofcheckout.size()));

			total_price = total_price
					+ Integer.parseInt(menuItems.get(i).get(KEY_OFFER_PRICE));
			Log.d("total_price", String.valueOf(total_price));

		}

		db.close();

		total_service_price = String.valueOf(total_price);
		sharedpreference.saveTotalServicePrice(total_service_price);
		tv_total_price.setText("Rs. " + total_service_price);

		if (listofcheckout.size() > 0) {

			checkoutadapter = new CheckoutAdapter(HomeMainActivity.this,
					listofcheckout);
			list_checkout.setAdapter(checkoutadapter);

			dialog.show();

			list_checkout.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub

					db = new DBAdapter(HomeMainActivity.this);
					db.open();

					String checklist_id = menuItems.get(position)
							.get(KEY_CHECKOUTLIST_ID).toString();

					remove = db.deleteRecord(checklist_id);

					db.close();

					dialog.dismiss();
					showCheckoutDialog();

				}

			});

		}

		else {

			alert_dialog = new Dialog(HomeMainActivity.this);
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_alert);
			alert_dialog.show();

			TextView alert_msg = (TextView) alert_dialog
					.findViewById(R.id.alert_msg);
			Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

			alert_msg.setText("No Items in Cart");

			alert_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					alert_dialog.dismiss();

				}
			});

		}

		tv_policy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				activity_terms_dialog = new Dialog(HomeMainActivity.this);
				activity_terms_dialog
						.requestWindowFeature(Window.FEATURE_NO_TITLE);
				activity_terms_dialog.getWindow().setGravity(Gravity.CENTER);
				activity_terms_dialog
						.setContentView(R.layout.dialog_activity_terms);

				list_activity_terms = (ListView) activity_terms_dialog
						.findViewById(R.id.list_activity_terms);
				btn_ok = (Button) activity_terms_dialog
						.findViewById(R.id.btn_ok);

				activitytermsadapter = new ActivityTermsAdapter(
						HomeMainActivity.this, listofcheckout);
				list_activity_terms.setAdapter(activitytermsadapter);

				activity_terms_dialog.show();

				btn_ok.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						activity_terms_dialog.dismiss();

					}
				});

			}
		});

		img_dialog_checkout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (listofcheckout.size() > 0 && chk_agree.isChecked() == true) {

					if (sharedpreference.getLoginFlag().equals("1")) {

						dialog.dismiss();

						loadCheckoutProcessUserFragment();

					}

					else {

						dialog.dismiss();

						loadCheckoutLoginFragment();

					}

				}

				else {

					alert_dialog = new Dialog(HomeMainActivity.this);
					alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alert_dialog.setContentView(R.layout.dialog_alert);
					alert_dialog.show();

					TextView alert_msg = (TextView) alert_dialog
							.findViewById(R.id.alert_msg);
					Button alert_ok = (Button) alert_dialog
							.findViewById(R.id.alert_ok);

					alert_msg
							.setText("You have to agree Zimmber Disclaimer Policy");

					alert_ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated
							// method stub

							alert_dialog.dismiss();

						}
					});
				}

			}
		});

	}

	public void loadHomeFragment() {

		home_flag = "0";

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		Fragment fragment = new HomeMenuFragment();
		trans.replace(R.id.FramelayoutContainer, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	public void loadServiceFragment() {

		home_flag = "1";

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		Fragment fragment = new ServiceFragment();
		trans.replace(R.id.FramelayoutContainer, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	public void loadCheckoutLoginFragment() {

		home_flag = "1";

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		Fragment fragment = new CheckoutLoginFragment();
		trans.replace(R.id.FramelayoutContainer, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	public void loadCheckoutProcessFragment() {

		home_flag = "1";

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		Fragment fragment = new CheckoutProcessFragment();
		trans.replace(R.id.FramelayoutContainer, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	public void loadCheckoutProcessUserFragment() {

		home_flag = "1";

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		Fragment fragment = new CheckoutProcessUserFragment();
		trans.replace(R.id.FramelayoutContainer, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	public void loadPayuMoneyFragment() {

		home_flag = "1";

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		Fragment fragment = new CheckoutPayuMoneyFragment();
		trans.replace(R.id.FramelayoutContainer, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	public void loadExpressCheckoutFragment() {

		home_flag = "1";

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		Fragment fragment = new ExpressCheckoutFragment();
		trans.replace(R.id.FramelayoutContainer, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	public void loadHowZimmberWorksFragment() {

		home_flag = "1";

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		Fragment fragment = new HowZimmberWorksFragment();
		trans.replace(R.id.FramelayoutContainer, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	public void loadTermsofUseFragment() {

		home_flag = "1";

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		Fragment fragment = new TermsofUseFragment();
		trans.replace(R.id.FramelayoutContainer, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	public void loadPrivacyPolicyFragment() {

		home_flag = "1";

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		Fragment fragment = new PrivacyPolicyFragment();
		trans.replace(R.id.FramelayoutContainer, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	public void loadAboutUsFragment() {

		home_flag = "1";

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		Fragment fragment = new AboutUsFragment();
		trans.replace(R.id.FramelayoutContainer, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	@Override
	public void onStartedLogout() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(HomeMainActivity.this);
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedLogout(String errorcode, String message) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (errorcode.equals("0")) {

			sharedpreference.saveLoginflag("");
			sharedpreference.saveUserEmail("");
			sharedpreference.saveLastUserEmail(email);
			sharedpreference.saveAccessToken("");

			db = new DBAdapter(HomeMainActivity.this);
			db.open();

			menuItems = new ArrayList<HashMap<String, String>>();
			menuItems = db.getRecords();

			for (int i = 0; i < menuItems.size(); i++) {

				String checklist_id = menuItems.get(i).get(KEY_CHECKOUTLIST_ID)
						.toString();

				remove = db.deleteRecord(checklist_id);

			}

			db.close();

			startActivity(new Intent(getApplicationContext(),
					HomeMainActivity.class));
			finish();

		}

		else {

			alert_dialog = new Dialog(HomeMainActivity.this);
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
				HomeMainActivity.this);
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

	public class CheckoutAdapter extends ArrayAdapter<CheckoutItem> {

		private LayoutInflater inflater;
		private Context mContext;

		public CheckoutAdapter(Context context,
				ArrayList<CheckoutItem> listofcheckout) {
			// TODO Auto-generated constructor stub
			super(context, R.layout.review_order_list_row,
					R.id.tv_service_name, listofcheckout);
			this.mContext = context;
			inflater = LayoutInflater.from(context);
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			StringBuilder service_details = new StringBuilder();

			final CheckoutItem surveyList = (CheckoutItem) this
					.getItem(position);

			final ViewHolder holder;

			if (convertView == null) {

				convertView = inflater.inflate(R.layout.review_order_list_row,
						null);

				holder = new ViewHolder();

				holder.tv_service_name = (TextView) convertView
						.findViewById(R.id.tv_service_name);
				holder.tv_service_details = (TextView) convertView
						.findViewById(R.id.tv_service_details);
				holder.tv_service_price = (TextView) convertView
						.findViewById(R.id.tv_service_price);
				holder.imgView_cross = (ImageView) convertView
						.findViewById(R.id.imgView_cross);

				holder.imgView_cross
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								db = new DBAdapter(HomeMainActivity.this);
								db.open();

								String checklist_id = menuItems.get(position)
										.get(KEY_CHECKOUTLIST_ID).toString();

								remove = db.deleteRecord(checklist_id);

								db.close();

								dialog.dismiss();
								showCheckoutDialog();

							}
						});

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();

			}

			if (!surveyList.getVar1Name().equals("")) {
				service_details.append(surveyList.getVar1Name() + ", ");
			}
			if (!surveyList.getVar2Name().equals("")) {
				service_details.append(surveyList.getVar2Name() + ", ");
			}
			if (!surveyList.getVar3Name().equals("")) {
				service_details.append(surveyList.getVar3Name() + ", ");
			}
			if (!surveyList.getVar4Name().equals("")) {
				service_details.append(surveyList.getVar4Name() + ", ");
			}
			if (!surveyList.getActivityName().equals("")) {
				service_details.append(surveyList.getActivityName() + ", ");
			}

			service_details.replace(service_details.length() - 2,
					service_details.length(), "");

			/*
			 * holder.tv_service_name.setText(surveyList.getServiceStatus() +
			 * " " + surveyList.getServiceName());
			 */

			holder.tv_service_name.setText(surveyList.getServiceName());
			holder.tv_service_details.setText(service_details);
			holder.tv_service_price.setText("Rs. "
					+ surveyList.getOfferPrice());

			return convertView;
		}

		public class ViewHolder {

			TextView tv_service_name;
			TextView tv_service_details;
			TextView tv_service_price;
			ImageView imgView_cross;

		}
	}

}
