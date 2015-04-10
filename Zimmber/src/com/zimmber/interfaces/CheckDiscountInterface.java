package com.zimmber.interfaces;

public interface CheckDiscountInterface {

	public void onStartedCheckDiscount();

	public void onCompletedCheckDiscount(String errorcode, String message,
			String discount_type, String discount_value);

}
