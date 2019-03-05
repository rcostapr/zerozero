package com.myfeup.zerozero;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CalendarCellAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> arrayListCell;

    public CalendarCellAdapter(Context context, ArrayList<String> arrayListCell){
        this.context=context;
        this.arrayListCell= arrayListCell;
    }

    @Override
    public int getCount() {
        return arrayListCell.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListCell.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_cell_row, parent, false);
        }
        TextView dayEventRow = convertView.findViewById(R.id.calendarTxtRow);
        dayEventRow.setText(arrayListCell.get(position));
        return convertView;
    }
}
