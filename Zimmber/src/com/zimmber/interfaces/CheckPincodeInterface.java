package com.zimmber.interfaces;

public interface CheckPincodeInterface {

	public void onStartedCheckPincode();

	public void onCompletedCheckPincode(String errorcode, String message);

}
