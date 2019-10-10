package com.changxiao.themeskin.utils;

import android.content.Context;

public class SharedPreferencesUtil extends BaseSharedPreferencesUtil {

	private static SharedPreferencesUtil instance;

	public SharedPreferencesUtil(Context context, String name) {
		super(context, name);
	}

	public static SharedPreferencesUtil getInstance(Context context) {

		if (instance == null) {
			instance = new SharedPreferencesUtil(context, AppConfig.SP_DATA);
		}
		return instance;
	}

}
