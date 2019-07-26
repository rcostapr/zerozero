package com.myfeup.zerozero;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myfeup.zerozero.calendar.CalendarActivity;
import com.myfeup.zerozero.calendar.CalendarItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MatchInfo extends AppCompatActivity {

    private Match tvMatch;
    private Context mContext;
    private static final String FILE_STATE = "out.txt";
    private State state = new State(1);
    private JSONObject jsonTVChannel = null;
    private ArrayList<TvChannel> arrayListChannel;

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

        // GET File Status
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

        matchInfoCanal.setText(getChannelName(tvMatch.getChannelId())+"     "+timeFormat.format(matchDate));
        matchInfoSport.setText(Integer.toString(tvMatch.getSportsId()));
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
    private String getChannelName(int channelId){

        if(state.getArrayListChannel()!=null)
            this.arrayListChannel = state.getArrayListChannel();
        else
            getArrayListChannel();



        for(int k=0; k < arrayListChannel.size(); k++){
            if(arrayListChannel.get(k).getId()==channelId){
                return arrayListChannel.get(k).getName();
            }
        }
        return null;
    }

    private void getArrayListChannel(){

        URL urlTVChannel = null;
        try {
            urlTVChannel = new URL(getString(R.string.urlTVChannel));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new MatchInfo.getJson().execute(urlTVChannel);

    }


    private class getJson extends AsyncTask<URL, Integer, Long> {

        JSONObject jsonObj = null;

        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                try {
                    jsonObj = getJsonUrl(urls[i]);
                    switch (i){
                        case 0:
                            jsonTVChannel = jsonObj;
                            break;
                        default:
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return totalSize;
        }
        protected void onPostExecute(Long result) {

            JSONObject mydata = null;
            try {
                mydata = jsonTVChannel.getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray tvchannels = null;
            try {
                tvchannels = mydata.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // looping through All Channels
            for (int i = 0; i < tvchannels.length(); i++) {
                JSONObject c = null;
                try {
                    c = tvchannels.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    String channel_id = getJsonString(c,"ID");
                    String channel = getJsonString(c,"NAME");
                    String image = getJsonString(c,"IMAGE");
                    String type = getJsonString(c,"TYPE");
                    String url = getJsonString(c,"URL");

                    String fileName = image.substring(image.lastIndexOf('/') + 1);

                    Log.d("MATCH INFO", channel_id + " - " + channel + " - " + image + " - " + fileName + " - " + url);
                    TvChannel nChannel = new TvChannel(Integer.parseInt(channel_id),
                            channel, image, type, url);

                    arrayListChannel.add(nChannel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            state.setArrayListChannel(arrayListChannel);
            try {
                saveState(state);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject getJsonUrl(URL url) throws IOException {

        String jsonStr;
        JSONObject jsonObj = null;
        Log.d("URL", url.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //Log.d("reader", convertStreamToString(in));
            jsonStr = convertStreamToString(in);

        } finally {
            urlConnection.disconnect();
        }

        try {
            jsonObj = new JSONObject(jsonStr);
        } catch (final JSONException e) {
            Log.e("JSON", "Json parsing error: " + e.getMessage());
        }

        return jsonObj;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private String getJsonString(JSONObject c, String key) throws JSONException {
        String val = null;
        if (c!=null)
            val = c.getString(key);
        return val;
    }
}
