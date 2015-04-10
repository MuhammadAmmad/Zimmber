package com.zimmber.interfaces;

public interface GetFeedbackInterface {

	public void onStartedGetFeedback();

	public void onCompletedGetFeedback(String errorcode, String message,
			String status, String quality_rate, String timely_rate,
			String behaviour_rate, String cleanliness_rate,
			String value_of_money_rate, String remarks);

}
