package com.zimmber.interfaces;

public interface CheckoutInterface {

	public void onStartedCheckout();

	public void onCompletedCheckout(String errorcode, String message, String booking_id);

}
