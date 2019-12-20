package com.example.workout_4;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ChestMenuAdapter extends BaseAdapter {

    Activity activity;
    List<ActionInfo> users;
    LayoutInflater inflater;

    public ChestMenuAdapter(Activity activity) {
        this.activity = activity;
    }

    public ChestMenuAdapter(Activity activity, List<ActionInfo> users) {
        this.activity = activity;
        this.users = users;

        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if(view == null){

            view = inflater.inflate(R.layout.all_menu_item,viewGroup,false);

            holder = new ViewHolder();

            holder.SportName = (TextView)view.findViewById(R.id.sport_name);
            holder.ivSport = (ImageView)view.findViewById(R.id.ivSprot);
            holder.finish = (TextView)view.findViewById(R.id.sport_over);

            view.setTag(holder);
        }
        else{
            holder = (ViewHolder)view.getTag();
        }
        ActionInfo model = users.get(position);

        holder.ivSport.setBackgroundResource(model.getFlag());
        holder.SportName.setText(model.getUserName());
        holder.finish.setText(model.getFinish());

        return  view;
    }
    public void updateRecords(List<ActionInfo> users){
        this.users = users;

        notifyDataSetChanged();
    }

    class  ViewHolder{
        TextView SportName;
        ImageView ivSport;
        TextView finish;
    }
}
