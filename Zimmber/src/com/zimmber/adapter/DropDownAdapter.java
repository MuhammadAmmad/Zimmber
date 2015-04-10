package com.zimmber.adapter;

import java.util.ArrayList;
import com.zimmber.R;
import com.zimmber.bin.DropDownItem;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DropDownAdapter extends BaseAdapter {

	private ArrayList<DropDownItem> _allItems;
	private LayoutInflater mInflater;

	public DropDownAdapter(Context context, ArrayList <DropDownItem> results) {

		_allItems = results;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return _allItems.size();
	}

	@Override
	public DropDownItem getItem(int position) {
		return _allItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		convertView = mInflater.inflate(R.layout.dropdownitem, null);
		holder = new ViewHolder();

		holder.spinnerValue = (TextView) convertView
				.findViewById(R.id.textViewDropDownItem);

		convertView.setTag(holder);
		holder = (ViewHolder) convertView.getTag();

		if (position == 0) {

			holder.spinnerValue.setTextColor((Color.parseColor("#2980b9")));
		}

		holder.spinnerValue.setText(_allItems.get(position).getName());
		return convertView;
	}

	static class ViewHolder {
		TextView spinnerValue; // spinner name
	}

	public void clearAndAddAll(ArrayList<DropDownItem> _itemToAdd) {
		_allItems.clear();
		_allItems.addAll(_itemToAdd);
		notifyDataSetChanged();

	}

 }