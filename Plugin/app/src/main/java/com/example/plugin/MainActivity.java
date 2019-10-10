package com.example.plugin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 0x05;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 0x06;
    private static final int IMAGE_NEED_CUT = 0x07;

    private TextView text;
    private Context context;

    private Button btnAlbum;
    private Button btnCamera;
    private ImageView photoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getBaseContext();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        Environment.getExternalStorageDirectory().getAbsolutePath();
        text = (TextView) findViewById(R.id.text);

        text.setText(context.getCacheDir().getAbsolutePath());

        // 相册操作
        btnAlbum = (Button) findViewById(R.id.btn_album);
        btnCamera = (Button) findViewById(R.id.btn_camera);
        photoImage = (ImageView) findViewById(R.id.img_avtor);
    }

    private void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // 相片类型
        startActivityForResult(intent,
                REQUEST_CODE_PICK_IMAGE);
    }

    private void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent(
                    "android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera,
                    REQUEST_CODE_CAPTURE_CAMEIA);
        }
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, IMAGE_NEED_CUT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Uri uri = null;
        if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            startPhotoZoom(data.getData());
        } else if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            File temp = new File(Environment.getExternalStorageDirectory() + "/Witon/Image" + "/picture.jpg");
            startPhotoZoom(Uri.fromFile(temp));
        } else if (requestCode == IMAGE_NEED_CUT) {
            uri = data.getData();
        }
        if (uri != null) {
            photoImage.setImageURI(uri);
        }
        // 下面是电话号码处理
        /*if (resultCode == RESULT_OK) {
            final Uri contactUri = data.getData();

            @SuppressWarnings("deprecation") Cursor cursor = managedQuery(contactUri, null, null, null, null);
            cursor.moveToFirst();
            // 获取手机号码的代码
            String phoneNum = "";
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phoneCursor = getContentResolver().query(Phone.CONTENT_URI, null,
                    Phone.CONTACT_ID + " = " + contactId, null, null);

            HashMap<String, String> map = new HashMap<String, String>();
            // 将取出的号码放入map容器里，这里的号码如果有重复的，则号码只保存一个
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER))
                        .replaceAll("-", "").replaceAll(" ", "");
                map.put(phoneNumber, phoneNumber);
            }
            // 将号码放入Iterator中，并读取出来
            Iterator<String> iter = map.keySet().iterator();
            while (iter.hasNext()) {
                phoneNum += ";" + iter.next();
                if (phoneNum.contains("+86")) {
                    phoneNum = phoneNum.replace("+86", "");
                }
            }
            phoneNum = phoneNum.replaceFirst(";", "");
            if (phoneNum.contains(";")) {
                String[] phoneArray = phoneNum.split(";");
                showSelectDialog(phoneArray);
            } else {
                phoneEditView.setText(phoneNum);
            }
            return;
        }*/
    }

    public PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
