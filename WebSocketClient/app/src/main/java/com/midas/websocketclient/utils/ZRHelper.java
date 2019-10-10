package com.midas.websocketclient.utils;

import android.content.Context;

import com.midas.websocketclient.bean.ZRUser;
import com.midas.websocketclient.db.dao.ZRContactDao;

import java.util.List;

/**
 * $desc$
 * <p/>
 * Created by Chang.Xiao on 2016/5/17.
 *
 * @version 1.0
 */
public class ZRHelper {

    public static ZRHelper zrHelper;

    private Context mContext;

    private ZRContactDao contactDao;

    /**
     * contact list in cache
     */
    private List<ZRUser> contactList;

    private ZRHelper() {
        this.contactDao = new ZRContactDao(mContext);
    }

    public static ZRHelper getInstance() {
        if (zrHelper == null) {
            synchronized (zrHelper.getClass()) {
                if (zrHelper == null) {
                    zrHelper = new ZRHelper();
                }
            }
        }
        return zrHelper;
    }

    public synchronized boolean onInit(Context context){
        this.mContext = context;
        return true;
    }

    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public List<ZRUser> getContactList() {
        if (contactList == null) {
            contactList = contactDao.getContactList();
        }
        return contactList;
    }

    /**
     * 保存单个user
     */
    public void saveContact(ZRUser contact){
        contactList.add(contact);
//        contactList.put(contact.get_id(), contact);
//        contactDao.saveContact(contact);
    }

}
