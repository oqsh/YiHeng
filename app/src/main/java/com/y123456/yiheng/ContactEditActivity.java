package com.y123456.yiheng;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.net.Uri;
import android.util.Log;

import com.y123456.yiheng.HanziToPinyin.Token;

import java.util.ArrayList;

public class ContactEditActivity extends AppCompatActivity {
    private static final String TAG = "ContactEditActivity";
    EditText name, tel, birthday;
    ContactContentProvider ccp;
    int id;
    String name_text, tel_text, birthday_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit_activity);

        name = (EditText)findViewById(R.id.name);
        tel = (EditText)findViewById(R.id.tel);
        birthday = (EditText)findViewById(R.id.birthday);

        //从contactInfoActivity获取数据，考虑到数据量较少因此不采用传主键查数据库的方式
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name_text = intent.getStringExtra("name");
        tel_text = intent.getStringExtra("tel");
        birthday_text = intent.getStringExtra("birthday");

        name.setText(name_text);
        tel.setText(tel_text);
        birthday.setText(birthday_text);
    }

    public String getFirstChars(String source){
        ArrayList<Token> tokens = HanziToPinyin.getInstance().get(source);
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0) {
            boolean firstName = true;
            for (Token token : tokens) {
                if (Token.PINYIN == token.type) {
                    if(firstName) {
                        sb.append(token.target);
                        firstName = false;
                    }
                    else {
                        sb.append(token.target.charAt(0));
                    }
                } else {
                    sb.append(token.source);
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    public void edit_contact(View view) {
        ContentValues cv = new ContentValues();
        String name_text = name.getText().toString();
        cv.put("name", name_text);
        cv.put("firstChars", getFirstChars(name_text));
        cv.put("tel", tel.getText().toString());
        cv.put("birthday", birthday.getText().toString());

        if(name_text.equals("")|| tel_text.equals("")|| birthday_text.equals("")) {
            Toast.makeText(this, "表单不可为空", Toast.LENGTH_LONG).show();
            return ;
        }

        if(birthday_text.length() != 10 || birthday_text.charAt(4) != '-' || birthday_text.charAt(7) != '-') {
            Toast.makeText(this, "生日格式应为XXXX-XX-XX", Toast.LENGTH_LONG).show();
            return ;
        }

        String selection = "id = ?";
        String selectionArgs[] = new String[] {String.valueOf(id)};

        Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
        int exit_code = this.getContentResolver().update(uri, cv, selection, selectionArgs);
        if(exit_code == 0)
            Toast.makeText(this, "修改联系人信息失败", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "成功修改联系人信息", Toast.LENGTH_LONG).show();

        this.finish();
    }

    public void back(View view) {
        this.finish();
    }

}
