package com.y123456.yiheng;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class BirthRemindAdminActivity extends AppCompatActivity {
    private static final String TAG = "BRAActicity";
    public EditText name_birthday;
    public TextView title, hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbs_admin_activity);

        title = (TextView)findViewById(R.id.title);
        hint = (TextView)findViewById(R.id.hint);

        title.setText("管理生日提醒名单");
        hint.setText("请输入联系人姓名+生日");

        name_birthday = (EditText)findViewById(R.id.text);
        name_birthday.setHint("NF: name+xxxx-xx-xx");
    }

    public void add(View view) {
        String name_birthday_text = name_birthday.getText().toString();
        String name_text = name_birthday_text.substring(0, name_birthday_text.length()-11);
        String birthday_text = name_birthday_text.substring(name_birthday_text.length()-10);

        Log.i(TAG, "name_text = "+name_text);
        Log.i(TAG, "birthday_text = "+birthday_text);

        String projection[] = new String[]{"id"};
        String selection = "name = ? and birthday = ?";
        String selectionArgs[] = new String[] {name_text, birthday_text};

        Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
        Cursor cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs, "");

        //通讯录中有该电话对应的联系人信息
        if(cursor.getCount() > 0) {
            cursor.moveToLast();
            int user_id = cursor.getInt(cursor.getColumnIndex("id"));
            ContentValues cv = new ContentValues();
            cv.put("user_id", user_id);
            cv.put("name", name_text);
            cv.put("birthday", birthday_text);

            uri = Uri.parse("content://com.y123456.yiheng.birthRemind");
            this.getContentResolver().insert(uri, cv);

            //修改contact table
            cv = new ContentValues();
            cv.put("in_birthRemind", 1);

            selection = "id = ?";
            selectionArgs = new String[] {String.valueOf(user_id)};

            uri = Uri.parse("content://com.y123456.yiheng.contact");
            this.getContentResolver().update(uri, cv, selection, selectionArgs);

            Toast.makeText(this, "成功将联系人 "+name_text+" 设置为生日提醒", Toast.LENGTH_LONG).show();
            this.finish();
        }
        else {
            Toast.makeText(this, "通讯录中无该联系人，添加生日提醒失败", Toast.LENGTH_LONG).show();
        }

    }

    public void remove(View view) {
        String name_birthday_text = name_birthday.getText().toString();
        String name_text = name_birthday_text.substring(0, name_birthday_text.length()-11);
        String birthday_text = name_birthday_text.substring(name_birthday_text.length()-10);

        Log.i(TAG, "name_text = "+name_text);
        Log.i(TAG, "birthday_text = "+birthday_text);

        String projection[] = new String[]{"id"};
        String selection = "name = ? and birthday = ?";
        String selectionArgs[] = new String[] {name_text, birthday_text};

        Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
        Cursor cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs, "");

        //通讯录中有该电话对应的联系人信息
        if(cursor.getCount() > 0) {
            cursor.moveToLast();
            int user_id = cursor.getInt(cursor.getColumnIndex("id"));

            selection = "user_id = ?";
            selectionArgs = new String[] {String.valueOf(user_id)};

            uri = Uri.parse("content://com.y123456.yiheng.birthRemind");
            this.getContentResolver().delete(uri, selection, selectionArgs);

            //修改contact table
            ContentValues cv = new ContentValues();
            cv.put("in_birthRemind", 0);

            selection = "id = ?";
            selectionArgs = new String[] {String.valueOf(user_id)};

            uri = Uri.parse("content://com.y123456.yiheng.contact");
            this.getContentResolver().update(uri, cv, selection, selectionArgs);

            Toast.makeText(this, "成功将联系人 "+name_text+" 取消生日提醒", Toast.LENGTH_LONG).show();
            this.finish();
        }
        else {
            Toast.makeText(this, "通讯录中无该联系人，取消生日提醒失败", Toast.LENGTH_LONG).show();
        }

    }
}
