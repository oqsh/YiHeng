package com.y123456.yiheng;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;

public class ContactActivity extends AppCompatActivity {
    private static final String TAG = "ContactActicity";
    public ArrayList<Info> contact_list_data;
    public ListView lv;
    private String firstChar[] = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "m", "n", "o", "p", "q", "r",
            "s", "t", "u", "v", "w", "x", "y", "z"};
    private ArrayList<String> data = new ArrayList<>();
    private LinearLayout container;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dialpad:
                    Intent dp_intent = new Intent(ContactActivity.this, MainActivity.class);
                    startActivity(dp_intent);
                    return true;
                case R.id.navigation_contact:
                    return true;
                case R.id.navigation_lastcall:
                    // startActivity
                    return true;
                case R.id.navigation_myprofile:
                    Intent pa_intent = new Intent(ContactActivity.this, PreferenceActivity.class);
                    startActivity(pa_intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        info_list();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_contact); // 设置进入 Activity 时导航栏的默认选中按钮
    }

    @Override
    protected void onResume(){
        super.onResume();
        info_list();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_contact); // 设置进入 Activity 时导航栏的默认选中按钮
    }

    public void info_list() {
        contact_list_data = new ArrayList<Info>();

        String projection[] = new String[]{"id", "name"};
        String selection = "";
        String selectionArgs[] = new String[] {};
        String sortOrder = "firstChars";

        Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
        Cursor cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        int id;
        String name;
        while(cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            Info info = new Info(id, name);
            contact_list_data.add(info);
        }

        lv = (ListView) findViewById(R.id.contact_list);
        initLv();

        initNSV();
    }

    private void initNSV() {
        Collections.addAll(data, firstChar);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(0, 0, 0, 0);

        container = (LinearLayout) findViewById(R.id.firstChar);

        Button all = new Button(this);
        all.setText("#");
        all.setTextSize(16);
        all.setGravity(Gravity.CENTER);
        all.setPadding(0, 0, 0, 0);
        all.setHeight(60);
        all.setMinimumHeight(60);
        all.setWidth(45);
        all.setMinimumWidth(45);
        all.setBackgroundColor(Color.WHITE);
        all.setLayoutParams(layoutParams);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            contact_list_data = new ArrayList<Info>();

            String projection[] = new String[]{"id", "name"};
            String selection = "";
            String selectionArgs[] = new String[] {};
            String sortOrder = "firstChars";

            Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
            Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

            int id;
            String name;
            while(cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("id"));
                name = cursor.getString(cursor.getColumnIndex("name"));
                Info info = new Info(id, name);
                contact_list_data.add(info);
            }
            initLv();
            }
        });

        container.addView(all);
        container.invalidate();
        for(int i = 0; i < data.size(); i++) {
            Button button = new Button(this);
            button.setText(data.get(i));
            button.setTextSize(16);
            button.setGravity(Gravity.CENTER);
            button.setPadding(0, 0, 0, 0);
            button.setHeight(60);
            button.setMinimumHeight(60);
            button.setWidth(45);
            button.setMinimumWidth(45);
            button.setBackgroundColor(Color.WHITE);
            button.setLayoutParams(layoutParams);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                contact_list_data = new ArrayList<Info>();

                    String prefix = ((Button)v).getText().toString();
                    String projection[] = new String[]{"id", "name"};
                    String selection = "firstChars like ?";
                    String selectionArgs[] = new String[] {prefix+"%"};
                    String sortOrder = "firstChars";

                    Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
                    Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

                    int id;
                    String name;
                    while(cursor.moveToNext()) {
                        id = cursor.getInt(cursor.getColumnIndex("id"));
                        name = cursor.getString(cursor.getColumnIndex("name"));
                        Info info = new Info(id, name);
                        contact_list_data.add(info);
                    }
                    initLv();
                }
            });
            container.addView(button);
            container.invalidate();
        }
    }

    private void initLv() {
        ArrayList<String> name_list = new ArrayList<String>();
        for (int i = 0; i < contact_list_data.size(); i++)
            name_list.add(contact_list_data.get(i).getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ContactActivity.this, R.layout.list_item, R.id.InfoName, name_list);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int info_id = (int)contact_list_data.get(position).getId();
            String name = contact_list_data.get(position).getName();
            info_call(view, info_id);
            }
        });
    }

    public void add_contact(View view) {
        Intent intent = new Intent(ContactActivity.this, ContactAddActivity.class);
        startActivity(intent);
    }

    public void info_call(View view, int id) {
        Intent intent = new Intent(ContactActivity.this, ContactInfoActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void back(View view) {
        this.finish();
    }

}
