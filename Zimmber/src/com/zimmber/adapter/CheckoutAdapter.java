package com.zimmber.adapter;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zimmber.R;
import com.zimmber.bin.CheckoutItem;

public class CheckoutAdapter extends ArrayAdapter<CheckoutItem> {

	private LayoutInflater inflater;
	private Context mContext;

	public CheckoutAdapter(Context context,
			ArrayList<CheckoutItem> listofcheckout) {
		// TODO Auto-generated constructor stub
		super(context, R.layout.review_order_list_row, R.id.tv_service_name,
				listofcheckout);
		this.mContext = context;
		inflater = LayoutInflater.from(context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		StringBuilder service_details = new StringBuilder();

		final CheckoutItem surveyList = (CheckoutItem) this.getItem(position);

		final ViewHolder holder;

		if (convertView == null) {

			convertView = inflater
					.inflate(R.layout.review_order_list_row, null);

			holder = new ViewHolder();

			holder.tv_service_name = (TextView) convertView
					.findViewById(R.id.tv_service_name);
			holder.tv_service_details = (TextView) convertView
					.findViewById(R.id.tv_service_details);
			holder.tv_service_price = (TextView) convertView
					.findViewById(R.id.tv_service_price);
			holder.imgView_cross = (ImageView) convertView
					.findViewById(R.id.imgView_cross);

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
				service_details.length() - 1, "");

		/*
		 * holder.tv_service_name.setText(surveyList.getServiceStatus() + " " +
		 * surveyList.getServiceName());
		 */

		holder.tv_service_name.setText(surveyList.getServiceName());
		holder.tv_service_details.setText(service_details);
		holder.tv_service_price.setText("Rs. " + surveyList.getOfferPrice());

		return convertView;
	}

	public class ViewHolder {

		TextView tv_service_name;
		TextView tv_service_details;
		TextView tv_service_price;
		ImageView imgView_cross;

	}
}
