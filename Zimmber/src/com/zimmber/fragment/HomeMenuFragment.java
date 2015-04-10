package com.zimmber.fragment;

import java.util.ArrayList;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.zimmber.HomeMainActivity;
import com.zimmber.R;
import com.zimmber.adapter.MenuListAdapter;
import com.zimmber.asynctask.GetMenuListAsynctask;
import com.zimmber.bin.MenuListItem;
import com.zimmber.database.SharedPreferenceClass;
import com.zimmber.interfaces.GetMenuListInterface;

public class HomeMenuFragment extends Fragment implements GetMenuListInterface {

	View rootView;

	GridView gridview;
	TextView tv_express_checkout;
	ImageView img_call;

	ArrayList<MenuListItem> listofmenu;
	MenuListAdapter adapter;

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	SharedPreferenceClass sharedpreference;

	private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.home_menu, container, false);

		initialize();

		onclick();

		/*Runnable run = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				handler.postDelayed(this, 1000);
				if (tv_express_checkout.isShown()) {
					tv_express_checkout.setVisibility(View.INVISIBLE);

				} else {
					tv_express_checkout.setVisibility(View.VISIBLE);
				}
			}

		};
		handler = new Handler();
		handler.post(run);*/

		GetMenuListAsynctask get_menu = new GetMenuListAsynctask(getActivity());
		get_menu.menuintf = HomeMenuFragment.this;
		get_menu.execute();

		return rootView;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		gridview = (GridView) rootView.findViewById(R.id.grid_view);
		tv_express_checkout = (TextView) rootView
				.findViewById(R.id.tv_express_checkout);
		img_call = (ImageView) rootView.findViewById(R.id.img_call);

		listofmenu = new ArrayList<MenuListItem>();

		sharedpreference = new SharedPreferenceClass(getActivity());

	}

	private void onclick() {
		// TODO Auto-generated method stub

		tv_express_checkout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				((HomeMainActivity) getActivity())
						.loadExpressCheckoutFragment();

			}
		});

		img_call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String number = "8080 824 824";

				Intent dial = new Intent();
				dial.setAction("android.intent.action.DIAL");
				dial.setData(Uri.parse("tel:" + number));
				startActivity(dial);

			}
		});

	}

	@Override
	public void onStartedMenu() {
		// TODO Auto-generated method stub

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		// pDialog.show();

	}

	@Override
	public void onCompletedMenu(String errorcode, String message,
			ArrayList<MenuListItem> listofmenu) {
		// TODO Auto-generated method stub

		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (listofmenu.size() > 0) {

			adapter = new MenuListAdapter(getActivity(), listofmenu);
			gridview.setAdapter(adapter);

			gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub

					sharedpreference.saveSelectServiceId(adapter.getItem(arg2)
							.getServiceId());
					sharedpreference.saveSelectServiceName(adapter
							.getItem(arg2).getServiceName());

					((HomeMainActivity) getActivity()).loadServiceFragment();

				}
			});

		}

		else {

			alert_dialog = new Dialog(getActivity());
			alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alert_dialog.setContentView(R.layout.dialog_alert);
			alert_dialog.show();

			TextView alert_msg = (TextView) alert_dialog
					.findViewById(R.id.alert_msg);
			Button alert_ok = (Button) alert_dialog.findViewById(R.id.alert_ok);

			alert_msg.setText(message);

			alert_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					alert_dialog.dismiss();

				}
			});
		}

	}

}
