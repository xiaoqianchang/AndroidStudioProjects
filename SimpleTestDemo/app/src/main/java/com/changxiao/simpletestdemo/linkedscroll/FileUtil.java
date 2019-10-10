package com.changxiao.simpletestdemo.linkedscroll;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2017/12/4.
 *
 * @version 1.0
 */

public class FileUtil {

    private static final String ENCODE = "UTF-8";

    /**
     * 从文件读取声音件(当前assets目录下面)
     *
     * @param context 上下文
     * @return
     */
    public static String readAssetFileData(Context context, String filePath) {

        if (context == null)
            return "";

        String result = "";

        if (result == null || result.equals("")) {
            AssetManager am = context.getResources().getAssets();
            InputStream inputStream = null;
            try {
                inputStream = am.open(filePath);
                result = inputStream2String(inputStream, ENCODE);
            } catch (Exception e) {
            } finally {
                if (inputStream != null)
                    try {
                        inputStream.close();
                        inputStream = null;
                    } catch (IOException e) {
                    }
            }
        }

        if (result == null)
            result = "";

        return result;

    }

    public static String inputStream2String(InputStream inputStream,
                                            String encoding) {

        String result = "";
        try {
            int lenght = inputStream.available();
            byte[] buffer = new byte[lenght];
            inputStream.read(buffer);
            result = new String(buffer, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result == null)
            result = "";

        return result;
    }
}
