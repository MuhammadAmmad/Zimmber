package com.zimmber.networkutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

	private Context _context;

	public ConnectionDetector(Context context) {
		this._context = context;
	}

	/**
	 * Checking for all possible internet providers
	 * **/
	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						// if(info[i].getTypeName().equalsIgnoreCase("mobile")){
						// String resoan=info[i].getReason();
						//
						// if(resoan!=null&&resoan.equalsIgnoreCase("dataEnabled")){
						// return true;
						// }
						// }else {

						if (info[i].isAvailable()) {

							return true;
							// }

						}
					}

		}
		return false;
	}
}
