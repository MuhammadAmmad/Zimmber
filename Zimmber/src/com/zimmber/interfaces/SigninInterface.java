package com.zimmber.interfaces;

public interface SigninInterface {

	public void onStartedSignIn();

	public void onCompletedSignIn(String errorcode, String message, String email,
			String access_token);

}
