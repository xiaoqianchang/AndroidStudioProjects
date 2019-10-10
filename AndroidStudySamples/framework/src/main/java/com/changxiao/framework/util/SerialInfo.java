package com.changxiao.framework.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

public class SerialInfo {

  /**
   * GSF Service (Google Service Framework)
   */
  private static final Uri GSF_URI = Uri
      .parse("content://com.google.android.gsf.gservices");

  /**
   * Key del ID
   */
  private static final String GSF_ID_KEY = "android_id";

  /**
   * Devuelve el Device ID segun GSF (Google Service Framework)<br>
   * Ej: 3189147f8714e99a<br>
   * Se necesita: < uses-permission android:name=
   * "com.google.android.providers.gsf.permission.READ_GSERVICES" /> en el
   * AndroidManisfest.xml
   *
   * @param ctx Contexto de Aplicacion o Actividad
   * @return cadena con el id
   */
  public static String getGSFId(Context ctx) {
    final String[] params = {GSF_ID_KEY};
    final Cursor c = ctx.getContentResolver().query(GSF_URI, null, null,
        params, null);

    if (c == null || !c.moveToFirst() || c.getColumnCount() < 2) {
      return null;
    }

    try {
      return Long.toHexString(Long.parseLong(c.getString(1)));
    } catch (NumberFormatException e) {
      return null;
    } finally {
      if (c != null) {
        c.close();
      }
    }
  }

  private static String mSerial;

  public static String getBuildSerialId() {

    if (!TextUtils.isEmpty(mSerial)) {
      return mSerial;
    }

    // Intentamos Tomar el SERIAL del Hardware
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
      mSerial = android.os.Build.SERIAL; // Android 2.3 y superiores
    } else {
      mSerial = "undefined";
    }
    return mSerial;
  }

  private static String mAndroidId;

  /**
   * Devuelve el SERIAL del Hardware (android.os.Build.SERIAL), o el SERIAL de
   * Arranque del Dispositivo (Settings.Secure.ANDROID_ID)<br>
   * Ej: 202ec37cf8d93ece
   *
   * @return un numero de Serie unico del Hardware
   * @throws Exception Si el algoritmo de calculo falla
   */
  public static String getAndroidId(Context ctx) {

    if (!TextUtils.isEmpty(mAndroidId)) {
      return mAndroidId;
    }

    // Intentamos Tomar el SERIAL del Hardware
    // tomamos el ANDROID_ID => si esto es NULO, no podemos IDENTIFICAR al
    // Aparato de forma UNICA
    if (ctx != null) {
      mAndroidId = Settings.Secure.getString(ctx.getContentResolver(),
          Settings.Secure.ANDROID_ID);
    }
    if (mAndroidId == null || mAndroidId.equalsIgnoreCase("android_id")
        || mAndroidId.equalsIgnoreCase("9774d56d682e549c")) {
      mAndroidId = "undefined";
    }
    return mAndroidId;
  }

  private static String mImei;

  /**
   * Devuelve el SERIAL del Hardware "Tagus Tablet" (IMEI)<br>
   * Ej: 202141237121581
   *
   * @return un numero de Serie unico del Hardware Tagus (IMEI)
   * @throws Exception Si el algoritmo de calculo falla
   */
  public static String getIMEI(final Context ctx) {
    try {
      return getIMEIAndNotDefual(ctx);
    } catch (Exception e) {
      e.printStackTrace();
      return "undefined";
    }
  }

  public static String getIMEIAndNotDefual(final Context context) throws Exception {

    Runnable task = new Runnable() {
      @Override
      public void run() {
        try {
          // Intentamos Tomar el SERIAL del Hardware de Telefonia
          TelephonyManager tm = (TelephonyManager) context
              .getSystemService(Context.TELEPHONY_SERVICE);
          mImei = tm.getDeviceId(); // the IMEI for GSM
        } catch (Exception ex) {
          mImei = null;
        }
      }
    };

    if (!TextUtils.isEmpty(mImei)) {
      //更新IMEI，防止用户动态拔插电话卡
      MyAsyncTask.execute(task, true);
      return mImei;
    }
    task.run();

    if (TextUtils.isEmpty(mImei)) {
      throw new Exception("未获取到imei");
    }

    return mImei;
  }

  private static String deviceId = null;

  /**
   * Devuelve un Identificador unico de DISPOSITO Android UUID en base a un
   * algoritmo propio que utiliza el DeviceID(IMEI nulo si no es telefono),
   * Serial SIM(nulo si no hay SIM) y ANDROID_ID<br>
   * Si cambia cualquiera de estos parametros, cambiará el Identificador
   * Calculado.<br>
   * Ej: 00000000-381a-6648-50b0-ec5a0033c587<br>
   * En el peor de los casos, devuelve el android.os.Build.SERIAL o
   * Settings.Secure.ANDROID_ID<br>
   *
   * @return String con el Device ID calculado
   */
  public static String getDeviceId(Context ctx) {

    if (!TextUtils.isEmpty(deviceId)) {
      return deviceId;
    }

    try {
      final TelephonyManager tm = (TelephonyManager) ctx
          .getSystemService(Context.TELEPHONY_SERVICE);

      final String tmDevice, tmSerial, androidId;
      tmDevice = "" + tm.getDeviceId();
      tmSerial = "" + tm.getSimSerialNumber();
      androidId = ""
          + android.provider.Settings.Secure.getString(
          ctx.getContentResolver(),
          android.provider.Settings.Secure.ANDROID_ID);

      UUID deviceUuid = new UUID(androidId.hashCode(),
          ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
      deviceId = deviceUuid.toString();
    } catch (Exception e) {
      // Intentamos Tomar el SERIAL del Hardware -> porque el Terminal era
      // un TELEFONO pero mal configurado (sin SIM o error con datos
      // incorrectos en la Telefonia)
      try {
        deviceId = getSerialDeviceId(ctx);
      } catch (Exception e1) {
        throw new RuntimeException(
            "FATAL!!!! - This device doesn't have a UNIQUE Serial Number",
            e);
      }
    }

    return deviceId;
  }

  private static String deviceIdIgnoreSim = null;

  public static String getDeviceIdIgnoreSim(Context ctx) {

    if (!TextUtils.isEmpty(deviceIdIgnoreSim)) {
      return deviceIdIgnoreSim;
    }

    try {
      final TelephonyManager tm = (TelephonyManager) ctx
          .getSystemService(Context.TELEPHONY_SERVICE);

      final String tmDevice, androidId;
      tmDevice = "" + tm.getDeviceId();
      androidId = ""
          + android.provider.Settings.Secure.getString(
          ctx.getContentResolver(),
          android.provider.Settings.Secure.ANDROID_ID);

      UUID deviceUuid = new UUID(androidId.hashCode(), (long) tmDevice.hashCode());
      deviceIdIgnoreSim = deviceUuid.toString();
    } catch (Exception e) {
      // Intentamos Tomar el SERIAL del Hardware -> porque el Terminal era
      // un TELEFONO pero mal configurado (sin SIM o error con datos
      // incorrectos en la Telefonia)
      try {
        deviceIdIgnoreSim = getSerialDeviceId(ctx);
      } catch (Exception e1) {
        throw new RuntimeException(
            "FATAL!!!! - This device doesn't have a UNIQUE Serial Number",
            e);
      }
    }

    return deviceIdIgnoreSim;
  }

  /**
   * Devuelve una cadena de Descripcion del Dispositivo:<br>
   * Build.MANUFACTURER + Build.MODEL + Build.VERSION.RELEASE<br>
   * Ej: MID Tagus Tablet - 4.0.3
   */
  public static String getPid() {
    StringBuilder pid = new StringBuilder(Build.MANUFACTURER).append(" ")
        .append(Build.MODEL).append(" - ")
        .append(Build.VERSION.RELEASE);
    return pid.toString();
  }

  private static String mSerialDeviceId;

  /**
   * Devuelve el SERIAL del Hardware (android.os.Build.SERIAL), o el SERIAL de
   * Arranque del Dispositivo (Settings.Secure.ANDROID_ID)<br>
   * Ej: 202ec37cf8d93ece
   *
   * @return un numero de Serie unico del Hardware
   * @throws Exception Si el algoritmo de calculo falla
   */
  public static String getSerialDeviceId(Context ctx) throws Exception {

    if (!TextUtils.isEmpty(mSerialDeviceId)) {
      return mSerialDeviceId;
    }

    // Intentamos Tomar el SERIAL del Hardware
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
      mSerialDeviceId = android.os.Build.SERIAL; // Android 2.3 y superiores
    }
    if (mSerialDeviceId == null || mSerialDeviceId.equalsIgnoreCase("unknown")) {
      // tomamos el ANDROID_ID => si esto es NULO, no podemos IDENTIFICAR
      // al Aparato de forma UNICA
      mSerialDeviceId = Settings.Secure.getString(ctx.getContentResolver(),
          Settings.Secure.ANDROID_ID);
      if (mSerialDeviceId == null || mSerialDeviceId.equalsIgnoreCase("android_id")
          || mSerialDeviceId.equalsIgnoreCase("9774d56d682e549c")) {
        throw new Exception(
            "FATAL!!!! - This device doesn't have a UNIQUE Serial Number");
      }
    }
    return mSerialDeviceId;
  }

  /**
   * Devuelve el SERIAL del Hardware "Tagus Tablet" (IMEI)<br>
   * Ej: 202141237121581
   *
   * @return un numero de Serie unico del Hardware Tagus (IMEI)
   * @throws Exception Si el algoritmo de calculo falla
   */
  public static String getTagusSerialDeviceId(Context ctx) {
    String tmDevice = null;
    try {
      // Intentamos Tomar el SERIAL del Hardware de Telefonia
      final TelephonyManager tm = (TelephonyManager) ctx
          .getSystemService(Context.TELEPHONY_SERVICE);
      tmDevice = tm.getDeviceId(); // the IMEI for GSM
      if (tmDevice == null) {
        // Si es un TABLET "Tagus" pero NO tiene Telefonia
        tmDevice = getSerialDeviceId(ctx);
      }
    } catch (Exception ex) {
      tmDevice = getDeviceId(ctx);
    }

    return tmDevice;
  }

  private static String mVersion = "";

  /**
   * 获取应用版本号
   */
  public static String getVersionName(Context context) {

    if (!TextUtils.isEmpty(mVersion)) {
      return mVersion;
    }

    if (context == null) {
      return mVersion;
    }
    try {
      // 获取packagemanager的实例
      PackageManager packageManager = context.getPackageManager();
      // getPackageName()是你当前类的包名，0代表是获取版本信息
      if (packageManager != null) {
        PackageInfo packInfo;
        packInfo = packageManager.getPackageInfo(context.getPackageName(),
            0);
        if (packInfo != null) {
          mVersion = packInfo.versionName;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      mVersion = "";
    }
    return mVersion;
  }

  private static int mVersionCode = 0;

  /**
   * 获取应用代码版本号
   */
  public static int getVersionCode(Context context) {

    if (mVersionCode != 0) {
      return mVersionCode;
    }

    if (context == null) {
      return mVersionCode;
    }

    try {
      // 获取packagemanager的实例
      PackageManager packageManager = context.getPackageManager();
      // getPackageName()是你当前类的包名，0代表是获取版本信息
      PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),
          0);
      mVersionCode = packInfo.versionCode;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
      mVersionCode = 0;
    } catch (Exception e) { //可能会有DeadObjectException
      e.printStackTrace();
      mVersionCode = 0;
    }
    return mVersionCode;
  }

  public static String getSDkVersion() {
    String phoneVersion = android.os.Build.VERSION.SDK;
    return "Android-" + phoneVersion;
  }

  public static String getPhoneModel() {
    String phoneVersion = android.os.Build.MODEL;
    return phoneVersion;
  }

  public static void checkSignature(Context context, Object[] objects) {
    if (context == null || objects == null || objects.length < 3) {
      throw new RuntimeException("t" + MD5.md5("attr num enough") + "t");
    }
    if (objects.length > 3) {
      checkSignature(context, (String) objects[0], (String) objects[1], (Boolean) objects[2],
          (Boolean) objects[3], (Boolean) objects[4]);
    } else {
      checkSignature(context, (String) objects[0], (String) objects[1], (Boolean) objects[2]);
    }
  }

  // 检测 包名 ,签名是否一致
  public static void checkSignature(Context context, String pageNameMd5, String SigInfo,
      boolean isRelease) {
    checkSignature(context, pageNameMd5, SigInfo, isRelease, true, true);
  }

  // 检测 包名 ,签名是否一致
  public static void checkSignature(Context context, String pageNameMd5, String SigInfo,
      boolean isRelease, boolean isCheckPkg, boolean isCheckSign) {

    if (isCheckPkg) {
      if (!MD5.md5(context.getPackageName()).equals(pageNameMd5)) {
        throw new RuntimeException("t" + MD5.md5("second package") + "t");
      }
    }

    if (isCheckSign) {
      if (!getSingInfoMd5(context).equals(SigInfo)) {
        throw new RuntimeException("t" + MD5.md5("second sign") + "t");
      }
    }
  }

  public static String getSingInfoMd5(Context context) {
    try {
      String packageName = context.getPackageName();
      PackageInfo packageInfo = context.getPackageManager()
          .getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
      Signature[] signs = packageInfo.signatures;
      Signature sign = signs[0];
      String signString = MD5.md5(sign.toByteArray());
      return signString;
    } catch (Exception e) {
      return "";
    }
  }

  public static String mUserAgent = "";

  public static String getUserAgent(Context context) {
    if (context != null && TextUtils.isEmpty(mUserAgent)) {
      StringBuilder sb = new StringBuilder();
      sb.append("ting_");
      sb.append(getVersionName(context));
      sb.append("(");
      try {
        sb.append(URLEncoder.encode(android.os.Build.MODEL, "utf-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      sb.append(",");
      sb.append("Android" + android.os.Build.VERSION.SDK_INT);
      sb.append(")");
      mUserAgent = sb.toString();
    }
    return mUserAgent;
  }
}