package com.y123456.yiheng;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.os.Bundle;
import android.database.Cursor;
import android.view.View;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

public class WhitelistAdminActivity extends AppCompatActivity {
    private static final String TAG = "WhitelistAdminActicity";
    public EditText tel;
    TextView title, hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbs_admin_activity);

        title = (TextView)findViewById(R.id.title);
        hint = (TextView)findViewById(R.id.hint);

        title.setText("管理白名单");
        hint.setText("请输入要管理的手机号");

        tel = (EditText)findViewById(R.id.text);
    }

    public void add(View view) {
        String tel_text = tel.getText().toString();

        String projection[] = new String[]{"id", "name"};
        String selection = "tel = ?";
        String selectionArgs[] = new String[] {tel_text};

        Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
        Cursor cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs, "");

        //通讯录中有该电话对应的联系人信息
        if(cursor.getCount() > 0) {
            cursor.moveToLast();
            int user_id = cursor.getInt(cursor.getColumnIndex("id"));
            String name_text = cursor.getString(cursor.getColumnIndex("name"));
            ContentValues cv = new ContentValues();
            cv.put("user_id", user_id);
            cv.put("name", name_text);
            cv.put("tel", tel_text);

            uri = Uri.parse("content://com.y123456.yiheng.whitelist");
            this.getContentResolver().insert(uri, cv);

            //修改contact table
            cv = new ContentValues();
            cv.put("in_whitelist", 1);

            selection = "id = ?";
            selectionArgs = new String[] {String.valueOf(user_id)};

            uri = Uri.parse("content://com.y123456.yiheng.contact");
            this.getContentResolver().update(uri, cv, selection, selectionArgs);

            Toast.makeText(this, "成功将联系人 "+name_text+" 添加至白名单", Toast.LENGTH_LONG).show();
            this.finish();
        }
        else {
            Toast.makeText(this, "通讯录中无该联系人，添加失败", Toast.LENGTH_LONG).show();
        }

    }

    public void remove(View view) {
        String tel_text = tel.getText().toString();

        String projection[] = new String[]{"id", "name"};
        String selection = "tel = ?";
        String selectionArgs[] = new String[] {tel_text};

        Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
        Cursor cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs, "");

        //通讯录中有该电话对应的联系人信息
        if(cursor.getCount() > 0) {
            cursor.moveToLast();
            int user_id = cursor.getInt(cursor.getColumnIndex("id"));
            String name_text = cursor.getString(cursor.getColumnIndex("name"));

            selection = "user_id = ?";
            selectionArgs = new String[] {String.valueOf(user_id)};

            uri = Uri.parse("content://com.y123456.yiheng.whitelist");
            this.getContentResolver().delete(uri, selection, selectionArgs);

            //修改contact table
            ContentValues cv = new ContentValues();
            cv.put("in_whitelist", 0);

            selection = "id = ?";
            selectionArgs = new String[] {String.valueOf(user_id)};

            uri = Uri.parse("content://com.y123456.yiheng.contact");
            this.getContentResolver().update(uri, cv, selection, selectionArgs);

            Toast.makeText(this, "成功将联系人 "+name_text+" 移出白名单", Toast.LENGTH_LONG).show();
            this.finish();
        }
        else {
            Toast.makeText(this, "通讯录中无该联系人，删除失败", Toast.LENGTH_LONG).show();
        }

    }
}
