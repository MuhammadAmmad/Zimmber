package com.zimmber.adapter;

import com.zimmber.fragment.AddressFragment;
import com.zimmber.fragment.ChangePasswordFragment;
import com.zimmber.fragment.MyAssetsFragment;
import com.zimmber.fragment.OrderFragment;
import com.zimmber.fragment.ProfileFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@Override
	public Fragment getItem(int index) {

		

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new ProfileFragment();
		case 1:
			// Games fragment activity
			return new AddressFragment();
		case 2:
			// Movies fragment activity
			return new ChangePasswordFragment();
		case 3:
			// Movies fragment activity
			return new OrderFragment();
		/*case 4:
			// Movies fragment activity
			return new MyAssetsFragment();*/
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
