package com.midas.websocketclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database Helper
 *
 * Created by Chang.Xiao on 2016/5/17.
 * @version 1.0
 */
public class DBHelper extends SQLiteOpenHelper{

	private Context mContext;
	private static DBHelper instance;

	public static DBHelper getInstance(Context context) {
		if (instance == null) {
			synchronized (DBHelper.class) {
				if (instance == null) {
					instance = new DBHelper(context.getApplicationContext());
				}
			}
		}
		return instance;
	}

	private DBHelper(Context context) {
		super(context, DBConfig.DB_NAME, null, DBConfig.DB_VERSION);
		this.mContext = context;
	}

	// 这个方法 在数据库被创建的时候才会执行，一旦执行完成，不会执行第二次
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建表
		db.execSQL(DBConfig.CONTACT_TABLE_CREATESTR);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public void closeDB() {
	    if (instance != null) {
	        try {
	            SQLiteDatabase db = instance.getWritableDatabase();
	            db.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        instance = null;
	    }
	}
	
}
