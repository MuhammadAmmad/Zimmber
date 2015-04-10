package com.zimmber.interfaces;

public interface SignupInterface {

	public void onStartedSignup();

	public void onCompletedSignup(String errorcode, String message, String email,
			String access_token);

}
