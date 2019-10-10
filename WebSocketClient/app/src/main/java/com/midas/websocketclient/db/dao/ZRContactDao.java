package com.midas.websocketclient.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.midas.websocketclient.bean.ZRUser;
import com.midas.websocketclient.db.DBHelper;

/**
 * 联系人Dao
 * 
 * Created by Chang.Xiao on 2016/5/17.
 * @version 1.0
 */
public class ZRContactDao {

	private Context mContext;
	private DBHelper mDBHelper;

	public ZRContactDao(Context context) {
		this.mContext = context;
		mDBHelper = DBHelper.getInstance(context);
	}

	/**
	 * 保存、更新好友list
	 *
	 * @param contactList
	 */
	synchronized public void saveContactList(List<ZRUser> contactList) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(ZRUser.TABLE_NAME, null, null);
			for (ZRUser contact : contactList) {
				ContentValues values = new ContentValues();
				values.put(ZRUser._ID, contact.get_id());
				if(contact.getNickName() != null)
					values.put(ZRUser.NICKNAME, contact.getNickName());
				if(contact.getAvatar() != null)
					values.put(ZRUser.AVATAR, contact.getAvatar());
				db.replace(ZRUser.TABLE_NAME, null, values);
			}
			db.close();
		}
	}

	/**
	 * 获取好友list
	 *
	 * @return
	 */
	synchronized public List<ZRUser> getContactList() {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<ZRUser> users = new ArrayList<ZRUser>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + ZRUser.TABLE_NAME /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String _id = cursor.getString(cursor.getColumnIndex(ZRUser._ID));
				String name = cursor.getString(cursor.getColumnIndex(ZRUser.NAME));
				String nick = cursor.getString(cursor.getColumnIndex(ZRUser.NICKNAME));
				String avatar = cursor.getString(cursor.getColumnIndex(ZRUser.AVATAR));
				ZRUser contact = new ZRUser();
				contact.set_id(_id);
				contact.setName(name);
				contact.setNickName(nick);
				contact.setAvatar(avatar);
				users.add(contact);
			}
			cursor.close();
			db.close();
		}
		return users;
	}

	/**
	 * 删除一个联系人
	 * @param name
	 */
	synchronized public void deleteContact(String name){
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(ZRUser.TABLE_NAME, ZRUser.NAME + " = ?", new String[]{name});
			db.close();
		}
	}

	/**
	 * 保存一个联系人
	 * @param contact
	 */
	synchronized public void saveContact(ZRUser contact){
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put(ZRUser._ID, contact.get_id());
			values.put(ZRUser.NAME, contact.getName());
			if(contact.getNickName() != null)
				values.put(ZRUser.NICKNAME, contact.getNickName());
			if(contact.getAvatar() != null)
				values.put(ZRUser.AVATAR, contact.getAvatar());
			db.insert(ZRUser.TABLE_NAME, null, values);
			db.close();
		}
	}


	synchronized public void closeDB(){
		if(mDBHelper != null){
			mDBHelper.closeDB();
		}
	}
    
}
