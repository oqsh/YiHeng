package com.y123456.yiheng;

import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Bundle;
import android.database.Cursor;
import android.view.View;
import android.net.Uri;
import android.content.Intent;
import android.widget.TextView;

import java.util.ArrayList;

public class WhitelistActivity extends AppCompatActivity {
    private static final String TAG = "WhitelistActicity";
    public ListView lv;
    private ArrayList<String> data;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbs_activity);

        title = (TextView)findViewById(R.id.title);
        title.setText("白名单");

        init_whitelist();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init_whitelist();
    }

    private void init_whitelist() {
        String projection[] = new String[]{"user_id", "name", "tel"};
        String selection = "";
        String selectionArgs[] = new String[] {};
        String sortOrder = "name";

        Uri uri = Uri.parse("content://com.y123456.yiheng.whitelist");
        Cursor cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        String name, tel, display_text;
        data = new ArrayList<>();
        while(cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
            tel = cursor.getString(cursor.getColumnIndex("tel"));
            display_text = name + "(" + tel + ")"; 
            data.add(display_text);
        }

        lv = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                WhitelistActivity.this, R.layout.list_item, R.id.InfoText, data);

        lv.setAdapter(adapter);
    }

    public void admin(View view) {
        Intent intent = new Intent(WhitelistActivity.this, WhitelistAdminActivity.class);
        startActivity(intent);
    }

    public void back(View view) {
        this.finish();
    }
    
}