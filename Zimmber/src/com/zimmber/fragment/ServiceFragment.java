package com.zimmber.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import com.zimmber.Constants;
import com.zimmber.HomeMainActivity;
import com.zimmber.R;
import com.zimmber.adapter.ActivityTermsAdapter;
import com.zimmber.adapter.CheckoutAdapter;
import com.zimmber.adapter.DropDownAdapter;
import com.zimmber.asynctask.CheckDiscountAsynctask;
import com.zimmber.asynctask.GetPriceDataAsynctask;
import com.zimmber.bin.CheckoutItem;
import com.zimmber.bin.DropDownItem;
import com.zimmber.database.DBAdapter;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.CheckDiscountInterface;
import com.zimmber.interfaces.GetPriceDataInterface;
import com.zimmber.networkutil.Utils;

public class ServiceFragment extends Fragment implements
		CheckDiscountInterface, GetPriceDataInterface {

	View rootView;

	TextView tv_service_name, tv_service_price, tv_apply_coupon;
	LinearLayout ll_spinner1, ll_spinner2, ll_spinner3, ll_spinner4,
			ll_spinner5, ll_spinner6, ll_edittext1, ll_edittext2, ll_edittext3,
			ll_edittext4, ll_add_more, ll_checkout;
	Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6;
	EditText editext1, editext2, editext3, editext4, et_coupon;
	ImageView img_call;

	ArrayList<DropDownItem> _spinner1Items;
	ArrayList<DropDownItem> _spinner2Items;
	ArrayList<DropDownItem> _spinner3Items;
	ArrayList<DropDownItem> _spinner4Items;
	ArrayList<DropDownItem> _spinner5Items;
	ArrayList<DropDownItem> _spinner6Items;

	DropDownAdapter _spinner1Adapter;
	DropDownAdapter _spinner2Adapter;
	DropDownAdapter _spinner3Adapter;
	DropDownAdapter _spinner4Adapter;
	DropDownAdapter _spinner5Adapter;
	DropDownAdapter _spinner6Adapter;

	int spinner_flag1 = 0, spinner_flag2 = 0, spinner_flag3 = 0,
			spinner_flag4 = 0, spinner_flag5 = 0, spinner_flag6 = 0;

	String filePath;
	String jsonstr = null;

	String var1_id = "", var1_name = "", cust_var1 = "", var2_id = "",
			var2_name = "", cust_var2 = "", var3_id = "", var3_name = "",
			cust_var3 = "", var4_id = "", var4_name = "", cust_var4 = "",
			activity_id = "", activity_name = "", activity_count_id = "",
			service_id = "", service_name = "", service_status = "",
			service_price = "0", service_terms = "", total_service_price = "0",
			coupon_code = "", discount_type = "", discount_value = "0",
			offer_price = "0", accessToken = "";

	Dialog dialog, activity_terms_dialog;
	ImageView img_dialog_checkout;
	ListView list_checkout, list_activity_terms;
	TextView tv_total_price, tv_policy;
	CheckBox chk_agree;
	Button btn_ok;

	int total_price = 0;

	String select_option = "", apply_code_option = "";

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	ArrayList<CheckoutItem> listofcheckout;
	CheckoutItem checkoutitem;
	CheckoutAdapter checkoutadapter;
	ActivityTermsAdapter activitytermsadapter;

	DBAdapter db;
	long id;
	boolean remove;

	ArrayList<HashMap<String, String>> menuItems;

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

	SharedPreferenceClass sharedpreference;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.service, container, false);

		filePath = Environment.getExternalStorageDirectory() + File.separator
				+ getActivity().getPackageName() + File.separator
				+ Constants.JSON_SERVICE_FILE_NAME;

		initialize();

		onclick();

		loadDDL();

		service_id = sharedpreference.getSelectServiceId();
		service_name = sharedpreference.getSelectServiceName();

		tv_service_name.setText(service_name);

		return rootView;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		tv_service_name = (TextView) rootView
				.findViewById(R.id.tv_service_name);
		tv_service_price = (TextView) rootView
				.findViewById(R.id.tv_service_price);
		tv_apply_coupon = (TextView) rootView
				.findViewById(R.id.tv_apply_coupon);

		ll_spinner1 = (LinearLayout) rootView.findViewById(R.id.ll_spinner1);
		ll_spinner2 = (LinearLayout) rootView.findViewById(R.id.ll_spinner2);
		ll_spinner3 = (LinearLayout) rootView.findViewById(R.id.ll_spinner3);
		ll_spinner4 = (LinearLayout) rootView.findViewById(R.id.ll_spinner4);
		ll_spinner5 = (LinearLayout) rootView.findViewById(R.id.ll_spinner5);
		ll_spinner6 = (LinearLayout) rootView.findViewById(R.id.ll_spinner6);
		ll_edittext1 = (LinearLayout) rootView.findViewById(R.id.ll_edittext1);
		ll_edittext2 = (LinearLayout) rootView.findViewById(R.id.ll_edittext2);
		ll_edittext3 = (LinearLayout) rootView.findViewById(R.id.ll_edittext3);
		ll_edittext4 = (LinearLayout) rootView.findViewById(R.id.ll_edittext4);

		spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
		spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
		spinner3 = (Spinner) rootView.findViewById(R.id.spinner3);
		spinner4 = (Spinner) rootView.findViewById(R.id.spinner4);
		spinner5 = (Spinner) rootView.findViewById(R.id.spinner5);
		spinner6 = (Spinner) rootView.findViewById(R.id.spinner6);

		editext1 = (EditText) rootView.findViewById(R.id.editext1);
		editext2 = (EditText) rootView.findViewById(R.id.editext2);
		editext3 = (EditText) rootView.findViewById(R.id.editext3);
		editext4 = (EditText) rootView.findViewById(R.id.editext4);
		et_coupon = (EditText) rootView.findViewById(R.id.et_coupon);

		ll_add_more = (LinearLayout) rootView.findViewById(R.id.ll_add_more);
		ll_checkout = (LinearLayout) rootView.findViewById(R.id.ll_checkout);

		img_call = (ImageView) rootView.findViewById(R.id.img_call);

		sharedpreference = new SharedPreferenceClass(getActivity());

	}

	private void onclick() {
		// TODO Auto-generated method stub

		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				spinner_flag1++;

				var1_id = _spinner1Adapter.getItem(position).getStrId();
				var1_name = _spinner1Adapter.getItem(position).getName();

				if (var1_id.equals("0")) {

					var1_id = "";
					var1_name = "";
				}

				if (spinner_flag1 != 1) {

					// getPriceDataAsyntask();

				}

				if (var1_name.equalsIgnoreCase("other")) {

					var1_id = "other";
					ll_edittext1.setVisibility(View.VISIBLE);
				}

				else {

					ll_edittext1.setVisibility(View.GONE);

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				spinner_flag2++;

				var2_id = _spinner2Adapter.getItem(position).getStrId();
				var2_name = _spinner2Adapter.getItem(position).getName();

				if (var2_id.equals("0")) {

					var2_id = "";
					var2_name = "";
				}

				if (spinner_flag2 != 1) {

					// getPriceDataAsyntask();

				}

				if (var2_name.equalsIgnoreCase("other")) {

					var2_id = "other";
					ll_edittext2.setVisibility(View.VISIBLE);
				}

				else {

					ll_edittext2.setVisibility(View.GONE);

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				spinner_flag3++;

				var3_id = _spinner3Adapter.getItem(position).getStrId();
				var3_name = _spinner3Adapter.getItem(position).getName();

				if (var3_id.equals("0")) {

					var3_id = "";
					var3_name = "";
				}

				if (spinner_flag3 != 1) {

					// getPriceDataAsyntask();

				}

				if (var3_name.equalsIgnoreCase("other")) {

					var3_id = "other";
					ll_edittext3.setVisibility(View.VISIBLE);
				}

				else {

					ll_edittext3.setVisibility(View.GONE);

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner4.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				spinner_flag4++;

				var4_id = _spinner4Adapter.getItem(position).getStrId();
				var4_name = _spinner4Adapter.getItem(position).getName();

				if (var4_id.equals("0")) {

					var4_id = "";
					var4_name = "";
				}

				if (spinner_flag4 != 1) {

					// getPriceDataAsyntask();

				}

				if (var4_name.equalsIgnoreCase("other")) {

					var4_id = "other";
					ll_edittext4.setVisibility(View.VISIBLE);
				}

				else {

					ll_edittext4.setVisibility(View.GONE);

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner5.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				spinner_flag5++;

				activity_id = _spinner5Adapter.getItem(position).getStrId();
				activity_name = _spinner5Adapter.getItem(position).getName();
				service_terms = _spinner5Adapter.getItem(position)
						.getServiceTerms();

				Log.d("activity_name", activity_name);

				if (activity_id.equals("0")) {

					activity_id = "";
					activity_name = "";
					service_terms = "If your problem is not listed, there will be minimum visiting charges Rs 250 for inspection and fixing the problem within 30 mins, if possible. Additional service and material charges will be conveyed to you after inspection and we will proceed for the work only after your approval.";

				}

				if (activity_name.equalsIgnoreCase("other")) {

					activity_id = "other";

				}

				if (spinner_flag5 != 1) {

					// getPriceDataAsyntask();

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner6.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				spinner_flag6++;

				activity_count_id = _spinner6Adapter.getItem(position)
						.getStrId();

				if (activity_count_id.equals("0")) {

					activity_count_id = "1";
				}

				if (spinner_flag6 != 1) {

					// getPriceDataAsyntask();

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		/*
		 * tv_apply_coupon.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * coupon_code = et_coupon.getText().toString(); apply_code_option =
		 * "yes";
		 * 
		 * if (coupon_code.equals("")) {
		 * 
		 * Toast.makeText(getActivity(), "Please Enter Coupon code",
		 * Toast.LENGTH_SHORT).show(); }
		 * 
		 * else if (activity_id.equals("")) {
		 * 
		 * Toast.makeText(getActivity(), "Please select an Activity",
		 * Toast.LENGTH_SHORT).show(); }
		 * 
		 * else {
		 * 
		 * CheckDiscountAsynctask check_discount = new CheckDiscountAsynctask(
		 * getActivity()); check_discount.discountintf = ServiceFragment.this;
		 * check_discount .execute(service_id, activity_id, coupon_code); }
		 * 
		 * } });
		 */

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

				select_option = "checkout";

				cust_var1 = editext1.getText().toString();
				cust_var2 = editext2.getText().toString();
				cust_var3 = editext3.getText().toString();
				cust_var4 = editext4.getText().toString();
				coupon_code = et_coupon.getText().toString();

				if (coupon_code.length() > 0) {

					apply_code_option = "yes";

				} else {

					apply_code_option = "";

				}

				if (!activity_name.equals("")
						&& apply_code_option.equals("yes")) {

					CheckDiscountAsynctask check_discount = new CheckDiscountAsynctask(
							getActivity());
					check_discount.discountintf = ServiceFragment.this;
					check_discount
							.execute(service_id, activity_id, coupon_code);

				}

				else if (!activity_name.equals("")
						&& apply_code_option.equals("")) {

					getPriceDataAsyntask();
				}

				else {

					Toast.makeText(getActivity(), "Please select an Activity",
							Toast.LENGTH_SHORT).show();

				}

			}

		});

		ll_add_more.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				select_option = "add_more";
				apply_code_option = "";

				cust_var1 = editext1.getText().toString();
				cust_var2 = editext2.getText().toString();
				cust_var3 = editext3.getText().toString();
				cust_var4 = editext4.getText().toString();
				coupon_code = et_coupon.getText().toString();

				if (coupon_code.length() > 0) {

					apply_code_option = "yes";

				} else {

					apply_code_option = "";

				}

				if (!activity_name.equals("")
						&& apply_code_option.equals("yes")) {

					CheckDiscountAsynctask check_discount = new CheckDiscountAsynctask(
							getActivity());
					check_discount.discountintf = ServiceFragment.this;
					check_discount
							.execute(service_id, activity_id, coupon_code);

				}

				else if (!activity_name.equals("")
						&& apply_code_option.equals("")) {

					getPriceDataAsyntask();
				}

				else {

					Toast.makeText(getActivity(), "Please select an Activity",
							Toast.LENGTH_SHORT).show();

				}

			}
		});

	}

	private void saveCheckoutList() {
		// TODO Auto-generated method stub

		if ((!var1_id.equals("") && !var1_id.equals("undefined"))
				|| (!var2_id.equals("") && !var2_id.equals("undefined"))
				|| (!var3_id.equals("") && !var3_id.equals("undefined"))
				|| (!var4_id.equals("") && !var4_id.equals("undefined"))
				|| !activity_id.equals("")) {

			db = new DBAdapter(getActivity());
			db.open();

			id = db.insertValue(service_id, service_name, var1_id, var1_name,
					cust_var1, var2_id, var2_name, cust_var2, var3_id,
					var3_name, cust_var3, var4_id, var4_name, cust_var4,
					activity_id, activity_name, activity_count_id,
					service_status, service_price, service_terms, coupon_code,
					offer_price);

			db.close();

			loadDDL();

		}

		if (select_option.equals("checkout")) {

			showCheckoutDialog();
		}

		else {

			((HomeMainActivity) getActivity()).loadHomeFragment();

		}

	}

	private void showCheckoutDialog() {
		// TODO Auto-generated method stub

		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.setContentView(R.layout.dialog_review_order);

		img_dialog_checkout = (ImageView) dialog
				.findViewById(R.id.img_dialog_checkout);
		list_checkout = (ListView) dialog.findViewById(R.id.list_checkout);
		tv_total_price = (TextView) dialog.findViewById(R.id.tv_total_price);
		tv_policy = (TextView) dialog.findViewById(R.id.tv_policy);
		chk_agree = (CheckBox) dialog.findViewById(R.id.chk_agree);

		db = new DBAdapter(getActivity());
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

			checkoutadapter = new CheckoutAdapter(getActivity(), listofcheckout);
			list_checkout.setAdapter(checkoutadapter);

			dialog.show();

			list_checkout.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub

					db = new DBAdapter(getActivity());
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

			alert_dialog = new Dialog(getActivity());
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

				activity_terms_dialog = new Dialog(getActivity());
				activity_terms_dialog
						.requestWindowFeature(Window.FEATURE_NO_TITLE);
				activity_terms_dialog.getWindow().setGravity(Gravity.CENTER);
				activity_terms_dialog
						.setContentView(R.layout.dialog_activity_terms);

				list_activity_terms = (ListView) activity_terms_dialog
						.findViewById(R.id.list_activity_terms);
				btn_ok = (Button) activity_terms_dialog
						.findViewById(R.id.btn_ok);

				activitytermsadapter = new ActivityTermsAdapter(getActivity(),
						listofcheckout);
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

						((HomeMainActivity) getActivity())
								.loadCheckoutProcessUserFragment();

					}

					else {

						dialog.dismiss();

						((HomeMainActivity) getActivity())
								.loadCheckoutLoginFragment();

					}

				}

				else {

					alert_dialog = new Dialog(getActivity());
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

	public void loadDDL() {

		spinner_flag1 = 0;
		spinner_flag2 = 0;
		spinner_flag3 = 0;
		spinner_flag4 = 0;
		spinner_flag5 = 0;
		spinner_flag6 = 0;

		var1_id = "undefined";
		var1_name = "";
		cust_var1 = "";
		var2_id = "undefined";
		var2_name = "";
		cust_var2 = "";
		var3_id = "undefined";
		var3_name = "";
		cust_var3 = "";
		var4_id = "undefined";
		var4_name = "";
		cust_var4 = "";
		activity_id = "";
		activity_name = "";
		activity_count_id = "";
		service_status = "";
		service_price = "0";
		service_terms = "";
		coupon_code = "";
		discount_type = "";
		discount_value = "0";
		offer_price = "0";

		_spinner1Items = new ArrayList<DropDownItem>();
		_spinner1Adapter = new DropDownAdapter(getActivity(), _spinner1Items);
		spinner1.setAdapter(_spinner1Adapter);

		_spinner2Items = new ArrayList<DropDownItem>();
		_spinner2Adapter = new DropDownAdapter(getActivity(), _spinner2Items);
		spinner2.setAdapter(_spinner2Adapter);

		_spinner3Items = new ArrayList<DropDownItem>();
		_spinner3Adapter = new DropDownAdapter(getActivity(), _spinner3Items);
		spinner3.setAdapter(_spinner3Adapter);

		_spinner4Items = new ArrayList<DropDownItem>();
		_spinner4Adapter = new DropDownAdapter(getActivity(), _spinner4Items);
		spinner4.setAdapter(_spinner4Adapter);

		_spinner5Items = new ArrayList<DropDownItem>();
		_spinner5Adapter = new DropDownAdapter(getActivity(), _spinner5Items);
		spinner5.setAdapter(_spinner5Adapter);

		_spinner6Items = new ArrayList<DropDownItem>();
		_spinner6Adapter = new DropDownAdapter(getActivity(), _spinner6Items);
		spinner6.setAdapter(_spinner6Adapter);

		tv_service_price.setText("RS. 00.00");
		editext1.setText("");
		editext2.setText("");
		editext3.setText("");
		editext4.setText("");
		et_coupon.setText("");

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

					for (int i = 0; i < _servicesArray.length(); i++) {

						if (_servicesArray.getJSONObject(i).getInt("id") == Integer
								.parseInt(service_id)) {

							JSONArray _listArray = _servicesArray
									.getJSONObject(i).getJSONArray("list");

							for (int j = 0; j < _listArray.length(); j++) {

								String labelId = _listArray.getJSONObject(j)
										.getString("id");
								String labelName = _listArray.getJSONObject(j)
										.getString("dropdownName");

								if (labelName.equals("Activity")) {

									ll_spinner5.setVisibility(View.VISIBLE);
									_spinner5Items.clear();

									DropDownItem _select = new DropDownItem();
									_select.setStrId("0");
									_select.setName("Select " + labelName);

									_spinner5Items.add(_select);

									JSONArray _itemOptions = _listArray
											.getJSONObject(j).getJSONArray(
													"itemList");

									for (int k = 0; k < _itemOptions.length(); k++) {

										DropDownItem _item = new DropDownItem();
										_item.setStrId(_itemOptions
												.getJSONObject(k).getString(
														"id"));
										_item.setName(_itemOptions
												.getJSONObject(k).getString(
														"name"));
										_item.setStrServiceTerms(_itemOptions
												.getJSONObject(k).getString(
														"activityTerms"));

										_spinner5Items.add(_item);
									}

									_spinner5Adapter.notifyDataSetChanged();
								}

								else {

									if (j == 0) {

										var1_id = "";

										ll_spinner1.setVisibility(View.VISIBLE);
										_spinner1Items.clear();

										DropDownItem _select = new DropDownItem();
										_select.setStrId("0");
										_select.setName("Select " + labelName);

										_spinner1Items.add(_select);

										JSONArray _itemOptions = _listArray
												.getJSONObject(j).getJSONArray(
														"itemList");

										for (int k = 0; k < _itemOptions
												.length(); k++) {

											DropDownItem _item = new DropDownItem();
											_item.setStrId(_itemOptions
													.getJSONObject(k)
													.getString("id"));
											_item.setName(_itemOptions
													.getJSONObject(k)
													.getString("name"));

											_spinner1Items.add(_item);
										}

										_spinner1Adapter.notifyDataSetChanged();

									}

									if (j == 1) {

										var2_id = "";

										ll_spinner2.setVisibility(View.VISIBLE);
										_spinner2Items.clear();

										DropDownItem _select = new DropDownItem();
										_select.setStrId("0");
										_select.setName("Select " + labelName);

										_spinner2Items.add(_select);

										JSONArray _itemOptions = _listArray
												.getJSONObject(j).getJSONArray(
														"itemList");

										for (int k = 0; k < _itemOptions
												.length(); k++) {

											DropDownItem _item = new DropDownItem();
											_item.setStrId(_itemOptions
													.getJSONObject(k)
													.getString("id"));
											_item.setName(_itemOptions
													.getJSONObject(k)
													.getString("name"));

											_spinner2Items.add(_item);
										}

										_spinner2Adapter.notifyDataSetChanged();

									}

									if (j == 2) {

										var3_id = "";

										ll_spinner3.setVisibility(View.VISIBLE);
										_spinner3Items.clear();

										DropDownItem _select = new DropDownItem();
										_select.setStrId("0");
										_select.setName("Select " + labelName);

										_spinner3Items.add(_select);

										JSONArray _itemOptions = _listArray
												.getJSONObject(j).getJSONArray(
														"itemList");

										for (int k = 0; k < _itemOptions
												.length(); k++) {

											DropDownItem _item = new DropDownItem();

											_item.setStrId(_itemOptions
													.getJSONObject(k)
													.getString("id"));
											_item.setName(_itemOptions
													.getJSONObject(k)
													.getString("name"));

											_spinner3Items.add(_item);
										}

										_spinner3Adapter.notifyDataSetChanged();

									}

									if (j == 3) {

										var4_id = "";

										ll_spinner4.setVisibility(View.VISIBLE);
										_spinner4Items.clear();

										DropDownItem _select = new DropDownItem();
										_select.setStrId("0");
										_select.setName("Select " + labelName);

										_spinner4Items.add(_select);

										JSONArray _itemOptions = _listArray
												.getJSONObject(j).getJSONArray(
														"itemList");

										for (int k = 0; k < _itemOptions
												.length(); k++) {

											DropDownItem _item = new DropDownItem();
											_item.setStrId(_itemOptions
													.getJSONObject(k)
													.getString("id"));
											_item.setName(_itemOptions
													.getJSONObject(k)
													.getString("name"));

											_spinner4Items.add(_item);
										}

										_spinner4Adapter.notifyDataSetChanged();

									}

								}

								ll_spinner6.setVisibility(View.VISIBLE);
								_spinner6Items.clear();

								DropDownItem _select = new DropDownItem();
								_select.setStrId("0");
								_select.setName("How Many");

								_spinner6Items.add(_select);

								for (int k = 1; k < 6; k++) {

									DropDownItem _item = new DropDownItem();
									_item.setStrId(String.valueOf(k));
									_item.setName(String.valueOf(k));

									_spinner6Items.add(_item);
								}

								_spinner6Adapter.notifyDataSetChanged();

							}

						}

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

	private void getPriceDataAsyntask() {
		// TODO Auto-generated method stub

		if (Utils.checkConnectivity(getActivity())) {

			GetPriceDataAsynctask get_price_data = new GetPriceDataAsynctask(
					getActivity());
			get_price_data.pricedataintf = ServiceFragment.this;
			get_price_data.execute(accessToken, var1_id, var2_id, var3_id,
					var4_id, activity_id, activity_count_id, service_id);

		}

		else {

			showNetworkDialog("internet");
		}

	}

	@Override
	public void onStartedCheckDiscount() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedCheckDiscount(String errorcode, String message,
			String discount_type, String discount_value) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		this.discount_type = discount_type;
		Log.d("discount_type", discount_type);
		this.discount_value = discount_value;

		if (errorcode.equals("0")) {

			getPriceDataAsyntask();
		}

		else {

			alert_dialog = new Dialog(getActivity());
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_alert);
			alert_dialog.setCancelable(false);
			alert_dialog.setCanceledOnTouchOutside(false);
			alert_dialog.show();

			TextView alert_msg = (TextView) alert_dialog
					.findViewById(R.id.alert_msg);
			Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

			alert_msg.setText("Sorry!! " + message);

			alert_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					alert_dialog.dismiss();

					et_coupon.setText("");

				}
			});

		}

	}

	@Override
	public void onStartedGetPrice() {
		// TODO Auto-generated method stub

		if (!apply_code_option.equals("yes")) {

			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please Wait...");
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();

		}

	}

	@Override
	public void onCompletedGetPrice(String errorcode, String price,
			String status) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (!errorcode.equals("0")) {

			service_price = price;
			tv_service_price.setText("RS. " + service_price);
			Log.d("service_price", service_price);

			service_status = status;

		}

		if (apply_code_option.equals("yes")) {

			if (discount_type.equals("0")) {

				int actual_price = Integer.parseInt(service_price);
				int discount_price = (actual_price * Integer
						.valueOf(discount_value)) / 100;

				if (((actual_price - discount_price)) < 0) {

					offer_price = "0";
				}

				else {

					offer_price = String.valueOf(actual_price - discount_price);
				}
				Log.d("offer_price", offer_price);

				alert_dialog = new Dialog(getActivity());
				alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alert_dialog.setContentView(R.layout.dialog_alert);
				alert_dialog.show();

				TextView alert_msg = (TextView) alert_dialog
						.findViewById(R.id.alert_msg);
				Button alert_ok = (Button) alert_dialog
						.findViewById(R.id.alert_ok);

				alert_msg.setText("Service cost will be Rs. " + offer_price
						+ " after getting " + discount_value + "% discount");

				alert_ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						alert_dialog.dismiss();

						saveCheckoutList();

					}
				});

			}

			else {

				int actual_price = Integer.parseInt(service_price);
				int discount_price = Integer.valueOf(discount_value);

				if (((actual_price - discount_price)) < 0) {

					offer_price = "0";
				}

				else {

					offer_price = String.valueOf(actual_price - discount_price);
				}
				Log.d("offer_price", offer_price);

				alert_dialog = new Dialog(getActivity());
				alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alert_dialog.setContentView(R.layout.dialog_alert);
				alert_dialog.show();

				TextView alert_msg = (TextView) alert_dialog
						.findViewById(R.id.alert_msg);
				Button alert_ok = (Button) alert_dialog
						.findViewById(R.id.alert_ok);

				alert_msg.setText("Service cost will be Rs. " + offer_price
						+ " after getting discount Rs. " + discount_value);

				alert_ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						alert_dialog.dismiss();

						saveCheckoutList();

					}
				});
			}

		}

		else {

			if (Integer.valueOf(service_price) < 150) {

				service_price = "150";
				offer_price = service_price;
			}

			else {

				offer_price = service_price;
			}

			Log.d("offer_price", offer_price);

			alert_dialog = new Dialog(getActivity());
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_alert);
			alert_dialog.show();

			TextView alert_msg = (TextView) alert_dialog
					.findViewById(R.id.alert_msg);
			Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

			alert_msg.setText("Service cost will be Rs. " + offer_price);

			alert_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					alert_dialog.dismiss();

					saveCheckoutList();

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
