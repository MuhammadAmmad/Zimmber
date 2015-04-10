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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.zimmber.R;
import com.zimmber.adapter.OrderDetailsAdapter;
import com.zimmber.asynctask.GetFeedbackAsynctask;
import com.zimmber.asynctask.GetOrderAsynctask;
import com.zimmber.asynctask.GetOrderDetailsAsynctask;
import com.zimmber.asynctask.SendFeedbackAsynctask;
import com.zimmber.bin.OrderDetailsItem;
import com.zimmber.bin.OrderItem;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.GetFeedbackInterface;
import com.zimmber.interfaces.GetOrderDetailsInterface;
import com.zimmber.interfaces.GetOrderInterface;
import com.zimmber.interfaces.SendFeedbackInterface;
import com.zimmber.networkutil.Utils;

public class OrderFragment extends Fragment implements GetOrderInterface,
		GetOrderDetailsInterface, GetFeedbackInterface, SendFeedbackInterface {

	View rootView;

	ListView list_my_order, list_order_details;

	String email = "", access_token = "";

	String booking_id = "";

	private ProgressDialog pDialog;
	Dialog alert_dialog, order_details_dialog, feedback_dialog;

	ArrayList<OrderItem> listofmyorder;
	ArrayList<OrderDetailsItem> listoforderdetails;

	OrderAdapter adapter;
	OrderDetailsAdapter order_details_adapter;

	RatingBar rating_quality, rating_timely, rating_behaviour,
			rating_cleanliness, rating_value_of_money;
	EditText et_remarks;
	LinearLayout ll_send_feedback;
	Button btn_send_feedback;

	SharedPreferenceClass sharedpreference;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.order, container, false);

		initialize();

		onclick();

		email = sharedpreference.getUserEmail();
		access_token = sharedpreference.getAccessToken();

		if (Utils.checkConnectivity(getActivity())) {

			GetOrderAsynctask get_order = new GetOrderAsynctask(getActivity());
			get_order.getorderintf = OrderFragment.this;
			get_order.execute(email, access_token);

		}

		else {

			showNetworkDialog("internet");
		}

		return rootView;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		list_my_order = (ListView) rootView.findViewById(R.id.list_my_order);

		listofmyorder = new ArrayList<OrderItem>();
		listoforderdetails = new ArrayList<OrderDetailsItem>();
		sharedpreference = new SharedPreferenceClass(getActivity());

	}

	private void onclick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartedGetOrder() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedGetOrder(ArrayList<OrderItem> listofmyorder,
			String errorcode, String message) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (listofmyorder.size() > 0) {

			adapter = new OrderAdapter(getActivity(), listofmyorder);
			list_my_order.setAdapter(adapter);

		}

		else {

			alert_dialog = new Dialog(getActivity());
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_alert);
			alert_dialog.show();

			TextView alert_msg = (TextView) alert_dialog
					.findViewById(R.id.alert_msg);
			Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

			alert_msg.setText("No Order Records Available");

			alert_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					alert_dialog.dismiss();

				}
			});
		}

	}

	public class OrderAdapter extends ArrayAdapter<OrderItem> {

		private LayoutInflater inflater;
		private Context mContext;

		public OrderAdapter(Context context, ArrayList<OrderItem> listofmyorder) {
			// TODO Auto-generated constructor stub
			super(context, R.layout.order_row, R.id.tv_booking_id,
					listofmyorder);
			this.mContext = context;
			inflater = LayoutInflater.from(context);
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final OrderItem surveyList = (OrderItem) this.getItem(position);

			ViewHolder holder;
			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.order_row, null);

			holder.tv_booking_id = (TextView) convertView
					.findViewById(R.id.tv_booking_id);
			holder.tv_total_price = (TextView) convertView
					.findViewById(R.id.tv_total_price);
			holder.tv_scheduled_timing = (TextView) convertView
					.findViewById(R.id.tv_scheduled_timing);
			holder.tv_booking_timing = (TextView) convertView
					.findViewById(R.id.tv_booking_timing);
			holder.btn_details = (Button) convertView
					.findViewById(R.id.btn_details);
			holder.btn_feedback = (Button) convertView
					.findViewById(R.id.btn_feedback);

			holder.btn_details.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					booking_id = surveyList.getBookingId();

					showOrderDetails();

				}

			});

			holder.btn_feedback.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					booking_id = surveyList.getBookingId();

					showFeedbackForm();

				}

			});

			convertView.setTag(holder);
			holder = (ViewHolder) convertView.getTag();

			holder.tv_booking_id.setText(surveyList.getBookingId());
			holder.tv_total_price.setText("Rs. " + surveyList.getTotalPrice());
			holder.tv_scheduled_timing.setText(surveyList.getServiceDate()
					+ " " + surveyList.getServiceTime());
			holder.tv_booking_timing.setText(surveyList.getBookingTime());

			return convertView;
		}

		public class ViewHolder {

			TextView tv_booking_id;
			TextView tv_total_price;
			TextView tv_scheduled_timing;
			TextView tv_booking_timing;
			Button btn_details;
			Button btn_feedback;

		}
	}

	private void showOrderDetails() {
		// TODO Auto-generated method stub

		order_details_dialog = new Dialog(getActivity());
		order_details_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		order_details_dialog.setContentView(R.layout.order_details);

		list_order_details = (ListView) order_details_dialog
				.findViewById(R.id.list_order_details);

		if (Utils.checkConnectivity(getActivity())) {

			GetOrderDetailsAsynctask get_order_details = new GetOrderDetailsAsynctask(
					getActivity());
			get_order_details.getorderdetailsintf = OrderFragment.this;
			get_order_details.execute(email, access_token, booking_id);

		}

		else {

			showNetworkDialog("internet");
		}

	}

	@Override
	public void onStartedOrderDetails() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedOrderDetails(
			ArrayList<OrderDetailsItem> listoforderdetails, String errorcode,
			String message) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (listoforderdetails.size() > 0) {

			order_details_adapter = new OrderDetailsAdapter(getActivity(),
					listoforderdetails);
			list_order_details.setAdapter(order_details_adapter);

			order_details_dialog.show();

		}

		else {

			alert_dialog = new Dialog(getActivity());
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_alert);
			alert_dialog.show();

			TextView alert_msg = (TextView) alert_dialog
					.findViewById(R.id.alert_msg);
			Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

			alert_msg.setText("No Records Available");

			alert_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					alert_dialog.dismiss();

				}
			});
		}

	}

	private void showFeedbackForm() {
		// TODO Auto-generated method stub

		feedback_dialog = new Dialog(getActivity());
		feedback_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		feedback_dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		feedback_dialog.setContentView(R.layout.dialog_order_feedback);

		rating_quality = (RatingBar) feedback_dialog
				.findViewById(R.id.rating_quality);
		rating_timely = (RatingBar) feedback_dialog
				.findViewById(R.id.rating_timely);
		rating_behaviour = (RatingBar) feedback_dialog
				.findViewById(R.id.rating_behaviour);
		rating_cleanliness = (RatingBar) feedback_dialog
				.findViewById(R.id.rating_cleanliness);
		rating_value_of_money = (RatingBar) feedback_dialog
				.findViewById(R.id.rating_value_of_money);
		et_remarks = (EditText) feedback_dialog.findViewById(R.id.et_remarks);
		ll_send_feedback = (LinearLayout) feedback_dialog
				.findViewById(R.id.ll_send_feedback);
		btn_send_feedback = (Button) feedback_dialog
				.findViewById(R.id.btn_send_feedback);

		if (Utils.checkConnectivity(getActivity())) {

			GetFeedbackAsynctask get_feedback = new GetFeedbackAsynctask(
					getActivity());
			get_feedback.getfeedbackintf = OrderFragment.this;
			get_feedback.execute(access_token, email, booking_id);

			feedback_dialog.dismiss();

		}

		else {

			showNetworkDialog("internet");
		}

		btn_send_feedback.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String quality_rate = String.valueOf(rating_quality.getRating());
				String timely_rate = String.valueOf(rating_timely.getRating());
				String behaviour_rate = String.valueOf(rating_behaviour
						.getRating());
				String cleanliness_rate = String.valueOf(rating_cleanliness
						.getRating());
				String value_of_money_rate = String
						.valueOf(rating_value_of_money.getRating());
				String remarks = et_remarks.getText().toString();

				if (Utils.checkConnectivity(getActivity())) {

					SendFeedbackAsynctask send_feedback = new SendFeedbackAsynctask(
							getActivity());
					send_feedback.sendfeedbackintf = OrderFragment.this;
					send_feedback.execute(access_token, email, booking_id,
							quality_rate, timely_rate, behaviour_rate,
							cleanliness_rate, value_of_money_rate, remarks);

					feedback_dialog.dismiss();

				}

				else {

					showNetworkDialog("internet");
				}

			}
		});

	}

	@Override
	public void onStartedGetFeedback() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedGetFeedback(String errorcode, String message,
			String status, String quality_rate, String timely_rate,
			String behaviour_rate, String cleanliness_rate,
			String value_of_money_rate, String remarks) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (status.equals("1")) {

			ll_send_feedback.setVisibility(View.GONE);

			rating_quality.setRating(Float.parseFloat(quality_rate));

			rating_timely.setRating(Float.parseFloat(timely_rate));

			rating_behaviour.setRating(Float.parseFloat(behaviour_rate));

			rating_cleanliness.setRating(Float.parseFloat(cleanliness_rate));

			rating_value_of_money.setRating(Float
					.parseFloat(value_of_money_rate));

			et_remarks.setText(remarks);

			et_remarks.setClickable(false);
			et_remarks.setFocusable(false);
		}

		else {

			ll_send_feedback.setVisibility(View.VISIBLE);

		}

		feedback_dialog.show();

	}

	@Override
	public void onStartedSendFeedback() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

	}

	@Override
	public void onCompletedSendFeedback(String errorcode, String message) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		alert_dialog = new Dialog(getActivity());
		alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alert_dialog.setContentView(R.layout.dialog_alert);
		alert_dialog.show();

		TextView alert_msg = (TextView) alert_dialog
				.findViewById(R.id.alert_msg);
		Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

		alert_msg
				.setText("Thanks for your response. Unlike others, we do care about it");

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