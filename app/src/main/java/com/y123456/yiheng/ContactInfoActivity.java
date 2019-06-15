package com.y123456.yiheng;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.net.Uri;
import android.database.Cursor;
import android.widget.Toast;

public class ContactInfoActivity extends AppCompatActivity {
    Button back, edit;
    TextView name, tel, birthday;
    ContactContentProvider ccp;
    int id;
    String name_text, tel_text, birthday_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info_activity);
        ccp = new ContactContentProvider();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        Log.i("contactInfoActivity", String.valueOf(id));

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

        name = (TextView)findViewById(R.id.name);
        tel = (TextView)findViewById(R.id.tel);
        birthday = (TextView)findViewById(R.id.birthday);

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

    public void back(View view) {
        this.finish();
    }

}
