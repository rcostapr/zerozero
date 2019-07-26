package com.myfeup.zerozero.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.myfeup.zerozero.LocaleHelper;
import com.myfeup.zerozero.Match;
import com.myfeup.zerozero.R;

import com.myfeup.zerozero.State;
import com.myfeup.zerozero.TvChannel;
import com.myfeup.zerozero.TvTeamList;

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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class TeamCalendar {

    private String TAG;
    private int teamId;
    private String teamName;
    private URL urlTeam;
    private Context context;
    private static final String FILE_STATE = "out.txt";
    private static final String DISK_CACHE_SUBDIR = "Teams";
    private State state = new State(1);
    private JSONObject jsonTVTEAM = null;
    private ArrayList<Match> arrayListChannel = new ArrayList<>();
    private ArrayList<CalendarCell> arrayCalendarCell = new ArrayList<>();
    private ArrayList<TvChannel> arrayListTvChannel = new ArrayList<>();
    private int iniMonth;
    private int iniYear;
    private int firstDay;
    private int firstMonth;
    private int lastDay;
    private int lastMonth;

    private CalendarAdapter calendarAdapter;
    private GridView calendarGrid;

    public TeamCalendar (Context context, CalendarItem calendarItem, CalendarAdapter calendarAdapter, GridView calendar){
        this.teamId = calendarItem.getTeamId();
        this.iniMonth = calendarItem.getMonth();
        this.iniYear = calendarItem.getYear();
        this.context = context;
        this.calendarAdapter = calendarAdapter;
        this.calendarGrid = calendar;

        this.TAG  = context.getClass().getSimpleName();

        boolean isConnected = checkInternetConnection();

        File file = context.getFileStreamPath(FILE_STATE);
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
            LocaleHelper.setLocale(context, state.getIdioma(),state.getIdiomaCountry());
        }
        // Get URL By Definition
        this.urlTeam = getUrl(context.getString(R.string.urlTeam)
                + Integer.toString(teamId));
        Log.i("MATCH","URL -> " + urlTeam);
        // Get Match List arrayListChannel
        if (isConnected) {
            new getJson().execute(this.urlTeam);
        } else {
            importTeamListFromCache();
        }

        arrayListTvChannel = state.getArrayListChannel();
    }

    private void buildArrayCalendarCell() {
        ArrayList<CalendarCell> arrayListCalendar = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");

        Calendar cal = Calendar.getInstance();
        Calendar inical = Calendar.getInstance();

        Date now = new Date();
        cal.set(Calendar.MONTH,iniMonth);
        cal.set(Calendar.YEAR,iniYear);
        cal.set(Calendar.DAY_OF_MONTH,1);
        inical.setTime(now);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        Log.i("DATETIME",  "INIDATE dayOfWeek -> "  + dayOfWeek + " iniMonth -> "+ iniMonth );

        while (cal.get(Calendar.DAY_OF_MONTH) != 1){
            cal.add(Calendar.DAY_OF_MONTH,-1);
        }
        while (cal.get(Calendar.DAY_OF_WEEK) != 2){
            cal.add(Calendar.DAY_OF_MONTH,-1);
        }
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        Log.i("DATETIME",  "STARTDATE dayOfWeek -> "  + dayOfWeek + " Year -> "+ cal.get(Calendar.YEAR) + " Month -> "+ cal.get(Calendar.MONTH) + " Day " + cal.get(Calendar.DAY_OF_MONTH));

        this.firstMonth = cal.get(Calendar.MONTH);
        this.firstDay = cal.get(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.MONTH,iniMonth);
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        Log.i("DATETIME",  "ENDOFMONTH dayOfWeek -> "  + dayOfWeek + " Year -> "+ cal.get(Calendar.YEAR) + " Month -> "+ cal.get(Calendar.MONTH) + " Day " + cal.get(Calendar.DAY_OF_MONTH));

        while (cal.get(Calendar.DAY_OF_WEEK) != 1){
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
        cal.add(Calendar.DAY_OF_MONTH,1);
        Log.i("DATETIME",  "ENDDATE dayOfWeek -> "  + dayOfWeek + " Year -> "+ cal.get(Calendar.YEAR) + " Month -> "+ cal.get(Calendar.MONTH) + " Day " + cal.get(Calendar.DAY_OF_MONTH));

        this.lastMonth = cal.get(Calendar.MONTH);
        this.lastDay = cal.get(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.MONTH,this.firstMonth);
        cal.set(Calendar.DAY_OF_MONTH,this.firstDay);

        while (!(cal.get(Calendar.MONTH) == this.lastMonth && cal.get(Calendar.DAY_OF_MONTH) == this.lastDay)){
            ArrayList<String> dayEvents = new ArrayList<>();
            Match match = null;
            //Log.i("MATCH", "GAMES -> " + arrayListChannel.size());
            for(int k=0;k<arrayListChannel.size();k++){
                Match matchDay = arrayListChannel.get(k);
                Date matchDate = null;
                try {
                    matchDate = format.parse(matchDay.getMatchDay());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar matchCalendar = Calendar.getInstance();
                matchCalendar.setTime(matchDate);
/*
                Log.i("MATCH", "matchCalendar.get(Calendar.MONTH)=" + matchCalendar.get(Calendar.MONTH)
                        + "==cal.get(Calendar.MONTH)=" +cal.get(Calendar.MONTH) + "\n\n"
                        + " matchCalendar.get(Calendar.DAY_OF_MONTH)=" + matchCalendar.get(Calendar.DAY_OF_MONTH)
                        + "==cal.get(Calendar.DAY_OF_MONTH)=" + cal.get(Calendar.DAY_OF_MONTH));
*/
                if(matchCalendar.get(Calendar.MONTH)==cal.get(Calendar.MONTH) && matchCalendar.get(Calendar.DAY_OF_MONTH)==cal.get(Calendar.DAY_OF_MONTH)){
                    dayEvents.add(matchDay.getHomeTeam());
                    dayEvents.add("Vs");
                    dayEvents.add(matchDay.getAwayTeam());
                    dayEvents.add(getChannelName(matchDay.getChannelId()));
                    dayEvents.add(timeFormat.format(matchDate));
                    match = matchDay;
                }
            }
            CalendarCell item = new CalendarCell(cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH),cal.get(Calendar.YEAR),dayEvents);
            item.setMatch(match);
            if(cal.get(Calendar.MONTH)!=iniMonth){
                item.setDayOfThisMonth(false);
            }
            if(cal.get(Calendar.DAY_OF_MONTH)==inical.get(Calendar.DAY_OF_MONTH) && cal.get(Calendar.MONTH)==inical.get(Calendar.MONTH)){
                item.setToday(true);
            }
            arrayListCalendar.add(item);
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
        Log.i("ARRAY", "Calendar Size -> "+ arrayListCalendar.size());
        this.arrayCalendarCell = arrayListCalendar;
        // TODO : Update Adapter
        calendarAdapter = new CalendarAdapter(context,arrayCalendarCell);
        calendarGrid.setAdapter(calendarAdapter);
    }

    public ArrayList<CalendarCell> getArrayCalendarCell() {
        Log.i("ARRAY","arrayCalendarCell Size ->" + arrayCalendarCell);
        return arrayCalendarCell;
    }

    public void setArrayCalendarCell(ArrayList<CalendarCell> arrayCalendarCell) {
        this.arrayCalendarCell = arrayCalendarCell;
    }

    private URL getUrl(String strUrl){
        URL url = null;

        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private boolean checkInternetConnection() {

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if(cm!=null)
            activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if(!isConnected){
            // Setting Toast
            CharSequence text = context.getResources().getText(R.string.no_internet);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        return isConnected;
    }

    private JSONObject getJsonUrl(URL url) throws IOException {

        String jsonStr;
        JSONObject jsonObj = null;
        Log.d("MATCHURL", url.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            jsonStr = convertStreamToString(in);

        } finally {
            urlConnection.disconnect();
        }

        try {
            jsonObj = new JSONObject(jsonStr);
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

        return jsonObj;
    }

    @SuppressLint("StaticFieldLeak")
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
                            jsonTVTEAM = jsonObj;
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
                mydata = jsonTVTEAM.getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray tvchannels = null;
            if(mydata!=null) {
                try {
                    tvchannels = mydata.getJSONArray("PROFILE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(tvchannels!=null) {
                // looping through All matches
                for (int i = 0; i < tvchannels.length(); i++) {
                    JSONObject c = null;
                    try {
                        c = tvchannels.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        String matchId = getJsonString(c,"MATCHID");
                        String channelId = getJsonString(c,"CHANNELIDS");
                        String sportsId = getJsonString(c,"SPORTID");
                        String competitionId = getJsonString(c,"COMPETITIONID");
                        String matchDay = getJsonString(c,"DATE");
                        String toolTip = getJsonString(c,"TOOLTIP");

                        JSONObject homeTeamArray = null;
                        homeTeamArray = c.getJSONObject("HOMETEAM");
                        JSONObject awayTeamArray = null;
                        awayTeamArray = c.getJSONObject("AWAYTEAM");

                        String homeTeamId = getJsonString(homeTeamArray,"ID");
                        String homeTeam = getJsonString(homeTeamArray,"NAME");
                        String imgHomeTeam = getJsonString(homeTeamArray,"IMAGE");
                        String fileImgHomeTeam = imgHomeTeam.substring(imgHomeTeam.lastIndexOf('/') + 1);

                        String awayTeamId = getJsonString(awayTeamArray,"ID");
                        String awayTeam = getJsonString(awayTeamArray,"NAME");
                        String imgAwayTeam = getJsonString(awayTeamArray,"IMAGE");
                        String fileImgAwayTeam = imgAwayTeam.substring(imgAwayTeam.lastIndexOf('/') + 1);


                        ContextWrapper wrapper = new ContextWrapper(context);
                        File file1 = wrapper.getDir(DISK_CACHE_SUBDIR,MODE_PRIVATE);
                        file1 = new File(file1, fileImgHomeTeam);
                        File file2 = wrapper.getDir(DISK_CACHE_SUBDIR,MODE_PRIVATE);
                        file2 = new File(file2, fileImgAwayTeam);


                        Log.d("channelId", channelId + " - " + homeTeam + " - " + awayTeam + " - " + matchDay);
                        Match nMatch = new Match(Integer.parseInt(matchId),
                                Integer.parseInt(channelId),
                                Integer.parseInt(competitionId),
                                matchDay,
                                Integer.parseInt(homeTeamId),
                                Integer.parseInt(awayTeamId),
                                homeTeam,
                                awayTeam,
                                Integer.parseInt(sportsId),
                                toolTip);


                        if (!file1.exists()) {
                            new DownloadTask(nMatch, 1).execute(imgHomeTeam, fileImgHomeTeam);
                        } else {
                            nMatch.setAbsfileImgHomeTeam(file1.getAbsolutePath());
                        }
                        if (!file2.exists()) {
                            new DownloadTask(nMatch, 2).execute(imgAwayTeam, fileImgAwayTeam);
                        } else {
                            nMatch.setAbsfileImgAwayTeam(file2.getAbsolutePath());
                        }

                        // Setting Calendar Team Name
                        if (teamName==null) {
                            if (Integer.parseInt(homeTeamId) == teamId) {
                                teamName = homeTeam;
                            } else if (Integer.parseInt(awayTeamId) == teamId) {
                                teamName = awayTeam;
                            }
                        }
                        arrayListChannel.add(nMatch);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            TvTeamList nTvTeamList = new TvTeamList(teamId,arrayListChannel);
            state.addArrayTvTeamList(nTvTeamList);
            try {
                saveState(state);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // TODO : Build Array Calendar Cell
            // Build Calendar
            buildArrayCalendarCell();
        }
    }

    /**
     * Store Images on local filesystem
     */
    private class DownloadTask extends AsyncTask<String, Void, Bitmap>{

        Match tvMatch;
        int team;

        private DownloadTask(Match tvMatch, int team) {
            this.tvMatch = tvMatch;
            this.team = team;
        }
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(String... urls){
            URL url = stringToURL(urls[0]);
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Save bitmap to internal storage
                Uri imageInternalUri = saveImageToInternalStorage(bmp, urls[1]);

                //Log.d(TAG,imageInternalUri.toString());
                if(team==1){
                    tvMatch.setAbsfileImgHomeTeam(imageInternalUri.toString());
                } else {
                    tvMatch.setAbsfileImgAwayTeam(imageInternalUri.toString());
                }

                // Return the downloaded bitmap
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            // TODO : notify matchAdapter
            calendarAdapter.notifyDataSetChanged();
        }
    }

    private String getJsonString(JSONObject c, String key) throws JSONException {
        String val = null;
        if (c!=null)
            val = c.getString(key);
        return val;
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

    private void saveState(State state) throws IOException {
        FileOutputStream fos = context.openFileOutput(FILE_STATE, MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(state);
        os.close();
        fos.close();
    }

    private State getState() throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(FILE_STATE);
        ObjectInputStream is = new ObjectInputStream(fis);
        State myState = (State) is.readObject();
        is.close();
        fis.close();
        return myState;
    }

    // Custom method to convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap, String imgName){
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(context);

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir(DISK_CACHE_SUBDIR,MODE_PRIVATE);

        // Create a file to save the image
        file = new File(file, imgName);

        try{
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
    }
    private void importTeamListFromCache(){
        ArrayList<TvTeamList> arrayTvTeamList = state.getArrayTvTeamList();
        for(int k=0;k<arrayTvTeamList.size();k++){
            if(arrayTvTeamList.get(k).getId()==teamId){
                this.arrayListChannel = arrayTvTeamList.get(k).getArrayListChannel();
                // TODO : Build Array Calendar Cell
                // Build Calendar
                buildArrayCalendarCell();
                break;
            }
        }
    }

    private String getChannelName(int channelId){
        for(int k=0; k<arrayListTvChannel.size(); k++){
            if(arrayListTvChannel.get(k).getId()==channelId){
                return arrayListTvChannel.get(k).getName();
            }
        }
        return null;
    }
}

