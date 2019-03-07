package com.myfeup.zerozero.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myfeup.zerozero.R;

import java.util.ArrayList;

public class CalendarTopAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> arrayListCalendar;

    public CalendarTopAdapter(Context context, ArrayList<String> arrayListCalendar){
        this.context = context;
        this.arrayListCalendar = arrayListCalendar;
    }

    @Override
    public int getCount() {
        return arrayListCalendar.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListCalendar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_top_cell, parent, false);
        }

        TextView txtDayOfWeek = convertView.findViewById(R.id.textWeekName);
        txtDayOfWeek.setText(arrayListCalendar.get(i));

        if(i==6){
            txtDayOfWeek.setTextColor(context.getResources().getColor(R.color.colorAlert));
        }

        return convertView;
    }
}
