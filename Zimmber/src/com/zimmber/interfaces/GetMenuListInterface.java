package com.zimmber.interfaces;

import java.util.ArrayList;
import com.zimmber.bin.MenuListItem;

public interface GetMenuListInterface {

	public void onStartedMenu();

	public void onCompletedMenu(String errorcode, String message,
			ArrayList<MenuListItem> listofmenu);

}
