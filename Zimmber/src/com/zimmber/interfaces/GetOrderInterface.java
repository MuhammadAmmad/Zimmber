package com.zimmber.interfaces;

import java.util.ArrayList;
import com.zimmber.bin.OrderItem;

public interface GetOrderInterface {

	public void onStartedGetOrder();

	public void onCompletedGetOrder(ArrayList<OrderItem> listofmyorder,
			String errorcode, String message);

}
