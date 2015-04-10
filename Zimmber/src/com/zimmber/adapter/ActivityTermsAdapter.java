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
import com.zimmber.bin.CheckoutItem;

public class ActivityTermsAdapter extends ArrayAdapter<CheckoutItem> {

	private LayoutInflater inflater;
	private Context mContext;

	public ActivityTermsAdapter(Context context,
			ArrayList<CheckoutItem> listofcheckout) {
		// TODO Auto-generated constructor stub
		super(context, R.layout.activity_terms_row, R.id.tv_service_name,
				listofcheckout);
		this.mContext = context;
		inflater = LayoutInflater.from(context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final CheckoutItem surveyList = (CheckoutItem) this.getItem(position);

		final ViewHolder holder;

		if (convertView == null) {

			convertView = inflater.inflate(R.layout.activity_terms_row, null);

			holder = new ViewHolder();

			holder.tv_service_name = (TextView) convertView
					.findViewById(R.id.tv_service_name);
			holder.tv_service_terms = (TextView) convertView
					.findViewById(R.id.tv_service_terms);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}

		holder.tv_service_name.setText(surveyList.getServiceName());
		holder.tv_service_terms.setText(surveyList.getServiceTerms());

		return convertView;
	}

	public class ViewHolder {

		TextView tv_service_name;
		TextView tv_service_terms;

	}
}
