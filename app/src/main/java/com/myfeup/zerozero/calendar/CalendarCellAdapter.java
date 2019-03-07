package com.myfeup.zerozero.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.shapes.Shape;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myfeup.zerozero.R;

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
        switch (position){
            case 0:
                dayEventRow.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                break;
            case 1:
                dayEventRow.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                dayEventRow.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                break;
            case 3:
                dayEventRow.setTextColor(context.getResources().getColor(R.color.colorOks));
                break;
            case 4:
                dayEventRow.setTextColor(context.getResources().getColor(R.color.colorLightOrange));
                dayEventRow.setTypeface(null, Typeface.BOLD);
                break;
            default:
                dayEventRow.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                break;
        }
        dayEventRow.setText(arrayListCell.get(position));
        return convertView;
    }
}
