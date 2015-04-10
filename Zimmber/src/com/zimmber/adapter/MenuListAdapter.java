package com.zimmber.adapter;

import java.util.ArrayList;
import com.zimmber.R;
import com.zimmber.bin.MenuListItem;
import com.zimmber.networkutil.ImageLoader;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuListAdapter extends ArrayAdapter<MenuListItem> {

	private LayoutInflater inflater;
	private Context mContext;
	private ImageLoader imageloader;

	public MenuListAdapter(Context context, ArrayList<MenuListItem> listofmenu) {
		// TODO Auto-generated constructor stub
		super(context, R.layout.menu_row, R.id.service_name, listofmenu);
		this.mContext = context;
		inflater = LayoutInflater.from(context);
		imageloader = new ImageLoader(context);
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final MenuListItem surveyList = (MenuListItem) this.getItem(position);

		ViewHolder holder;
		holder = new ViewHolder();

		convertView = inflater.inflate(R.layout.menu_row, null);
		
		holder.service_name = (TextView) convertView
				.findViewById(R.id.service_name);
		holder.service_image = (ImageView) convertView
				.findViewById(R.id.service_image);
		
		convertView.setTag(holder);
		holder = (ViewHolder) convertView.getTag();

		holder.service_name.setText(surveyList.getServiceName());

		// photo_image.setPadding(5, 5, 5, 5);
		//holder.service_image.setScaleType(ImageView.ScaleType.FIT_XY);
		imageloader
				.DisplayImage(surveyList.getServiceIcon(), holder.service_image);

		return convertView;
	}

	public class ViewHolder {

		private TextView service_name;
		private ImageView service_image;

	}

}
