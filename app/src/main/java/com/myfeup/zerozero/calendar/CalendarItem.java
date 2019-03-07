package com.myfeup.zerozero.calendar;

import java.io.Serializable;

public class CalendarItem implements Serializable {
    int month;
    int year;
    int teamId;
    String teamName;

    public CalendarItem (int month, int year, int teamId, String teamName){
        this.month=month;
        this.year=year;
        this.teamId=teamId;
        this.teamName=teamName;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
