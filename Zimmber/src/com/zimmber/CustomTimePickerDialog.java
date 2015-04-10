package com.zimmber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class CustomTimePickerDialog extends TimePickerDialog {

	private final static int TIME_PICKER_INTERVAL = 5;
	private TimePicker timePicker;
	private OnTimeSetListener callback;
	
	public CustomTimePickerDialog(Context context, OnTimeSetListener callBack,
			int hourOfDay, int minute, boolean is24HourView) {
		super(context, callBack, hourOfDay, minute, is24HourView);
		// TODO Auto-generated constructor stub
		this.callback = callBack;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		if (callback != null && timePicker != null) {
			timePicker.clearFocus();
            callback.onTimeSet(timePicker, timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
	}
	
	@SuppressLint("NewApi") @Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		try { 
			Class<?> classForid = Class.forName("com.android.internal.R$id");
	        Field timePickerField = classForid.getField("timePicker");
	        this.timePicker = (TimePicker) findViewById(timePickerField
	                .getInt(null));
	        Field field = classForid.getField("minute");
	
	        NumberPicker mMinuteSpinner = (NumberPicker) timePicker
	                .findViewById(field.getInt(null));
	        mMinuteSpinner.setMinValue(0);
	        mMinuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
	        List<String> displayedValues = new ArrayList<String>();
	        for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
	            displayedValues.add(String.format("%02d", i));
	        } 
	        mMinuteSpinner.setDisplayedValues(displayedValues
	                .toArray(new String[0]));
		} catch (Exception e) {
            e.printStackTrace();
        }      
	}
}
