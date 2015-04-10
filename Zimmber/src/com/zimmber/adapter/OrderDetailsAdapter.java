package com.zimmber.adapter;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.zimmber.R;
import com.zimmber.bin.OrderDetailsItem;

public class OrderDetailsAdapter extends ArrayAdapter<OrderDetailsItem> {

	private LayoutInflater inflater;
	private Context mContext;

	public OrderDetailsAdapter(Context context,
			ArrayList<OrderDetailsItem> listoforderdetails) {
		// TODO Auto-generated constructor stub
		super(context, R.layout.order_details_row, R.id.tv_service_name,
				listoforderdetails);
		this.mContext = context;
		inflater = LayoutInflater.from(context);
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		StringBuilder service_details = new StringBuilder();
		int total_price = 0, count = 0, unit_price = 0;

		final OrderDetailsItem surveyList = (OrderDetailsItem) this
				.getItem(position);

		ViewHolder holder;
		holder = new ViewHolder();

		convertView = inflater.inflate(R.layout.order_details_row, null);

		holder.tv_service = (TextView) convertView
				.findViewById(R.id.tv_service);
		holder.tv_service_details = (TextView) convertView
				.findViewById(R.id.tv_service_details);
		holder.tv_activity_count = (TextView) convertView
				.findViewById(R.id.tv_activity_count);
		holder.tv_unit_price = (TextView) convertView
				.findViewById(R.id.tv_unit_price);
		holder.tv_total_price = (TextView) convertView
				.findViewById(R.id.tv_total_price);

		convertView.setTag(holder);
		holder = (ViewHolder) convertView.getTag();

		if (!surveyList.getVar1Name().equals("")
				&& !surveyList.getVar1Name().equals("null")) {
			service_details.append(surveyList.getVar1Name() + ", ");
		}
		if (!surveyList.getVar2Name().equals("")
				&& !surveyList.getVar2Name().equals("null")) {
			service_details.append(surveyList.getVar2Name() + ", ");
		}
		if (!surveyList.getVar3Name().equals("")
				&& !surveyList.getVar3Name().equals("null")) {
			service_details.append(surveyList.getVar3Name() + ", ");
		}
		if (!surveyList.getVar4Name().equals("")
				&& !surveyList.getVar4Name().equals("null")) {
			service_details.append(surveyList.getVar4Name() + ", ");
		}
		if (!surveyList.getActivityName().equals("")
				&& !surveyList.getActivityName().equals("null")) {
			service_details.append(surveyList.getActivityName() + ", ");
		}
		
		service_details.replace(service_details.length() - 2,
				service_details.length(), "");

		holder.tv_service.setText(surveyList.getServiceName());
		holder.tv_service_details.setText(service_details);
		holder.tv_activity_count.setText(surveyList.getActivityCount());

		/*
		 * if (!surveyList.getActivityPrice().equals("") &&
		 * !surveyList.getActivityPrice().equals("null")) {
		 * 
		 * unit_price = Integer.parseInt(surveyList.getActivityPrice());
		 * holder.tv_unit_price.setText("Rs. " + unit_price);
		 * 
		 * }
		 * 
		 * int count = Integer.parseInt(surveyList.getActivityCount()); int
		 * total_price = unit_price * count;
		 */

		if (!surveyList.getActivityPrice().equals("")
				&& !surveyList.getActivityPrice().equals("null")) {

			total_price = Integer.parseInt(surveyList.getActivityPrice());
			holder.tv_total_price.setText("Rs. " + String.valueOf(total_price));

		}

		count = Integer.parseInt(surveyList.getActivityCount());
		unit_price = total_price / count;

		holder.tv_unit_price.setText("Rs. " + unit_price);

		return convertView;
	}

	public class ViewHolder {

		TextView tv_service;
		TextView tv_service_details;
		TextView tv_activity_count;
		TextView tv_unit_price;
		TextView tv_total_price;

	}
}
