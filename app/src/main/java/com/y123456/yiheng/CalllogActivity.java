package com.y123456.yiheng;

import android.Manifest;
import android.content.ContentResolver;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Intent;
import static android.icu.text.DateTimePatternGenerator.DAY;

public class CalllogActivity extends AppCompatActivity {
    private ListView listView;
    private List<Map<String, String>> dataList;
    private ContentResolver resolver;
    private Uri callUri = CallLog.Calls.CONTENT_URI;
    private String[] columns = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型}
    private CalllogAdapter adapter;
    private String mobile;//被授权人电话号码
    private Button stat_button;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dialpad:
                    Intent dp_intent = new Intent(CalllogActivity.this, MainActivity.class);
                    startActivity(dp_intent);
                    return true;
                case R.id.navigation_contact:
                    Intent ca_intent = new Intent(CalllogActivity.this, ContactActivity.class);
                    startActivity(ca_intent);
                    return true;
                case R.id.navigation_lastcall:
                    return true;
                case R.id.navigation_myprofile:
                    // startActivity
                    return true;
                case R.id.navigation_settings:
                    Intent pa_intent = new Intent(CalllogActivity.this, PreferenceActivity.class);
                    startActivity(pa_intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calllog);
        initView();
        stat_button = (Button) findViewById(R.id.stat_button);
        stat_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(CalllogActivity.this, StatActivity.class));
            }
        });
        getPersimmionInfo();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_lastcall); // 设置进入 Activity 时导航栏的默认选中按钮
    }

    protected void onResume(){
        super.onResume();
        getPersimmionInfo();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_lastcall); // 设置进入 Activity 时导航栏的默认选中按钮
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_view);
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
        adapter = new CalllogAdapter(this, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mobile = dataList.get(position).get("number")
                        .replaceAll(" ", "")
                        .replaceAll("\\+", "")
                        .replaceAll("-", "");
                String nameStr = dataList.get(position).get("name");
                Toast.makeText(CalllogActivity.this, mobile + " " + nameStr, Toast.LENGTH_SHORT).show();
            }
        });
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
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            String day = new SimpleDateFormat("MM-dd           ").format(new Date(dateLong));
            String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            String dayCurrent = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String dayRecord = new SimpleDateFormat("yyyyMMdd").format(new Date(dateLong));
            String dayP = new SimpleDateFormat("yyyy-MM-dd").format(new Date(dateLong));
            String typeString = "";
            switch (type) {
                case CallLog.Calls.INCOMING_TYPE:
                    //"打入"
                    typeString = "打入";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    //"打出"
                    typeString = "打出";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    //"未接"
                    typeString = "未接";
                    break;
                default:
                    break;
            }
            if (true) {
                String dayString = "";
                if ((Integer.parseInt(dayCurrent)) == (Integer.parseInt(dayRecord))) {
                    //今天
                    dayString = "今天              ";
                } else if ((Integer.parseInt(dayCurrent) - 1) == (Integer.parseInt(dayRecord))) {
                    //昨天
                    dayString = "昨天              ";
                } else if(Integer.parseInt(dayCurrent)/10000 == (Integer.parseInt(dayRecord))/10000) {
                    dayString = day;
                }
                else{
                    dayString = dayP;
                }
                long day_lead =getTimeDistance(new Date(dateLong),new Date());
                if (true) {//只显示48小时以内通话记录，防止通话记录数据过多影响加载速度
                    Map<String, String> map = new HashMap<>();
                    //"未备注联系人"
                    map.put("name", (name == null) ? "未备注联系人" : name);//姓名
                    map.put("number", number);//手机号
                    map.put("date", date);//通话日期
                    // "分钟"
                    if(duration<1)
                        map.put("duration", "未接通");//时长
                    else if(duration / 60<1)
                        map.put("duration", duration + "秒");//时长
                    else if(duration / 60<60)
                        map.put("duration", (duration / 60) + "分" + duration%60 + "秒");//时长
                    else
                        map.put("duration", (duration / 3600) + "小时" + (duration % 3600) + "分" + duration%60 + "秒");//时长
                    map.put("type", typeString);//类型
                    map.put("time", time);//通话时间
                    map.put("day", dayString);//
                    // map.put("time_lead", TimeStampUtil.compareTime(date));
                    list.add(map);
                }else {
                    return list;
                }

            }
        }
        return list;
    }
    //验证手机号是否正确ֻ
    public static boolean isMobileNO(String s) {
        Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
        Matcher m = p.matcher(s);
        return m.matches();
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

        long dayDistance = (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / DAY;
        dayDistance = Math.abs(dayDistance);

        return dayDistance;
    }

}

