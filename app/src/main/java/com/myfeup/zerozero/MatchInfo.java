package com.myfeup.zerozero;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myfeup.zerozero.calendar.CalendarActivity;
import com.myfeup.zerozero.calendar.CalendarItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MatchInfo extends AppCompatActivity {

    private Match tvMatch;
    private Context mContext;
    private static final String FILE_STATE = "out.txt";
    private State state = new State(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);

        // Get the application context
        mContext = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar_Match_Info);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_Match_Info);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Adicionar aos Favoritos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        tvMatch =  (Match) intent.getSerializableExtra("tvMatch");

        TextView matchInfoSport = findViewById(R.id.match_Info_Sport);
        TextView matchInfoData = findViewById(R.id.match_Info_Data);
        TextView matchInfoCanal = findViewById(R.id.match_Info_Canal);
        ImageView matchInfoImgHomeTeam = findViewById(R.id.match_Info_imgHomeTeam);
        TextView matchInfoHomeTeam = findViewById(R.id.match_Info_txtHomeTeam);
        ImageView matchInfoImgAwayTeam = findViewById(R.id.match_Info_imgAwayTeam);
        TextView matchInfoAwayTeam = findViewById(R.id.match_Info_txtAwayTeam);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date matchDate = null;
        try {
            matchDate = format.parse(tvMatch.getMatchDay());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");

        matchInfoCanal.setText(tvMatch.getChannel()+"     "+timeFormat.format(matchDate));
        matchInfoSport.setText(tvMatch.getSports());
        matchInfoData.setText(dateFormat.format(matchDate));

        matchInfoHomeTeam.setText(tvMatch.getHomeTeam());
        matchInfoAwayTeam.setText(tvMatch.getAwayTeam());

        if(tvMatch.getAbsfileImgHomeTeam()!= null) {
            ImageView ivh = findViewById(R.id.match_Info_imgHomeTeam);
            ivh.setImageURI(Uri.parse(tvMatch.getAbsfileImgHomeTeam()));
        }

        if(tvMatch.getAbsfileImgAwayTeam()!=null) {
            ImageView iva = findViewById(R.id.match_Info_imgAwayTeam);
            iva.setImageURI(Uri.parse(tvMatch.getAbsfileImgAwayTeam()));
        }

        RelativeLayout hr = (RelativeLayout) matchInfoHomeTeam.getParent();
        hr.setClickable(true);

        hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date now = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                CalendarItem calendarItem = new CalendarItem(cal.get(Calendar.MONTH),cal.get(Calendar.YEAR),tvMatch.getHomeTeamId(),tvMatch.getHomeTeam());
                Intent calendar = new Intent(mContext, CalendarActivity.class);
                calendar.putExtra("calendarItem", calendarItem);
                startActivity(calendar);
            }
        });

        RelativeLayout ar = (RelativeLayout) matchInfoAwayTeam.getParent();
        ar.setClickable(true);

        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date now = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                CalendarItem calendarItem = new CalendarItem(cal.get(Calendar.MONTH),cal.get(Calendar.YEAR),tvMatch.getAwayTeamId(),tvMatch.getAwayTeam());
                Intent calendar = new Intent(mContext, CalendarActivity.class);
                calendar.putExtra("calendarItem", calendarItem);
                startActivity(calendar);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.match_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        try {
            this.state = getState();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (id == R.id.match_info_action_camera) {
            state.setState(1);
            try {
                saveState(state);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent nIntent = new Intent(mContext, Main2Activity.class);
            startActivity(nIntent);
            return true;
        }
        if (id == R.id.match_info_action_BackSports) {
            state.setState(2);
            try {
                saveState(state);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent nIntent = new Intent(mContext, Main2Activity.class);
            startActivity(nIntent);
            return true;
        }
        if (id == R.id.match_info_action_BackCalendar) {
            return true;
        }
        if (id == R.id.match_info_action_user) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
