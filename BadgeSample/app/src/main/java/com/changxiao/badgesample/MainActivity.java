package com.changxiao.badgesample;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.changxiao.badgesample.utils.BadgeUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity {

    //必须使用，Activity启动页
    private final static String lancherActivityClassName = MainActivity.class.getName();

    @Bind(R.id.numInput)
    EditText numInput;
    @Bind(R.id.btnSetBadge)
    Button setBadgeConunt;
    @Bind(R.id.btnRemoveBadge)
        Button resetBadgeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //find the home launcher Package
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;

        TextView textViewHomePackage = (TextView) findViewById(R.id.textViewHomePackage);
        textViewHomePackage.setText("launcher:" + currentHomePackage);

    }

    @OnClick({R.id.btnSetBadge, R.id.btnRemoveBadge})
    public void onClick(View v) {
        int badgeCount = 1;
        try {
            badgeCount = Integer.parseInt(numInput.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Error input", Toast.LENGTH_SHORT).show();
        }
        switch (v.getId()) {
            case R.id.btnSetBadge:
                setBadgeConunt(badgeCount);
//                showAtMIUI(badgeCount);
                break;
            case R.id.btnRemoveBadge:
                resetBadgeCount(badgeCount);
                break;
        }
    }

    private void showAtMIUI(int badgeCount) {
        /**
         * http://dev.xiaomi.com/doc/p=3904/index.html
         */
        // MIUI 6上重新设计了桌面app图标的角标显示，基本规则如下
        NotificationManager mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("title").setContentText("text").setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, badgeCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mNotificationManager.notify(0,notification);
    }

    private void setBadgeConunt(int badgeCount) {
        // 发送未读消息数目广播：count为未读消息数目（int类型）
//        BadgeUtil.setBadgeCount(this, 90);
        boolean apply = ShortcutBadger.applyCount(this, badgeCount);
        Toast.makeText(getApplicationContext(), "Set count=" + badgeCount + ", success=" + apply, Toast.LENGTH_SHORT).show();
    }

    private void resetBadgeCount(int badgeCount) {
        // 发送重置/清除未读消息数目广播
//        BadgeUtil.resetBadgeCount(this);
        boolean apply = ShortcutBadger.applyCount(this, 0);
        //        ShortcutBadger.removeCount(this);
        Toast.makeText(getApplicationContext(), "success=" + apply, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        sendBadgeNumber();
    }

    private void sendBadgeNumber() {
        String number = "35";
        if (TextUtils.isEmpty(number)) {
            number = "0";
        } else {
            int numInt = Integer.valueOf(number);
            number = String.valueOf(Math.max(0, Math.min(numInt, 99)));
        }

        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            sendToXiaoMi(number);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            sendToSony(number);
        } else if (Build.MANUFACTURER.toLowerCase().contains("sony")) {
            sendToSamsumg(number);
        } else {
            Toast.makeText(this, "Not Support" + Build.MANUFACTURER, Toast.LENGTH_LONG).show();
        }
    }

    private void sendToXiaoMi(String number) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        boolean isMiUIV6 = true;
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("您有"+number+"未读消息");
            builder.setTicker("您有"+number+"未读消息");
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setDefaults(Notification.DEFAULT_LIGHTS);
            notification = builder.build();
            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            field.set(miuiNotification, number);// 设置信息数
            field = notification.getClass().getField("extraNotification");
            field.setAccessible(true);
            field.set(notification, miuiNotification);
            Toast.makeText(this, "Xiaomi=>isSendOk=>1", Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            e.printStackTrace();
            //miui 6之前的版本
            isMiUIV6 = false;
            Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra("android.intent.extra.update_application_component_name",getPackageName() + "/"+ lancherActivityClassName );
            localIntent.putExtra("android.intent.extra.update_application_message_text",number);
            sendBroadcast(localIntent);
        }
        finally
        {
            if(notification!=null && isMiUIV6 )
            {
                //miui6以上版本需要使用通知发送
                nm.notify(101010, notification);
            }
        }

    }

    private void sendToSony(String number) {
        boolean isShow = true;
        if ("0".equals(number)) {
            isShow = false;
        }
        Intent localIntent = new Intent();
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE",isShow);//是否显示
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",lancherActivityClassName );//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", number);//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME",getPackageName());//包名
        sendBroadcast(localIntent);

        Toast.makeText(this, "Sony," + "isSendOk", Toast.LENGTH_LONG).show();
    }

    private void sendToSamsumg(String number)
    {
        Intent localIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        localIntent.putExtra("badge_count", number);//数字
        localIntent.putExtra("badge_count_package_name", getPackageName());//包名
        localIntent.putExtra("badge_count_class_name",lancherActivityClassName ); //启动页
        sendBroadcast(localIntent);
        Toast.makeText(this, "Samsumg," + "isSendOk", Toast.LENGTH_LONG).show();
    }
}
