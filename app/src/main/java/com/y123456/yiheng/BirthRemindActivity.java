package com.y123456.yiheng;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BirthRemindActivity extends AppCompatActivity {
    private static final String TAG = "BirthRemindActicity";
    public ListView lv;
    public TextView title;
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbs_activity);

        title = (TextView)findViewById(R.id.title);
        title.setText("生日提醒名单");

        init_birthRemind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init_birthRemind();
    }

    private void init_birthRemind() {
        String projection[] = new String[]{"user_id", "name", "birthday"};
        String selection = "";
        String selectionArgs[] = new String[] {};
        String sortOrder = "birthday";

        Uri uri = Uri.parse("content://com.y123456.yiheng.birthRemind");
        Cursor cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        String name, birthday, display_text;
        data = new ArrayList<>();
        while(cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
            birthday = cursor.getString(cursor.getColumnIndex("birthday"));
            display_text = name + "(" + birthday + ")";
            data.add(display_text);
        }

        lv = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                BirthRemindActivity.this, R.layout.list_item, R.id.InfoText, data);

        lv.setAdapter(adapter);
    }

    public void admin(View view) {
        Intent intent = new Intent(BirthRemindActivity.this, BirthRemindAdminActivity.class);
        startActivity(intent);
    }

    public void back(View view) {
        this.finish();
    }

}