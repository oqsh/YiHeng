package com.y123456.yiheng;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bstar, bdash;
    ImageButton iCall, iBack;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dialpad:
                    return true;
                case R.id.navigation_contact:
                    // startActivity
                    return true;
                case R.id.navigation_lastcall:
                    // startActivity
                    return true;
                case R.id.navigation_myprofile:
                    // startActivity
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_dialpad); // 设置进入 Activity 时导航栏的默认选中按钮
        bindOnClickListeners();
    }

    private void bindOnClickListeners() {
        textView = findViewById(R.id.textView);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);
        b6 = findViewById(R.id.b6);
        b7 = findViewById(R.id.b7);
        b8 = findViewById(R.id.b8);
        b9 = findViewById(R.id.b9);
        b0 = findViewById(R.id.b0);
        bstar = findViewById(R.id.bstar);
        bdash = findViewById(R.id.bdash);
        iCall = findViewById(R.id.callButton);
        iBack = findViewById(R.id.backspaceButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '1';
                textView.setText(newText);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '2';
                textView.setText(newText);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '3';
                textView.setText(newText);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '4';
                textView.setText(newText);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '5';
                textView.setText(newText);
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '6';
                textView.setText(newText);
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '7';
                textView.setText(newText);
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '8';
                textView.setText(newText);
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '9';
                textView.setText(newText);
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '0';
                textView.setText(newText);
            }
        });
        bstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '*';
                textView.setText(newText);
            }
        });
        bdash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString() + '#';
                textView.setText(newText);
            }
        });
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString();
                if (newText.length() > 0) {
                    newText = newText.substring(0, newText.length() - 1);
                }
                textView.setText(newText);
            }
        });
        iCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = textView.getText().toString();
                if (newText.length() > 0) {
                    Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + newText));
                    startActivity(intent);
                }
            }
        });
    }

}
