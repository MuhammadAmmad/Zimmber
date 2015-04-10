package com.zimmber.fragment;

import com.zimmber.R;
import com.zimmber.database.SharedPreferenceClass;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class CheckoutPayuMoneyFragment extends Fragment {

	View rootView;

	WebView webView;

	String url = "http://www.zimmber.com/MobileCheckout/Payment?Ref_Id=";
	String booking_id = "";

	SharedPreferenceClass sharedpreference;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.checkout_payumoney, container,
				false);

		initialize();

		booking_id = sharedpreference.getBookingId();

		url = url + booking_id;
		Log.v("url", url);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);

		return rootView;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		webView = (WebView) rootView.findViewById(R.id.webView);

		sharedpreference = new SharedPreferenceClass(getActivity());

	}

}
