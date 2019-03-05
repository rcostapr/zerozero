package com.myfeup.zerozero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        CalendarCellAdapter dayListAdapter = new CalendarCellAdapter(context,arrayListCalendar.get(i).getDayListEvents());

        txtDay.setText(arrayListCalendar.get(i).getDay());
        lstDay.setAdapter(dayListAdapter);

        return convertView;
    }
}
