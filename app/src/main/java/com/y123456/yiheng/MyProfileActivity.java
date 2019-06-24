package com.y123456.yiheng;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: yzq
 * @date: 2017/10/26 15:17
 * @declare :
 */

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Button scanBtn;
    private TextView result;
    private TextView contentEt;
//    private Button encodeBtn;
//    private ImageView contentIv;
    private Toolbar toolbar;
    private Button addBtn;
    private int REQUEST_CODE_SCAN = 111;
    /**
     * 生成带logo的二维码
     */
    private Button encodeBtnWithLogo;
    private ImageView contentIvWithLogo;
    private String contentEtString;
    private String scan_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.my_profile_activity);
        initView();
    }


    private void initView() {
        /*扫描按钮*/
        scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);
        /*扫描结果*/
        result = findViewById(R.id.result);

        /*要生成二维码的输入框*/
        contentEt = findViewById(R.id.contentEt);
        /*生成按钮*/
//        encodeBtn = findViewById(R.id.encodeBtn);
//        encodeBtn.setOnClickListener(this);
        /*生成的图片*/
//        contentIv = findViewById(R.id.contentIv);

        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("个人名片");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String name, tel;

        String projection[] = new String[]{"k", "v"};
        String selection = "k = ?";
        String name_selectionArgs[] = new String[] {"name"};
        Uri uri = Uri.parse("content://com.y123456.yiheng.preference");
        Cursor cursor = this.getContentResolver().query(uri, projection, selection, name_selectionArgs, "");
        if(cursor.getCount() <= 0) {
            Intent mp_intent = new Intent(MyProfileActivity.this, PreferenceActivity.class);
            startActivity(mp_intent);
        }

        String tel_selectionArgs[] = new String[] {"tel"};
        cursor = this.getContentResolver().query(uri, projection, selection, tel_selectionArgs, "");
        if(cursor.getCount() <= 0) {
            Intent mp_intent = new Intent(MyProfileActivity.this, PreferenceActivity.class);
            mp_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(mp_intent);
        }

        cursor.moveToNext();
        name = cursor.getString(cursor.getColumnIndex("name"));
        tel = cursor.getString(cursor.getColumnIndex("tel"));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        result = (TextView) findViewById(R.id.result);
        scanBtn = (Button) findViewById(R.id.scanBtn);
        contentEt = (TextView) findViewById(R.id.contentEt);
        String a = name+"-" + tel;
        contentEt.setText(a);
//        contentEt.setText("hhh");
        encodeBtnWithLogo = (Button) findViewById(R.id.encodeBtnWithLogo);
        encodeBtnWithLogo.setOnClickListener(this);
        contentIvWithLogo = (ImageView) findViewById(R.id.contentIvWithLogo);
//        encodeBtn = (Button) findViewById(R.id.encodeBtn);
//        contentIv = (ImageView) findViewById(R.id.contentIv);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dialpad:
                    return true;
                case R.id.navigation_contact:
                    Intent ca_intent = new Intent(MyProfileActivity.this, ContactActivity.class);
                    ca_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(ca_intent);
                    return true;
                case R.id.navigation_lastcall:
                    // startActivity
                    return true;
                case R.id.navigation_myprofile:
                    Intent mp_intent = new Intent(MyProfileActivity.this, MyProfileActivity.class);
                    mp_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(mp_intent);
                    return true;
                case R.id.navigation_settings:
                    Intent se_intent = new Intent(MyProfileActivity.this, PreferenceActivity.class);
                    se_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(se_intent);
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onClick(View v) {
        Bitmap bitmap = null;
        switch (v.getId()) {
            case R.id.scanBtn:

                AndPermission.with(this)
                        .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Intent intent = new Intent(MyProfileActivity.this, CaptureActivity.class);
                                /*ZxingConfig是配置类
                                 *可以设置是否显示底部布局，闪光灯，相册，
                                 * 是否播放提示音  震动
                                 * 设置扫描框颜色等
                                 * 也可以不传这个参数
                                 * */
                                ZxingConfig config = new ZxingConfig();
                                // config.setPlayBeep(false);//是否播放扫描声音 默认为true
                                //  config.setShake(false);//是否震动  默认为true
                                // config.setDecodeBarCode(false);//是否扫描条形码 默认为true
//                                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
//                                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
//                                config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
                                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                                startActivityForResult(intent, REQUEST_CODE_SCAN);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Uri packageURI = Uri.parse("package:" + getPackageName());
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);

                                Toast.makeText(MyProfileActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                            }
                        }).start();

                break;
//            case R.id.encodeBtn:
//                contentEtString = contentEt.getText().toString().trim();
//                if (TextUtils.isEmpty(contentEtString)) {
//                    Toast.makeText(this, "请输入要生成二维码图片的字符串", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                bitmap = CodeCreator.createQRCode(contentEtString, 400, 400, null);
//                if (bitmap != null) {
//                    contentIv.setImageBitmap(bitmap);
//                }
//                break;

            case R.id.encodeBtnWithLogo:
                contentEtString = contentEt.getText().toString().trim();
                if (TextUtils.isEmpty(contentEtString)) {
                    Toast.makeText(this, "请在个人信息中设置本机的使用者姓名及手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                bitmap = CodeCreator.createQRCode(contentEtString, 400, 400, logo);
                if (bitmap != null) {
                    contentIvWithLogo.setImageBitmap(bitmap);
                }
                break;

            case R.id.addBtn:
                String delimeter = "-";  // 指定分割字符
                String[] temp;
                temp = scan_res.split(delimeter); // 分割字符串

                String name_text = temp[0];
                String tel_text = temp[1];
                ContentValues cv = new ContentValues();
                cv.put("name", name_text);
                cv.put("tel", tel_text);
                cv.put("birthday", "1111-11-11");
                Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
                this.getContentResolver().insert(uri, cv);
                Toast.makeText(this, "成功添加联系人", Toast.LENGTH_LONG).show();
                break;


            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                addBtn.setVisibility(View.VISIBLE);

                scan_res = content;
//                result.setText("扫描结果为：" + content);
            }
        }
    }
}
