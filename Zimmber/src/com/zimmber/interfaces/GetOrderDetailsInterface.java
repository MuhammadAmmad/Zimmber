package com.zimmber.interfaces;

import java.util.ArrayList;
import com.zimmber.bin.OrderDetailsItem;

public interface GetOrderDetailsInterface {
	
	public void onStartedOrderDetails();

	public void onCompletedOrderDetails(ArrayList<OrderDetailsItem> listoforderdetails,
			String errorcode, String message);


}
