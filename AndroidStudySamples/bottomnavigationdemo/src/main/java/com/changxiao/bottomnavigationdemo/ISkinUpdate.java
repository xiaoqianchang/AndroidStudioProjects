package com.changxiao.bottomnavigationdemo;


import android.graphics.drawable.Drawable;

/**
 * Call back when theme has changed </br>
 * Normally implements by activity of fragment
 * 
 * @author fengjun
 */
public interface ISkinUpdate {
	void onSkinUpdate(int position, Drawable selectedDrawable);
}
