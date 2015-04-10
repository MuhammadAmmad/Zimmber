package com.zimmber.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.zimmber.R;

public class TermsofUseFragment extends Fragment {

	private WebView webView;
	ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.terms_of_use, container,
				false);

		webView = (WebView) rootView.findViewById(R.id.webView);
		startWebView("http://zimmber.com/front/default/MobTermsOfUse");

		return rootView;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void startWebView(String url) {

		// Create new webview Client to show progress dialog
		// When opening a url or click on link

		webView.setWebViewClient(new WebViewClient() {

			// If you will not use this method url links are open in new browser
			// not in webview
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// Show loader on url load
			public void onLoadResource(WebView view, String url) {
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(getActivity());
					progressDialog.setMessage("Loading...");
					progressDialog.show();
				}
			}

			public void onPageFinished(WebView view, String url) {
				try {
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();	
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		});

		// Javascript inabled on webview
		webView.getSettings().setJavaScriptEnabled(true);

		// Other webview options

		// webView.getSettings().setLoadWithOverviewMode(true);
		// webView.getSettings().setUseWideViewPort(true);
		// webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		// webView.setScrollbarFadingEnabled(false);
		webView.getSettings().setBuiltInZoomControls(true);

		/*
		 * String summary =
		 * "<html><body>You scored <b>192</b> points.</body></html>";
		 * webview.loadData(summary, "text/html", null);
		 */

		// Load url in webview
		webView.loadUrl(url);

	}

}
