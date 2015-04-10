package com.zimmber.interfaces;

public interface GetPriceDataInterface {

	public void onStartedGetPrice();

	public void onCompletedGetPrice(String errorcode, String price, String status);

}
