package com.myfeup.zerozero;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {

    private String TAG = Main2Activity.class.getSimpleName();

    private static final String FILE_STATE = "out.txt";

    private Context context;
    private ArrayList<CalendarCell> arrayCalendarCell;
    private GridView calendar;
    private CalendarAdapter calendarAdapter;

    private State state = new State(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        boolean isConnected = checkInternetConnection();
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
        this.arrayCalendarCell =  (ArrayList<CalendarCell>) intent.getSerializableExtra("calendarList");

        Log.i("ARRAY", "Size -> " + arrayCalendarCell.size());

        calendarAdapter = new CalendarAdapter(context,arrayCalendarCell);

        calendar = findViewById(R.id.dCalendar);

        calendar.setAdapter(calendarAdapter);

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

    private boolean checkInternetConnection() {

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if(!isConnected){
            // Setting Toast
            Context context = getApplicationContext();
            CharSequence text = getResources().getText(R.string.no_internet);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            state.setWiFi(isWiFi);
        }
        state.setInternetStatus(isConnected);
        return isConnected;
    }
}
