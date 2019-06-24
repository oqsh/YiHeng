package com.y123456.yiheng;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class PreferenceActivity extends AppCompatActivity {
    private static final String TAG = "PreferenceActicity";
    String name_text = "", tel_text = "", birthday_text = "";
    String NDstarttime_text = "", NDendtime_text = "";
    boolean is_notDistrub = false, is_birthRemind = false, is_specialDateRemind = false;
    int NDstarttime = 0, NDendtime = 0;

    EditText name, tel, birthday;
    EditText ET_NDstarttime, ET_NDendtime;
    TextView arrow;

    Switch NotDisturb, BirthRemind, SpecialDateRemind;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dialpad:
                    Intent dp_intent = new Intent(PreferenceActivity.this, MainActivity.class);
                    dp_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(dp_intent);
                    return true;
                case R.id.navigation_contact:
                    Intent ca_intent = new Intent(PreferenceActivity.this, ContactActivity.class);
                    ca_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(ca_intent);
                    return true;
                case R.id.navigation_lastcall:
                    Intent calllog_intent = new Intent(PreferenceActivity.this, CalllogActivity.class);
                    calllog_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(calllog_intent);
                    return true;
                case R.id.navigation_myprofile:
                    // startActivity
                    return true;
                case R.id.navigation_settings:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_activity);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_settings); // 设置进入 Activity 时导航栏的默认选中按钮
        initdata();

        name = (EditText) findViewById(R.id.name);
        tel = (EditText) findViewById(R.id.tel);
        birthday = (EditText) findViewById(R.id.birthday);

        name.setText(name_text);
        tel.setText(tel_text);
        birthday.setText(birthday_text);

        NotDisturb = (Switch)findViewById(R.id.NotDisturb);
        BirthRemind = (Switch)findViewById(R.id.BirthRemind);
        SpecialDateRemind = (Switch)findViewById(R.id.SpecialDateRemind);

        arrow = (TextView)findViewById(R.id.arrow);
        ET_NDstarttime = (EditText)findViewById(R.id.NDstarttime);
        ET_NDendtime = (EditText)findViewById(R.id.NDendtime);

        ET_NDstarttime.setText(NDstarttime_text);
        ET_NDendtime.setText(NDendtime_text);

        initNotDistrub();
        initBirthRemind();
        initSpecialDateRemind();

        Log.i(TAG, "name_text = " + name_text);
        Log.i(TAG, "tel_text = " + tel_text);
        Log.i(TAG, "birthday_text = " + birthday_text);
        Log.i(TAG, "NDstarttime = " + NDstarttime_text);
        Log.i(TAG, "NDendtime = "+ NDendtime_text);
        Log.i(TAG, "is_notDisturb = " + (is_notDistrub?"True":"False"));
        Log.i(TAG, "is_birthRemind = " + (is_birthRemind?"True":"False"));
        Log.i(TAG, "is_specialDateRemind = " + (is_specialDateRemind?"True":"False"));
    }

    private void initdata() {
        String projection[] = new String[]{"k", "v"};
        String selection = "";
        String selectionArgs[] = new String[] {};

        Uri uri = Uri.parse("content://com.y123456.yiheng.preference");
        Cursor cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs, "");

        String k, v;
        while(cursor.moveToNext()) {
            k = cursor.getString(cursor.getColumnIndex("k"));
            v = cursor.getString(cursor.getColumnIndex("v"));

            Log.i(TAG, "k = " + k);
            Log.i(TAG, "v = " + v);

            switch(k) {
                case "name":
                    name_text = v;
                    break;
                case "tel":
                    tel_text = v;
                    break;
                case "birthday":
                    birthday_text = v;
                    break;
                case "NotDistrub":
                    if(v.equals("true"))
                        is_notDistrub = true;
                    else if(v.equals("false"))
                        is_notDistrub = false;
                    break;
                case "BirthRemind":
                    if(v.equals("true"))
                        is_birthRemind = true;
                    else if(v.equals("false"))
                        is_birthRemind = false;
                    break;
                case "SpecialDateRemind":
                    if(v.equals("true"))
                        is_specialDateRemind = true;
                    else if(v.equals("false"))
                        is_specialDateRemind = false;
                    break;
                case "NDstarttime":
                    NDstarttime_text = v;
                    NDstarttime = calc_sec_time(NDstarttime_text);
                    break;
                case "NDendtime":
                    NDendtime_text = v;
                    NDendtime = calc_sec_time(NDendtime_text);
                    break;
            }
        }
    }

    private int calc_sec_time(String timestr) {
        if(timestr.length() != 5 || timestr.charAt(2) != ':')
            return 0;

        //timeStr format: HH:MM
        int time = (int)(timestr.charAt(0)-'0') * 36000
                + (int)(timestr.charAt(1)-'0') * 3600
                + (int)(timestr.charAt(3)-'0') * 600
                + (int)(timestr.charAt(4)-'0') * 60;
        Log.i(TAG, "timeStr = " + timestr);
        Log.i(TAG, "time = " + String.valueOf(time));
        if(time > 86400) time = 86400;
        return time;
    }

    private void initNotDistrub() {
        if(is_notDistrub == true) {
            NotDisturb.setChecked(true);
            arrow.setVisibility(View.VISIBLE);
            ET_NDstarttime.setVisibility(View.VISIBLE);
            ET_NDendtime.setVisibility(View.VISIBLE);
        }
        else {
            NotDisturb.setChecked(false);
            arrow.setVisibility(View.GONE);
            ET_NDstarttime.setVisibility(View.GONE);
            ET_NDendtime.setVisibility(View.GONE);
        }

        //监听事件
        NotDisturb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    is_notDistrub = true;

                    arrow.setVisibility(View.VISIBLE);
                    arrow.setText("->");
                    ET_NDstarttime.setVisibility(View.VISIBLE);
                    ET_NDendtime.setVisibility(View.VISIBLE);

                    ContentValues cv = new ContentValues();
                    cv.put("k", "NotDistrub");
                    cv.put("v", "true");

                    Uri uri = Uri.parse("content://com.y123456.yiheng.preference");
                    getContentResolver().insert(uri, cv);

                    Toast.makeText(PreferenceActivity.this, "打开勿扰模式", Toast.LENGTH_LONG).show();
                } else {
                    is_notDistrub = false;

                    arrow.setVisibility(View.GONE);
                    ET_NDstarttime.setVisibility(View.GONE);
                    ET_NDendtime.setVisibility(View.GONE);

                    ContentValues cv = new ContentValues();
                    cv.put("k", "NotDistrub");
                    cv.put("v", "false");

                    Uri uri = Uri.parse("content://com.y123456.yiheng.preference");
                    getContentResolver().insert(uri, cv);

                    Toast.makeText(PreferenceActivity.this, "关闭勿扰模式", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initBirthRemind() {
        if(is_birthRemind == true)
            BirthRemind.setChecked(true);
        else
            BirthRemind.setChecked(false);

        //监听事件
        BirthRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    is_birthRemind = true;

                    ContentValues cv = new ContentValues();
                    cv.put("k", "BirthRemind");
                    cv.put("v", "true");

                    Uri uri = Uri.parse("content://com.y123456.yiheng.preference");
                    getContentResolver().insert(uri, cv);

                    Toast.makeText(PreferenceActivity.this, "打开生日提醒", Toast.LENGTH_LONG).show();
                } else {
                    is_birthRemind = false;

                    ContentValues cv = new ContentValues();
                    cv.put("k", "BirthRemind");
                    cv.put("v", "false");

                    Uri uri = Uri.parse("content://com.y123456.yiheng.preference");
                    getContentResolver().insert(uri, cv);

                    Toast.makeText(PreferenceActivity.this, "关闭生日提醒", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initSpecialDateRemind() {
        if(is_specialDateRemind == true)
            SpecialDateRemind.setChecked(true);
        else
            SpecialDateRemind.setChecked(false);

        //监听事件
        SpecialDateRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    is_specialDateRemind = true;

                    ContentValues cv = new ContentValues();
                    cv.put("k", "SpecialDateRemind");
                    cv.put("v", "true");

                    Uri uri = Uri.parse("content://com.y123456.yiheng.preference");
                    getContentResolver().insert(uri, cv);

                    Toast.makeText(PreferenceActivity.this, "打开特殊日期提醒", Toast.LENGTH_LONG).show();
                } else {
                    is_specialDateRemind = false;

                    ContentValues cv = new ContentValues();
                    cv.put("k", "SpecialDateRemind");
                    cv.put("v", "false");

                    Uri uri = Uri.parse("content://com.y123456.yiheng.preference");
                    getContentResolver().insert(uri, cv);

                    Toast.makeText(PreferenceActivity.this, "关闭特殊日期提醒", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void save_preference(View view) {
        name_text = name.getText().toString();
        tel_text = tel.getText().toString();
        birthday_text = birthday.getText().toString();
        NDstarttime_text = ET_NDstarttime.getText().toString();
        NDendtime_text = ET_NDendtime.getText().toString();

        if(name_text.equals("")|| tel_text.equals("")|| birthday_text.equals("")) {
            Toast.makeText(this, "表单不可为空", Toast.LENGTH_LONG).show();
            return ;
        }

        if(birthday_text.length() != 10 || birthday_text.charAt(4) != '-' || birthday_text.charAt(7) != '-') {
            Toast.makeText(this, "生日格式应为XXXX-XX-XX", Toast.LENGTH_LONG).show();
            return ;
        }

        if(NDstarttime_text.length() != 5 || NDstarttime_text.charAt(2) != ':') {
            Toast.makeText(this, "起始时间格式应为HH:MM", Toast.LENGTH_LONG).show();
            return ;
        }

        if(NDendtime_text.length() != 5 || NDendtime_text.charAt(2) != ':') {
            Toast.makeText(this, "终止时间格式应为HH:MM", Toast.LENGTH_LONG).show();
            return ;
        }

        String keys[] = {"name", "tel", "birthday", "NotDisturb", "BirthRemind", "SpecialDateRemind", "NDstarttime", "NDendtime"};
        String values[] = {name_text, tel_text, birthday_text, "", "", "", NDstarttime_text, NDendtime_text};
        values[3] = is_notDistrub ? "true" : "false";
        values[4] = is_birthRemind ? "true" : "false";
        values[5] = is_specialDateRemind ? "true" : "false";

        Uri uri = Uri.parse("content://com.y123456.yiheng.preference");
        for(int i = 0; i < 8; i++) {
            ContentValues cv = new ContentValues();
            cv.put("k", keys[i]);
            cv.put("v", values[i]);
            getContentResolver().insert(uri, cv);
        }

        Toast.makeText(this, "成功保存个人信息", Toast.LENGTH_LONG).show();
//        this.finish();
    }

    public void toWhitelist(View view) {
        Intent intent = new Intent(PreferenceActivity.this, WhitelistActivity.class);
        startActivity(intent);
    }

    public void toBirthRemind(View view) {
        Intent intent = new Intent(PreferenceActivity.this, BirthRemindActivity.class);
        startActivity(intent);
    }

    public void toSpecialDateRemind(View view) {
        Intent intent = new Intent(PreferenceActivity.this, SpecialDateRemindActivity.class);
        startActivity(intent);
    }

    public void back(View view) {
        this.finish();
    }

}
