package com.zimmber.interfaces;

public interface SendFeedbackInterface {

	public void onStartedSendFeedback();

	public void onCompletedSendFeedback(String errorcode, String message);

}
