package com.y123456.yiheng;

import java.util.ArrayList;

import com.y123456.yiheng.HanziToPinyin.Token;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.util.Log;

public class ContactAddActivity extends AppCompatActivity {
    Button back, add;
    EditText name, tel, birthday;
    String name_text, firstChars, tel_text, birthday_text;
    ContactContentProvider ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_add_activity);
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

    public void add_contact(View view) {
        name = (EditText)findViewById(R.id.name);
        tel = (EditText)findViewById(R.id.tel);
        birthday = (EditText)findViewById(R.id.birthday);

        name_text = name.getText().toString();
        firstChars = getFirstChars(name_text);
        tel_text = tel.getText().toString();
        birthday_text = birthday.getText().toString();

        if(name_text.equals("")|| tel_text.equals("")|| birthday_text.equals("")) {
            Toast.makeText(this, "表单不可为空", Toast.LENGTH_LONG).show();
            return ;
        }

        if(birthday_text.length() != 10 || birthday_text.charAt(4) != '-' || birthday_text.charAt(7) != '-') {
            Toast.makeText(this, "生日格式应为XXXX-XX-XX", Toast.LENGTH_LONG).show();
            return ;
        }

        ContentValues cv = new ContentValues();
        cv.put("name", name_text);
        cv.put("firstChars", firstChars);
        cv.put("tel", tel_text);
        cv.put("birthday", birthday_text);

        Uri uri = Uri.parse("content://com.y123456.yiheng.contact");
        this.getContentResolver().insert(uri, cv);
        Toast.makeText(this, "成功添加联系人", Toast.LENGTH_LONG).show();

        this.finish();
    }

    public void back(View view) {
        this.finish();
    }

}
