package com.myfeup.zerozero.calendar;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.myfeup.zerozero.LocaleHelper;
import com.myfeup.zerozero.R;
import com.myfeup.zerozero.State;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private static final String FILE_STATE = "out.txt";

    private Context context;
    private ArrayList<CalendarCell> arrayCalendarCell = new ArrayList<>();
    private GridView calendar;
    private GridView topCalendar;
    private CalendarAdapter calendarAdapter;
    private CalendarTopAdapter topCalendarAdapter;
    private CalendarItem calendarItem;

    private State state = new State(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        File file = getApplicationContext().getFileStreamPath(FILE_STATE);
        if(file.exists()) {
            try {
                this.state = getState();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(!state.getIdioma().equals("default")) {
            //Change Application level locale
            LocaleHelper.setLocale(CalendarActivity.this, state.getIdioma(),state.getIdiomaCountry());
        }

        // Get the application context
        context = getApplicationContext();

        Intent intent = getIntent();

        calendarItem =  (CalendarItem) intent.getSerializableExtra("calendarItem");
        Calendar cal =  Calendar.getInstance();
        cal.set(Calendar.MONTH,calendarItem.getMonth());
        cal.set(Calendar.YEAR,calendarItem.getYear());

        setTitle(calendarItem.getTeamName()+ " Calendar");

        ImageView nextMonth = findViewById(R.id.imageViewRight);
        ImageView previousMonth = findViewById(R.id.imageViewLeft);
        TextView txtMonth = findViewById(R.id.textMonth);

        txtMonth.setText(cal.getDisplayName(Calendar.MONTH,Calendar.LONG,context.getResources().getConfiguration().locale)
        + " " + calendarItem.getYear());

        ArrayList<String> weeksNames = new ArrayList<>();
        weeksNames.add("Seg");
        weeksNames.add("Ter");
        weeksNames.add("Qua");
        weeksNames.add("Qui");
        weeksNames.add("Sex");
        weeksNames.add("Sáb");
        weeksNames.add("Dom");

        topCalendarAdapter = new CalendarTopAdapter(context,weeksNames);
        topCalendar = findViewById(R.id.dCalendarTop);
        topCalendar.setAdapter(topCalendarAdapter);

        calendarAdapter = new CalendarAdapter(context,arrayCalendarCell);
        calendar = findViewById(R.id.dCalendar);
        calendar.setAdapter(calendarAdapter);
        new TeamCalendar(context, calendarItem, calendarAdapter, calendar);

        FloatingActionButton fab = findViewById(R.id.fab_Calendar_Info);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Adicionar esta equipa Favoritos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        previousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal =  Calendar.getInstance();
                cal.set(Calendar.MONTH,calendarItem.getMonth());
                cal.set(Calendar.YEAR,calendarItem.getYear());
                cal.add(Calendar.MONTH,-1);
                CalendarItem nCalendarItem = new CalendarItem(cal.get(Calendar.MONTH),cal.get(Calendar.YEAR),calendarItem.getTeamId(),calendarItem.getTeamName());
                Intent calendar = new Intent(context, CalendarActivity.class);
                calendar.putExtra("calendarItem", nCalendarItem);
                startActivity(calendar);
            }
        });
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal =  Calendar.getInstance();
                cal.set(Calendar.MONTH,calendarItem.getMonth());
                cal.set(Calendar.YEAR,calendarItem.getYear());
                cal.add(Calendar.MONTH,1);
                CalendarItem nCalendarItem = new CalendarItem(cal.get(Calendar.MONTH),cal.get(Calendar.YEAR),calendarItem.getTeamId(),calendarItem.getTeamName());
                Intent calendar = new Intent(context, CalendarActivity.class);
                calendar.putExtra("calendarItem", nCalendarItem);
                startActivity(calendar);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }

    private void saveState(State state) throws IOException {
        FileOutputStream fos = getApplicationContext().openFileOutput(FILE_STATE, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(state);
        os.close();
        fos.close();
    }

    private State getState() throws IOException, ClassNotFoundException {
        FileInputStream fis = getApplicationContext().openFileInput(FILE_STATE);
        ObjectInputStream is = new ObjectInputStream(fis);
        State myState = (State) is.readObject();
        is.close();
        fis.close();
        return myState;
    }
}
