package com.example.workout_4.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.workout_4.R;

public class RecordListAdapter extends BaseAdapter{

    private String[] mspl_dates;
    Activity activity;
    LayoutInflater inflater;


    public RecordListAdapter(Activity activity, String[] spl_dates){
        mspl_dates=spl_dates;
        this.activity = activity;

        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mspl_dates.length;
    }

    @Override
    public Object getItem(int position) {
        return mspl_dates[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 檢查convertView是否有值，有值表示是重複使用的
        if (convertView == null) {
            // 沒有值就要自己建立一個
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sql, null);
        }

        // 找到TextView
        TextView title = (TextView) convertView.findViewById(R.id.traning_date);
        // 取出文字
        String text = (String) getItem(position);
        // 將文字內容設定給TextView
        title.setText(text);

        // 一定要將convertView回傳，供ListView呈現使用，並加入重用機制中
        return convertView;
    }
}
