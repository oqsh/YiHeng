package com.y123456.yiheng;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.icu.text.DateTimePatternGenerator.DAY;

public class StatActivity extends AppCompatActivity {
    private ListView listView;
    private List<Map<String, String>> dataList;
    private ContentResolver resolver;
    private Uri callUri = CallLog.Calls.CONTENT_URI;
    private String[] columns = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型}
    private StatAdapter adapter;
    private String mobile;//被授权人电话号码
    private  Button call_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        initView();
        call_button = (Button) findViewById(R.id.calllog_button);
        call_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(StatActivity.this, CalllogActivity.class));
            }
        });
        getPersimmionInfo();
    }

    protected void onResume(){
        super.onResume();
        getPersimmionInfo();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.statWords);
    }

    //授权信息
    private void getPersimmionInfo() {
        if (Build.VERSION.SDK_INT >= 23) {
            //1. 检测是否添加权限   PERMISSION_GRANTED  表示已经授权并可以使用
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                //手机为Android6.0的版本,未授权则动态请求授权
                //2. 申请请求授权权限
                //1. Activity
                // 2. 申请的权限名称
                // 3. 申请权限的 请求码
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.READ_CALL_LOG//通话记录
                        }, 1005);
            } else {//手机为Android6.0的版本,权限已授权可以使用
                // 执行下一步
                initContacts();
            }
        } else {//手机为Android6.0以前的版本，可以使用
            initContacts();
        }

    }

    private void initContacts() {
        dataList = getDataList();
        adapter = new StatAdapter(this, dataList);
        listView.setAdapter(adapter);
    }

    /**
     * 读取数据
     *
     * @return 读取到的数据
     */
    private List<Map<String, String>> getDataList() {
        // 1.获得ContentResolver
        resolver = getContentResolver();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
        }
        // 2.利用ContentResolver的query方法查询通话记录数据库
        /**
         * @param uri 需要查询的URI，（这个URI是ContentProvider提供的）
         * @param projection 需要查询的字段
         * @param selection sql语句where之后的语句
         * @param selectionArgs ?占位符代表的数据
         * @param sortOrder 排序方式
         */
        Cursor cursor = resolver.query(callUri, // 查询通话记录的URI
                columns
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        // 3.通过Cursor获得数据
        List<Map<String, String>> list = new ArrayList<>();
        if (cursor == null) return list;
        int num_day=0,time_day=0;
        int num_week=0,time_week=0;
        int num_month=0,time_month=0;
        int num_year=0,time_year=0;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            String day = new SimpleDateFormat("MM-dd           ").format(new Date(dateLong));
            String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            Date dayCurrent = new Date();
            Date dayRecord = new Date(dateLong);
            String dayP = new SimpleDateFormat("yyyy-MM-dd").format(new Date(dateLong));
            String typeString = "";
            long dif = getTimeDistance(dayRecord, dayCurrent)/(3600*24*1000);
            //Log.e(dif+"","差距");
            if (dif < 1) {
                //一天内
                num_day = num_day + 1;
                time_day = time_day + duration;
            }
            if (dif < 7) {
                //一个星期内
                num_week = num_week + 1;
                time_week = time_week + duration;
            }
            if (dif < 30) {
                //一个月内
                num_month = num_month + 1;
                time_month = time_month + duration;
            }
            if (dif < 365) {
                //一年内
                num_year = num_year + 1;
                time_year = time_year + duration;
            }
        }
        Log.e(num_day+"","hhh日期");
        Log.e(time_day+"111","hhh时长");
        Log.e(num_week+"","hhh日期");
        Log.e(time_week+"111","hhh时长");
        Log.e(num_month+"","hhh日期");
        Log.e(time_month+"111","hhh时长");
        Log.e(num_year+"","hhh日期");
        Log.e(time_year+"111","hhh时长");
        Map<String, String> map1 = new HashMap<>();
        //一天内的情况
        map1.put("name", "一天内 ");//通话时间段
        map1.put("number", num_day+"");//通话次数
        map1.put("time", time_day+"");//通话时长
        list.add(map1);
        Map<String, String> map2 = new HashMap<>();
        //一星期内的情况
        map2.put("name", "一星期内");//通话时间段
        map2.put("number", num_week+"");//通话次数
        map2.put("time", time_week+"");//通话时长
        list.add(map2);
        Map<String, String> map3 = new HashMap<>();
        //一个月内的情况
        map3.put("name", "一个月内");//通话时间段
        map3.put("number", num_month+"");//通话次数
        map3.put("time", time_month+"");//通话时长
        list.add(map3);
        Map<String, String> map4 = new HashMap<>();
        //一年内的情况
        map4.put("name", "一年内 ");//通话时间段
        map4.put("number", num_year+"");//通话次数
        map4.put("time", time_year+"");//通话时长
        list.add(map4);

        return list;
    }

    /**
     * 获得两个日期间距多少天
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long getTimeDistance(Date beginDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(beginDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
        fromCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
        fromCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
        fromCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
        toCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
        toCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
        toCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));

        long dayDistance = (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime());
        dayDistance = Math.abs(dayDistance);

        return dayDistance;
    }

}

