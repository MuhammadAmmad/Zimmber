package com.zimmber.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceClass {

	private static final String USER_PREFS = "ZimmberApplication";
	private SharedPreferences sharedprefs;
	private SharedPreferences.Editor prefsEditor;

	public SharedPreferenceClass(Context context) {
		this.sharedprefs = context.getSharedPreferences(USER_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = sharedprefs.edit();
	}

	public void saveLoginflag(String login_flag) {
		prefsEditor.putString("login_flag", login_flag);
		prefsEditor.commit();
	}

	public String getLoginFlag() {
		return sharedprefs.getString("login_flag", "");
	}

	public void saveUserEmail(String user_email) {
		prefsEditor.putString("user_email", user_email);
		prefsEditor.commit();
	}

	public String getUserEmail() {
		return sharedprefs.getString("user_email", "");
	}

	public void saveLastUserEmail(String last_user_email) {
		prefsEditor.putString("last_user_email", last_user_email);
		prefsEditor.commit();
	}

	public String getLastUserEmail() {
		return sharedprefs.getString("last_user_email", "");
	}

	public void saveAccessToken(String access_token) {
		prefsEditor.putString("access_token", access_token);
		prefsEditor.commit();
	}

	public String getAccessToken() {
		return sharedprefs.getString("access_token", "");
	}

	public void saveFirstName(String firstname) {
		prefsEditor.putString("firstname", firstname);
		prefsEditor.commit();
	}

	public String getFirstName() {
		return sharedprefs.getString("firstname", "");
	}

	public void saveLastName(String lastname) {
		prefsEditor.putString("lastname", lastname);
		prefsEditor.commit();
	}

	public String getLastName() {
		return sharedprefs.getString("lastname", "");
	}

	public void savePhone(String phone) {
		prefsEditor.putString("phone", phone);
		prefsEditor.commit();
	}

	public String getPhone() {
		return sharedprefs.getString("phone", "");
	}

	public void saveDOB(String dob) {
		prefsEditor.putString("dob", dob);
		prefsEditor.commit();
	}

	public String getDOB() {
		return sharedprefs.getString("dob", "");
	}

	public void saveGender(String gender) {
		prefsEditor.putString("gender", gender);
		prefsEditor.commit();
	}

	public String getGender() {
		return sharedprefs.getString("gender", "");
	}

	public void saveMaritalStatus(String marital_status) {
		prefsEditor.putString("marital_status", marital_status);
		prefsEditor.commit();
	}

	public String getMaritalStatus() {
		return sharedprefs.getString("marital_status", "");
	}
	
	public void saveAddressTitle(String address_title) {
		prefsEditor.putString("address_title", address_title);
		prefsEditor.commit();
	}

	public String getAddressTitle() {
		return sharedprefs.getString("address_title", "");
	}

	public void saveState(String state) {
		prefsEditor.putString("state", state);
		prefsEditor.commit();
	}

	public String getState() {
		return sharedprefs.getString("state", "");
	}

	public void saveCity(String city) {
		prefsEditor.putString("city", city);
		prefsEditor.commit();
	}

	public String getCity() {
		return sharedprefs.getString("city", "");
	}

	public void saveLocation(String location) {
		prefsEditor.putString("location", location);
		prefsEditor.commit();
	}

	public String getLocation() {
		return sharedprefs.getString("location", "");
	}

	public void savelandmark(String landmark) {
		prefsEditor.putString("landmark", landmark);
		prefsEditor.commit();
	}

	public String getLandmark() {
		return sharedprefs.getString("landmark", "");
	}

	public void saveStreet(String street) {
		prefsEditor.putString("street", street);
		prefsEditor.commit();
	}

	public String getStreet() {
		return sharedprefs.getString("street", "");
	}

	public void saveFlatNo(String flat_no) {
		prefsEditor.putString("flat_no", flat_no);
		prefsEditor.commit();
	}

	public String getFlatNo() {
		return sharedprefs.getString("flat_no", "");
	}

	public void saveAddress(String address) {
		prefsEditor.putString("address", address);
		prefsEditor.commit();
	}

	public String getAddress() {
		return sharedprefs.getString("address", "");
	}

	public void savePincode(String pincode) {
		prefsEditor.putString("pincode", pincode);
		prefsEditor.commit();
	}

	public String getPincode() {
		return sharedprefs.getString("pincode", "");
	}

	public void saveTotalServicePrice(String total_service_price) {
		prefsEditor.putString("total_service_price", total_service_price);
		prefsEditor.commit();
	}

	public String getTotalServicePrice() {
		return sharedprefs.getString("total_service_price", "");
	}

	public void saveBookingId(String booking_id) {
		prefsEditor.putString("booking_id", booking_id);
		prefsEditor.commit();
	}

	public String getBookingId() {
		return sharedprefs.getString("booking_id", "");
	}

	public void saveSelectServiceId(String service_id) {
		prefsEditor.putString("service_id", service_id);
		prefsEditor.commit();
	}

	public String getSelectServiceId() {
		return sharedprefs.getString("service_id", "");
	}

	public void saveSelectServiceName(String service_name) {
		prefsEditor.putString("service_name", service_name);
		prefsEditor.commit();
	}

	public String getSelectServiceName() {
		return sharedprefs.getString("service_name", "");
	}

}
