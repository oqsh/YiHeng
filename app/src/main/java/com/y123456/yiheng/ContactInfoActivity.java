package com.y123456.yiheng;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.net.Uri;
import android.database.Cursor;
import android.widget.Toast;

public class ContactInfoActivity extends AppCompatActivity {
    private static final String TAG = "ContactInfoActivity";
    Switch whitelist, birthRemind;
    TextView name, tel, birthday;
    int id, in_whitelist, in_birthRemind;
    String name_text, tel_text, birthday_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info_activity);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        Log.i("contactInfoActivity", String.valueOf(id));

        String projection[] = new String[]{"name", "tel", "birthday", "in_whitelist", "in_birthRemind"};
        String selection = "id = ?";
        String selectionArgs[] = new String[] {String.valueOf(id)};

        Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
        Cursor info = this.getContentResolver().query(uri, projection, selection, selectionArgs, "");

        if (info.getCount() > 0) {
            info.moveToLast();
            name_text = info.getString(info.getColumnIndex("name"));
            tel_text = info.getString(info.getColumnIndex("tel"));
            birthday_text = info.getString(info.getColumnIndex("birthday"));
            in_whitelist = info.getInt(info.getColumnIndex("in_whitelist"));
            in_birthRemind = info.getInt(info.getColumnIndex("in_birthRemind"));
        }

        name = (TextView)findViewById(R.id.name);
        tel = (TextView)findViewById(R.id.tel);
        birthday = (TextView)findViewById(R.id.birthday);

        name.setText(name_text);
        tel.setText(tel_text);
        birthday.setText(birthday_text);

        whitelist = (Switch)findViewById(R.id.whitelist);
        birthRemind = (Switch)findViewById(R.id.birthRemind);

        //设置状态以及监听事件
        initWhitelist();
        initBirthRemind();
    }

    protected void initWhitelist() {
        if(in_whitelist == 1)
            whitelist.setChecked(true);
        else
            whitelist.setChecked(false);

        //whitelist的监听事件
        whitelist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    in_whitelist = 1;
                    //修改contact table
                    ContentValues cv = new ContentValues();
                    cv.put("in_whitelist", in_whitelist);

                    String selection = "id = ?";
                    String selectionArgs[] = new String[] {String.valueOf(id)};

                    Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
                    getContentResolver().update(uri, cv, selection, selectionArgs);

                    //修改whitelist table
                    cv = new ContentValues();
                    cv.put("user_id", id);
                    cv.put("name", name_text);
                    cv.put("tel", tel_text);

                    uri = Uri.parse("content://com.y123456.yiheng.whitelist");
                    getContentResolver().insert(uri, cv);

                    Toast.makeText(ContactInfoActivity.this, "成功加入白名单", Toast.LENGTH_LONG).show();
                } else {
                    in_whitelist = 0;
                    //修改contact table
                    ContentValues cv = new ContentValues();
                    cv.put("in_whitelist", in_whitelist);

                    String selection = "id = ?";
                    String selectionArgs[] = new String[] {String.valueOf(id)};

                    Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
                    getContentResolver().update(uri, cv, selection, selectionArgs);

                    //修改whitelist table
                    selection = "user_id = ?";

                    uri = Uri.parse("content://com.y123456.yiheng.whitelist");
                    getContentResolver().delete(uri, selection, selectionArgs);

                    Toast.makeText(ContactInfoActivity.this, "成功移出白名单", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void initBirthRemind() {
        if(in_birthRemind == 1)
            birthRemind.setChecked(true);
        else
            birthRemind.setChecked(false);

        //birthRemind的监听事件
        birthRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    in_birthRemind = 1;
                    //修改contact table
                    ContentValues cv = new ContentValues();
                    cv.put("in_birthRemind", in_birthRemind);

                    String selection = "id = ?";
                    String selectionArgs[] = new String[] {String.valueOf(id)};

                    Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
                    getContentResolver().update(uri, cv, selection, selectionArgs);

                    //修改birthRemind table
                    cv = new ContentValues();
                    cv.put("user_id", id);
                    cv.put("name", name_text);
                    cv.put("birthday", birthday_text);

                    uri = Uri.parse("content://com.y123456.yiheng.birthRemind");
                    getContentResolver().insert(uri, cv);

                    Toast.makeText(ContactInfoActivity.this, "成功设置生日提醒", Toast.LENGTH_LONG).show();
                } else {
                    in_birthRemind = 0;

                    //修改contact table
                    ContentValues cv = new ContentValues();
                    cv.put("in_birthRemind", in_birthRemind);

                    String selection = "id = ?";
                    String selectionArgs[] = new String[] {String.valueOf(id)};

                    Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
                    getContentResolver().update(uri, cv, selection, selectionArgs);

                    //修改birthRemind table
                    selection = "user_id = ?";

                    uri = Uri.parse("content://com.y123456.yiheng.birthRemind");
                    getContentResolver().delete(uri, selection, selectionArgs);

                    Toast.makeText(ContactInfoActivity.this, "成功取消生日提醒", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        String projection[] = new String[]{"name", "tel", "birthday"};
        String selection = "id = ?";
        String selectionArgs[] = new String[] {String.valueOf(id)};

        Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
        Cursor info = this.getContentResolver().query(uri, projection, selection, selectionArgs, "");

        if (info.getCount() > 0) {
            info.moveToLast();
            name_text = info.getString(info.getColumnIndex("name"));
            tel_text = info.getString(info.getColumnIndex("tel"));
            birthday_text = info.getString(info.getColumnIndex("birthday"));
        }

        name.setText(name_text);
        tel.setText(tel_text);
        birthday.setText(birthday_text);
    }

    public void edit_contact(View view) {
        Intent intent = new Intent(ContactInfoActivity.this, ContactEditActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name_text);
        intent.putExtra("tel", tel_text);
        intent.putExtra("birthday", birthday_text);
        startActivity(intent);
    }

    public void delete(View view) {
        String selection = "id = ?";
        String selectionArgs[] = new String[] {String.valueOf(id)};

        Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
        int exit_code = this.getContentResolver().delete(uri, selection, selectionArgs);
        if(exit_code == 0)
            Toast.makeText(this, "删除联系人失败", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "成功删除联系人", Toast.LENGTH_LONG).show();
        this.finish();
    }

    public void back(View view) {
        this.finish();
    }

}
