package com.midas.websocketclient.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.midas.websocketclient.R;
import com.midas.websocketclient.bean.ZRUser;

/**
 * 
 * 
 * Created by Chang.Xiao on 2016/5/17.
 * @version 1.0
 */
public class ZRContactUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static ZRUser getUserInfo(String username){
//		ZRContact contact = ZRHelper.getInstance().getContactList().get(username);
		ZRUser contact = null;
        if(contact == null){
            contact = new ZRUser(username);
        }
            
        if(contact != null){
            //demo没有这些数据，临时填充
        	if(TextUtils.isEmpty(contact.getNickName()))
				contact.setNickName(username);
        }
        return contact;
    }
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
		ZRUser contact = getUserInfo(username);
        if(contact != null && !TextUtils.isEmpty(contact.getAvatar())){
            Glide.with(context).load(contact.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
        }else{
			Glide.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }
    
	/**
     * 设置陌生人用户头像
     * @param headerImageUrl 头像地址
     */
    public static void setContactAvatar(Context context, String headerImageUrl, ImageView imageView){
        if(!TextUtils.isEmpty(headerImageUrl)){
			Glide.with(context).load(headerImageUrl).placeholder(R.drawable.default_avatar).into(imageView);
        }else{
			Glide.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
		ZRUser contact = getUserInfo(username);
    	if(contact != null){
    		textView.setText(contact.getNickName());
    	}else{
    		textView.setText(username);
    	}
    }
    
    /**
     * 设置陌生人昵称
     */
    public static void setStrangerNick(String nickname,TextView textView){
    	textView.setText(nickname);
    }
    
    /**
     * 保存或更新某个用户
     * @param newUser
     */
	public static void saveUserInfo(ZRUser newUser) {
		if (newUser == null || newUser.getName() == null) {
			return;
		}
		ZRHelper.getInstance().saveContact(newUser);
	}
	
	/**
	 * 查询当前用户是否是我的好友、是否在cache和DB中
	 * 
	 * @param username
	 */
	/*public static boolean isMyFriend(String username) {
		ZRContact contact = ZRHelper.getInstance().getContactList().get(username);
		if (contact != null) {
			return true;
		}
		return false;
	}*/

}
