package com.myfeup.zerozero;

import java.io.Serializable;
import java.util.ArrayList;

class CalendarCell implements Serializable {

    int day;
    ArrayList<String> dayListEvents;

    public CalendarCell(int day, ArrayList<String> dayListEvents){
        this.day=day;
        this.dayListEvents = dayListEvents;
    }

    public String getDay() {
        return Integer.toString(day);
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<String> getDayListEvents() {
        return dayListEvents;
    }
}
