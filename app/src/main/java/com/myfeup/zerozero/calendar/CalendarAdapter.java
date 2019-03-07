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

public class CalendarAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CalendarCell> arrayListCalendar;

    public CalendarAdapter(Context context, ArrayList<CalendarCell> arrayListCalendar){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_cell, parent, false);
        }

        TextView txtDay = convertView.findViewById(R.id.txtDay);
        ListView lstDay = convertView.findViewById(R.id.calendarCellList);
        RelativeLayout circleView = convertView.findViewById(R.id.circleTop);
        circleView.setBackgroundColor(Color.TRANSPARENT);

        if(!arrayListCalendar.get(i).isDayOfThisMonth()){
            txtDay.setTextColor(context.getResources().getColor(R.color.colorGrey));
        }
        if(arrayListCalendar.get(i).isDayOfThisMonth() && arrayListCalendar.get(i).getWeekDay()!=1){
            txtDay.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }
        if(arrayListCalendar.get(i).isDayOfThisMonth() && arrayListCalendar.get(i).getWeekDay()==1){
            txtDay.setTextColor(context.getResources().getColor(R.color.colorAlert));
        }
        if(arrayListCalendar.get(i).isToday()){
            txtDay.setTextColor(context.getResources().getColor(R.color.colorWhite));
            circleView.setBackground(context.getDrawable(R.drawable.calendar_circle_fill));
        }


        CalendarCellAdapter dayListAdapter = new CalendarCellAdapter(context,arrayListCalendar.get(i).getDayListEvents());

        txtDay.setText(arrayListCalendar.get(i).getDay());
        lstDay.setAdapter(dayListAdapter);

        return convertView;
    }
}
