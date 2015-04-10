package com.zimmber.bin;

public class OrderItem {

	private String booking_id = "";
	private String total_price = "";
	private String booking_time = "";
	private String service_date = "";
	private String service_time = "";
	private String status = "";

	public String getBookingId() {
		return booking_id;
	}

	public void setBookingId(String booking_id) {
		this.booking_id = booking_id;
	}

	public String getTotalPrice() {
		return total_price;
	}

	public void setTotalPrice(String total_price) {
		this.total_price = total_price;
	}

	public String getBookingTime() {
		return booking_time;
	}

	public void setBookingTime(String booking_time) {
		this.booking_time = booking_time;
	}

	public String getServiceDate() {
		return service_date;
	}

	public void setServiceDate(String service_date) {
		this.service_date = service_date;
	}

	public String getServiceTime() {
		return service_time;
	}

	public void setServiceTime(String service_time) {
		this.service_time = service_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
