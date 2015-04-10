package com.zimmber.interfaces;

public interface ForgotPasswordInterface {
	
	public void onStartedForgotPassword();

	public void onCompletedForgotPassword(String errorcode, String message);

}
