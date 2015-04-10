package com.zimmber.interfaces;

import java.util.ArrayList;
import com.zimmber.bin.AddressItem;

public interface GetAddressInterface {

	public void onStartedGetAddress();

	public void onCompletedGetAddress(ArrayList<AddressItem> listofmyaddress,
			String errorcode, String message);

}
