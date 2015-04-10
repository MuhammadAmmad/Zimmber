package com.zimmber.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.zimmber.R;
import com.zimmber.UpdateProfileActivity;
import com.zimmber.database.SharedPreferenceClass;

public class ProfileFragment extends Fragment {

	View rootView;

	TextView tv_name, tv_email, tv_phone, tv_dob, tv_gender, tv_marital_status,
			tv_address, tv_state, tv_city, tv_location, tv_landmark, tv_street,
			tv_flatno, tv_pincode;

	Button btn_edit_profile;

	String email = "", access_token = "";

	private ProgressDialog pDialog;
	Dialog alert_dialog;

	SharedPreferenceClass sharedpreference;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		rootView = inflater.inflate(R.layout.profile_details, container, false);

		initialize();

		onclick();

		email = sharedpreference.getUserEmail();

		tv_name.setText(sharedpreference.getFirstName() + " "
				+ sharedpreference.getLastName());
		tv_email.setText(sharedpreference.getUserEmail());
		tv_phone.setText(sharedpreference.getPhone());
		tv_dob.setText(sharedpreference.getDOB());
		tv_gender.setText(sharedpreference.getGender());
		tv_marital_status.setText(sharedpreference.getMaritalStatus());
		tv_state.setText(sharedpreference.getState());
		tv_city.setText(sharedpreference.getCity());
		tv_location.setText(sharedpreference.getLocation());
		tv_landmark.setText(sharedpreference.getLandmark());
		tv_street.setText(sharedpreference.getStreet());
		tv_flatno.setText(sharedpreference.getFlatNo());
		tv_address.setText(sharedpreference.getLocation() + "\n"
				+ sharedpreference.getCity() + "\n"
				+ sharedpreference.getState());
		tv_pincode.setText(sharedpreference.getPincode());

		return rootView;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		tv_name = (TextView) rootView.findViewById(R.id.tv_name);

		tv_email = (TextView) rootView.findViewById(R.id.tv_email);
		tv_phone = (TextView) rootView.findViewById(R.id.tv_phone);
		tv_dob = (TextView) rootView.findViewById(R.id.tv_dob);
		tv_gender = (TextView) rootView.findViewById(R.id.tv_gender);
		tv_marital_status = (TextView) rootView
				.findViewById(R.id.tv_marital_status);
		tv_state = (TextView) rootView.findViewById(R.id.tv_state);
		tv_city = (TextView) rootView.findViewById(R.id.tv_city);
		tv_location = (TextView) rootView.findViewById(R.id.tv_location);
		tv_landmark = (TextView) rootView.findViewById(R.id.tv_landmark);
		tv_street = (TextView) rootView.findViewById(R.id.tv_street);
		tv_flatno = (TextView) rootView.findViewById(R.id.tv_flatno);
		tv_address = (TextView) rootView.findViewById(R.id.tv_address);
		tv_pincode = (TextView) rootView.findViewById(R.id.tv_pincode);

		btn_edit_profile = (Button) rootView
				.findViewById(R.id.btn_edit_profile);

		sharedpreference = new SharedPreferenceClass(getActivity());

	}

	private void onclick() {
		// TODO Auto-generated method stub

		btn_edit_profile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(getActivity(),
						UpdateProfileActivity.class));
				
				getActivity().finish();

			}
		});

	}

}