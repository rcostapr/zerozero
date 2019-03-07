package com.myfeup.zerozero.calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myfeup.zerozero.Match;
import com.myfeup.zerozero.MatchInfo;
import com.myfeup.zerozero.R;

import java.util.ArrayList;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CalendarCell> arrayListCalendar;
    CalendarCell cell;

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

        this.cell = arrayListCalendar.get(i);
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

        convertView.setTag(i);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = (int) v.getTag();

                CalendarCell match = arrayListCalendar.get(position);

                RelativeLayout circleView = v.findViewById(R.id.circleTop);
                if(circleView.getBackground().getConstantState().equals(context.getDrawable(R.drawable.calendar_circle).getConstantState())) {
                    Log.i("GRIDELEMENT", "ITEM Go to Match ->"+circleView.toString() + " Day " + match.getDay());
                    Intent infoMatch = new Intent(context, MatchInfo.class);
                    Match matchDay = match.getMatch();
                    infoMatch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    infoMatch.putExtra("tvMatch", matchDay);
                    context.startActivity(infoMatch);
                }

                if(circleView.getBackground().getConstantState().equals(context.getDrawable(R.drawable.calendar_circle_fill).getConstantState())) {
                    Log.i("GRIDELEMENT", "ITEM Fill Go to Match ->"+circleView.toString() + " Day " + match.getDay());
                    Intent infoMatch = new Intent(context, MatchInfo.class);
                    Match matchDay = match.getMatch();
                    infoMatch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    infoMatch.putExtra("tvMatch", matchDay);
                    context.startActivity(infoMatch);
                }

                GridView calendarGrid = (GridView) v.getParent();
                for(int k = 0; k < calendarGrid.getChildCount(); k++) {
                    //Log.i("GRIDELEMENT", "ITEM ->"+calendarGrid.getChildAt(k).toString());
                    RelativeLayout gridIem = (RelativeLayout) calendarGrid.getChildAt(k);
                    RelativeLayout circleIemView = gridIem.findViewById(R.id.circleTop);

                    if(!circleIemView.getBackground().getConstantState().equals(context.getDrawable(R.drawable.calendar_circle_fill).getConstantState())) {
                        circleIemView.setBackgroundColor(Color.TRANSPARENT);
                    }
                    //Log.i("GRIDELEMENT", "ITEM ->"+circleIemView.getBackground().getConstantState().toString() + " Compare To " + (context.getDrawable(R.drawable.calendar_circle_fill).getConstantState()).toString());
                }
                //Log.i("VIEW", "onClick ->" + v.toString() + " Touch -> " + v.getParent().toString() + " GRID -> " + calendarGrid.getChildCount());


                if(!circleView.getBackground().getConstantState().equals(context.getDrawable(R.drawable.calendar_circle_fill).getConstantState())) {
                    circleView.setBackground(context.getDrawable(R.drawable.calendar_circle));
                }
            }
        });

        return convertView;
    }
}
