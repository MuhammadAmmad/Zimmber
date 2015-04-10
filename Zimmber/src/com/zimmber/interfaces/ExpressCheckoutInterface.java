package com.zimmber.interfaces;

public interface ExpressCheckoutInterface {

	public void onStartedExpreeCheckout();

	public void onCompletedExpreeCheckout(String errorcode, String message);

}
