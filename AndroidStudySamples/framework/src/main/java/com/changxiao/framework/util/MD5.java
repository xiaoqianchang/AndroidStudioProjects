package com.changxiao.framework.util;

import android.text.TextUtils;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * MD5
 *
 * Created by Chang.Xiao on 2019/1/13.
 *
 * @version 1.0
 */
public class MD5 {

  public static String md5(byte[] input) {
    String result = "";
    if (input != null) {
      try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input);
        BigInteger hash = new BigInteger(1, md.digest());

        for(result = hash.toString(16); result.length() < 32; result = "0" + result) {
          ;
        }
      } catch (Exception var4) {
        Logger.log("md5加密出错" + var4.getMessage());
      }
    }

    return result;
  }

  public static String md5(String input) {
    String result = input;
    if (input != null) {
      try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        BigInteger hash = new BigInteger(1, md.digest());

        for(result = hash.toString(16); result.length() < 32; result = "0" + result) {
          ;
        }
      } catch (Exception var4) {
        Logger.log("md5加密出错" + var4.getMessage());
      }
    }

    return result;
  }

  public static String getFileNameMd5(String url) {
    if (TextUtils.isEmpty(url)) {
      return "";
    } else {
      int end = url.indexOf("?");
      url = url.substring(url.indexOf("/", "http://".length()), end == -1 ? url.length() : end);
      return md5(url);
    }
  }
}
