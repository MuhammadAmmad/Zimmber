package com.zimmber.database;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	static final String KEY_CHECKOUTLIST_ID = "id";

	static final String KEY_SERVICE_ID = "service_id";

	static final String KEY_SERVICE_NAME = "service_name";

	static final String KEY_VAR1_ID = "var1_id";

	static final String KEY_VAR1_NAME = "var1_name";

	static final String KEY_VAR2_ID = "var2_id";

	static final String KEY_VAR2_NAME = "var2_name";

	static final String KEY_VAR3_ID = "var3_id";

	static final String KEY_VAR3_NAME = "var3_name";

	static final String KEY_VAR4_ID = "var4_id";

	static final String KEY_VAR4_NAME = "var4_name";

	static final String KEY_ACTIVITY_ID = "activity_id";

	static final String KEY_ACTIVITY_NAME = "activity_name";

	static final String KEY_ACTIVITY_COUNT = "activity_count";

	static final String KEY_SERVICE_STATUS = "service_status";

	static final String KEY_SERVICE_PRICE = "service_price";

	static final String KEY_SERVICE_TERMS = "service_terms";

	static final String KEY_CUST_VAR1 = "cust_var1";

	static final String KEY_CUST_VAR2 = "cust_var2";

	static final String KEY_CUST_VAR3 = "cust_var3";

	static final String KEY_CUST_VAR4 = "cust_var4";

	static final String KEY_COUPON_CODE = "coupon_code";

	static final String KEY_OFFER_PRICE = "offer_price";

	static final String TAG = "DBAdapter";

	static final String DATABASE_NAME = "CheckoutListMasterDB";

	static final String DATABASE_TABLE = "checkoutlistmaster";

	static final int DATABASE_VERSION = 2;

	static final String DATABASE_CREATE = "create table checkoutlistmaster (id integer primary key autoincrement,"
			+ "service_id text not null, service_name text not null, var1_id text not null, var1_name text not null, cust_var1 text not null,var2_id text not null, var2_name text not null, cust_var2 text not null, var3_id text not null, var3_name text not null, cust_var3 text not null, var4_id text not null, var4_name text not null, cust_var4 text not null, activity_id text not null, activity_name text not null, activity_count text not null, service_status text not null, service_price text not null, service_terms text not null, coupon_code text not null, offer_price text not null);";

	final Context context;

	DatabaseHelper DBHelper;

	SQLiteDatabase db;

	String order_by = KEY_CHECKOUTLIST_ID;

	String[] col = new String[] { KEY_CHECKOUTLIST_ID, KEY_SERVICE_ID,
			KEY_SERVICE_NAME, KEY_VAR1_ID, KEY_VAR1_NAME, KEY_CUST_VAR1,
			KEY_VAR2_ID, KEY_VAR2_NAME, KEY_CUST_VAR2, KEY_VAR3_ID,
			KEY_VAR3_NAME, KEY_CUST_VAR3, KEY_VAR4_ID, KEY_VAR4_NAME,
			KEY_CUST_VAR4, KEY_ACTIVITY_ID, KEY_ACTIVITY_NAME,
			KEY_ACTIVITY_COUNT, KEY_SERVICE_STATUS, KEY_SERVICE_PRICE,
			KEY_SERVICE_TERMS, KEY_COUPON_CODE, KEY_OFFER_PRICE };

	ArrayList<HashMap<String, String>> checkoutList = new ArrayList<HashMap<String, String>>();

	public DBAdapter(Context ctx) {

		this.context = ctx;

		DBHelper = new DatabaseHelper(context);

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {

			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			try {

				db.execSQL(DATABASE_CREATE);

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			Log.v(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS checkoutlistmaster");

			onCreate(db);
		}

		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {

			Log.v(TAG, "Downgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS checkoutlistmaster");

			onCreate(db);
		}

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ---opens the database---
	public DBAdapter open() throws SQLException {

		DBHelper = new DatabaseHelper(context);

		db = DBHelper.getWritableDatabase();

		return this;

	}

	// ---closes the database---
	public void close() {

		DBHelper.close();

	}

	// ---insert a contact into the database---
	public long insertValue(String service_id, String service_name,
			String var1_id, String var1_name, String cust_var1, String var2_id,
			String var2_name, String cust_var2, String var3_id,
			String var3_name, String cust_var3, String var4_id,
			String var4_name, String cust_var4, String activity_id,
			String activity_name, String activity_count, String service_status,
			String service_price, String service_terms, String coupon_code,
			String offer_price) {

		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_SERVICE_ID, service_id);

		initialValues.put(KEY_SERVICE_NAME, service_name);

		initialValues.put(KEY_VAR1_ID, var1_id);

		initialValues.put(KEY_VAR1_NAME, var1_name);

		initialValues.put(KEY_CUST_VAR1, cust_var1);

		initialValues.put(KEY_VAR2_ID, var2_id);

		initialValues.put(KEY_VAR2_NAME, var2_name);

		initialValues.put(KEY_CUST_VAR2, cust_var2);

		initialValues.put(KEY_VAR3_ID, var3_id);

		initialValues.put(KEY_VAR3_NAME, var3_name);

		initialValues.put(KEY_CUST_VAR3, cust_var3);

		initialValues.put(KEY_VAR4_ID, var4_id);

		initialValues.put(KEY_VAR4_NAME, var4_name);

		initialValues.put(KEY_CUST_VAR4, cust_var4);

		initialValues.put(KEY_ACTIVITY_ID, activity_id);

		initialValues.put(KEY_ACTIVITY_NAME, activity_name);

		initialValues.put(KEY_ACTIVITY_COUNT, activity_count);

		initialValues.put(KEY_SERVICE_STATUS, service_status);

		initialValues.put(KEY_SERVICE_PRICE, service_price);

		initialValues.put(KEY_SERVICE_TERMS, service_terms);

		initialValues.put(KEY_COUPON_CODE, coupon_code);

		initialValues.put(KEY_OFFER_PRICE, offer_price);

		return db.insert(DATABASE_TABLE, null, initialValues);

	}

	// ---deletes a particular contact---
	public boolean deleteRecord(String checkoutlist_id) {
		return db.delete(DATABASE_TABLE, KEY_CHECKOUTLIST_ID + "="
				+ checkoutlist_id, null) > 0;
	}

	// ---retrieves all records---

	public ArrayList<HashMap<String, String>> getRecords() {
		// TODO Auto-generated method stub

		/*
		 * Cursor c = db.query(DATABASE_TABLE, col, null, null, null, null,
		 * order_by + " DESC");
		 */

		Cursor c = db.query(DATABASE_TABLE, col, null, null, null, null,
				order_by);

		int checkoutlist_id_pos = c.getColumnIndex(KEY_CHECKOUTLIST_ID);

		int service_id_pos = c.getColumnIndex(KEY_SERVICE_ID);

		int service_name_pos = c.getColumnIndex(KEY_SERVICE_NAME);

		int var1_id_pos = c.getColumnIndex(KEY_VAR1_ID);

		int var1_name_pos = c.getColumnIndex(KEY_VAR1_NAME);

		int cust_var1_pos = c.getColumnIndex(KEY_CUST_VAR1);

		int var2_id_pos = c.getColumnIndex(KEY_VAR2_ID);

		int var2_name_pos = c.getColumnIndex(KEY_VAR2_NAME);

		int cust_var2_pos = c.getColumnIndex(KEY_CUST_VAR2);

		int var3_id_pos = c.getColumnIndex(KEY_VAR3_ID);

		int var3_name_pos = c.getColumnIndex(KEY_VAR3_NAME);

		int cust_var3_pos = c.getColumnIndex(KEY_CUST_VAR3);

		int var4_id_pos = c.getColumnIndex(KEY_VAR4_ID);

		int var4_name_pos = c.getColumnIndex(KEY_VAR4_NAME);

		int cust_var4_pos = c.getColumnIndex(KEY_CUST_VAR4);

		int activity_id_pos = c.getColumnIndex(KEY_ACTIVITY_ID);

		int activity_name_pos = c.getColumnIndex(KEY_ACTIVITY_NAME);

		int activity_count_pos = c.getColumnIndex(KEY_ACTIVITY_COUNT);

		int service_status_pos = c.getColumnIndex(KEY_SERVICE_STATUS);

		int service_price_pos = c.getColumnIndex(KEY_SERVICE_PRICE);

		int service_terms_pos = c.getColumnIndex(KEY_SERVICE_TERMS);

		int coupon_code_pos = c.getColumnIndex(KEY_COUPON_CODE);

		int offer_price_pos = c.getColumnIndex(KEY_OFFER_PRICE);

		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {

			String checkoutlist_id = c.getString(checkoutlist_id_pos);

			String service_id = c.getString(service_id_pos);

			String service_name = c.getString(service_name_pos);

			String var1_id = c.getString(var1_id_pos);

			String var1_name = c.getString(var1_name_pos);

			String cust_var1 = c.getString(cust_var1_pos);

			String var2_id = c.getString(var2_id_pos);

			String var2_name = c.getString(var2_name_pos);

			String cust_var2 = c.getString(cust_var2_pos);

			String var3_id = c.getString(var3_id_pos);

			String var3_name = c.getString(var3_name_pos);

			String cust_var3 = c.getString(cust_var3_pos);

			String var4_id = c.getString(var4_id_pos);

			String var4_name = c.getString(var4_name_pos);

			String cust_var4 = c.getString(cust_var4_pos);

			String activity_id = c.getString(activity_id_pos);

			String activity_name = c.getString(activity_name_pos);

			String activity_count = c.getString(activity_count_pos);

			String service_status = c.getString(service_status_pos);

			String service_price = c.getString(service_price_pos);

			String service_terms = c.getString(service_terms_pos);

			String coupon_code = c.getString(coupon_code_pos);

			String offer_price = c.getString(offer_price_pos);

			HashMap<String, String> map = new HashMap<String, String>();

			// adding each child node to HashMap key => value

			map.put(KEY_CHECKOUTLIST_ID, checkoutlist_id);

			map.put(KEY_SERVICE_ID, service_id);

			map.put(KEY_SERVICE_NAME, service_name);

			map.put(KEY_VAR1_ID, var1_id);

			map.put(KEY_VAR1_NAME, var1_name);

			map.put(KEY_CUST_VAR1, cust_var1);

			map.put(KEY_VAR2_ID, var2_id);

			map.put(KEY_VAR2_NAME, var2_name);

			map.put(KEY_CUST_VAR2, cust_var2);

			map.put(KEY_VAR3_ID, var3_id);

			map.put(KEY_VAR3_NAME, var3_name);

			map.put(KEY_CUST_VAR3, cust_var3);

			map.put(KEY_VAR4_ID, var4_id);

			map.put(KEY_VAR4_NAME, var4_name);

			map.put(KEY_CUST_VAR4, cust_var4);

			map.put(KEY_ACTIVITY_ID, activity_id);

			map.put(KEY_ACTIVITY_NAME, activity_name);

			map.put(KEY_ACTIVITY_COUNT, activity_count);

			map.put(KEY_SERVICE_STATUS, service_status);

			map.put(KEY_SERVICE_PRICE, service_price);

			map.put(KEY_SERVICE_TERMS, service_terms);

			map.put(KEY_COUPON_CODE, coupon_code);

			map.put(KEY_OFFER_PRICE, offer_price);

			// adding HashList to ArrayList
			checkoutList.add(map);
		}
		c.close();

		return checkoutList;

	}

}
