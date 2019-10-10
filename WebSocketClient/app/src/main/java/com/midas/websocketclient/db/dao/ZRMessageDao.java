package com.midas.websocketclient.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.midas.websocketclient.bean.ZRUser;
import com.midas.websocketclient.bean.ZRMessage;
import com.midas.websocketclient.db.DBConfig;
import com.midas.websocketclient.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * $desc$
 * <p/>
 * Created by Chang.Xiao on 2016/5/17.
 *
 * @version 1.0
 */
public class ZRMessageDao {

    private Context mContext;
    private SQLiteDatabase db;
    private DBHelper mDBHelper;

    public ZRMessageDao(Context context) {
        this.mContext = context;
        db=context.openOrCreateDatabase(DBConfig.DB_NAME, Context.MODE_PRIVATE, null);
        mDBHelper = DBHelper.getInstance(context);
    }

    private void createMessageTable(String contactId) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + ZRMessage.TABLE_NAME + contactId + " ("
                + ZRMessage._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ZRMessage.FROM + " TEXT, "
                + ZRMessage.TO + " TEXT, "
                + ZRMessage.DATE + " TEXT, "
                + ZRMessage.CONTENT + " TEXT, ");
    }

    /**
     * 保存聊天数据，发送和接收的
     * @param contactId  联系人id
     * @param message
     */
    public void saveMsg(String contactId,ZRMessage message){
        createMessageTable(contactId);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(ZRMessage._ID, message.get_id());
            values.put(ZRMessage.FROM, message.getFrom().get_id());
            values.put(ZRMessage.TO, message.getTo().get_id());
            values.put(ZRMessage.DATE, message.getDate());
            values.put(ZRMessage.CONTENT, message.getContent());
            db.insert(ZRMessage.TABLE_NAME + contactId, null, values);
            db.close();
        }
    }
    /**
     * 查找用户的聊天记录，还不清楚查找存储方式，如果用户更换登录会怎样。这些表是单独和一个的聊天记录
     * @param contactId 要查找的id
     * @return 返回List<ZRMessage>。如果没有记录则为size()=0
     */
    public List<ZRMessage> getMsgByContactId(String contactId) {
        createMessageTable(contactId);
        List<ZRMessage> list=new ArrayList<ZRMessage>();

        db = mDBHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + ZRMessage.TABLE_NAME + contactId+" ORDER BY " + ZRMessage.DATE + " DESC LIMIT 10",null);
            while(cursor.moveToNext()) {
                String _id=cursor.getString(cursor.getColumnIndex(ZRMessage._ID));
                String from=cursor.getString(cursor.getColumnIndex(ZRMessage.FROM));
                String to=cursor.getString(cursor.getColumnIndex(ZRMessage.TO));
                String date=cursor.getString(cursor.getColumnIndex(ZRMessage.DATE));
                String content=cursor.getString(cursor.getColumnIndex(ZRMessage.CONTENT));
                ZRMessage message = new ZRMessage();
                message.set_id(_id);
                message.setFrom(new ZRUser(from));
                message.setTo(new ZRUser(to));
                message.setDate(date);
                message.setContent(content);
                list.add(message);
            }
        }
        return list;
    }
    /**
     * 关闭数据库连接
     */
    public void close()
    {
        if(db!=null){
            db.close();
        }
    }
}
