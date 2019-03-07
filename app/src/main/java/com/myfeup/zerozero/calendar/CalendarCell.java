package com.myfeup.zerozero.calendar;

import com.myfeup.zerozero.Match;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

class CalendarCell implements Serializable {

    int day;
    int month;
    int year;
    Match match = null;
    boolean today = false;
    boolean dayOfThisMonth = true;
    ArrayList<String> dayListEvents;

    public CalendarCell(int day, int month, int year, ArrayList<String> dayListEvents){
        this.day=day;
        this.month=month;
        this.year=year;
        this.dayListEvents = dayListEvents;
    }

    public int getWeekDay(){
        Calendar nCal = Calendar.getInstance();
        nCal.set(Calendar.DAY_OF_MONTH,day);
        nCal.set(Calendar.MONTH,month);
        nCal.set(Calendar.YEAR,year);
        return nCal.get(Calendar.DAY_OF_WEEK);
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public int getYear() {
        return year;
    }

    public String getDay() {
        return Integer.toString(day);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean isDayOfThisMonth() {
        return dayOfThisMonth;
    }

    public void setDayOfThisMonth(boolean dayOfThisMonth) {
        this.dayOfThisMonth = dayOfThisMonth;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<String> getDayListEvents() {
        return dayListEvents;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }
}
