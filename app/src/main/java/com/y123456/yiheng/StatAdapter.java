package com.y123456.yiheng;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created on 2019/2/18 11:44
 */

public class StatAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;

    public StatAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Holder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_stat, parent, false);
            holder = new Holder();
            holder.calllogName = (TextView) convertView.findViewById(R.id.calllog_name);
            holder.calllogNum = (TextView)convertView.findViewById(R.id.calllog_num);
            holder.calllogTime = (TextView)convertView.findViewById(R.id.calllog_time);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.calllogName.setText(list.get(position).get("name"));//通话时间段
        holder.calllogNum.setText(list.get(position).get("number"));//通话次数
        int duration= new Integer(list.get(position).get("time")).intValue();
        String str_dur;
        if(duration <60)
            str_dur = duration + "秒";//时长
        else if(duration / 60<60)
            str_dur = (duration / 60) + "分" + duration%60 + "秒";//时长
        else
            str_dur = (duration / 3600) + "时" + (duration % 3600)/60 + "分" + duration%60 + "秒";//时长
        Log.e(str_dur,"hhh时长hhh");
        holder.calllogTime.setText(str_dur);//通话时长
        return convertView;
    }

    class Holder {
        private TextView calllogName,//名字
                calllogNum,//时长
                calllogTime;//时间差
    }
}
