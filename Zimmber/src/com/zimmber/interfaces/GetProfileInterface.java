package com.zimmber.interfaces;

public interface GetProfileInterface {

	public void onStartedGetProfile();

	public void onCompletedGetProfile(String errorcode, String message, String firstname,
			String lastname, String email, String phone, String dob,
			String gender, String marital_status, String address_title, String state,
			String city, String location, String landmark, String street,
			String flat_no, String address, String pincode);

}
