package com.zimmber.interfaces;

public interface LogoutInterface {

	public void onStartedLogout();

	public void onCompletedLogout(String errorcode, String message);

}
