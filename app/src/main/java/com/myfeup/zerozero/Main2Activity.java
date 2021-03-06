package com.myfeup.zerozero;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.myfeup.zerozero.calendar.CalendarActivity;
import com.myfeup.zerozero.calendar.CalendarItem;
import com.myfeup.zerozero.calendar.TeamCalendar;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = Main2Activity.class.getSimpleName();

    private ProgressDialog mProgressDialog;

    private Context mContext;
    private Activity mActivity;

    private ListView channelList;
    private ArrayList<TvChannel> arrayListChannel;
    private ArrayList<Competition> arrayListSports;

    private URL urlTVChannel = null;
    private URL urlCompetitions = null;
    private JSONObject jsonTVChannel = null;
    private JSONObject jsonSports = null;

    private static final String DISK_CACHE_SUBDIR = "Images";
    private static final String DISK_CACHE_TEAMS = "Teams";
    private static final String FILE_STATE = "out.txt";
    private static final String COLOR_BACKGROUND = "#FF4081";

    private CustomAdapter customAdapter;
    private SportsAdapter sportsAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;

    private State state = new State(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("IDIOMA","Locale -> " + Locale.getDefault().getDisplayLanguage()
                + " - " + Locale.getDefault().getLanguage()
                + " - " + Locale.getDefault().getCountry()
                + " - " + Locale.getDefault().getDisplayCountry()
                + " - " + Locale.getDefault().getDisplayName()
        );

        // GET File Status
        File file = getApplicationContext().getFileStreamPath(FILE_STATE);
        if(file.exists()) {
            try {
                Log.i(TAG,"File Exists State -> " + Integer.toString(state.getState()));
                this.state = getState();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Log.i("IDIOMA", "ENTRY IDIOMA -> "+ state.getIdioma());
        if(!state.getIdioma().equals("default")) {
            //Change Application level locale
            LocaleHelper.setLocale(Main2Activity.this, state.getIdioma(),state.getIdiomaCountry());
        }

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = Main2Activity.this;
        channelList = findViewById(R.id.lst2Canais);

        // Setting Favorites Icon
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ver Notificação de Eventos Favoritos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Register customAdapter
        arrayListChannel = new ArrayList<>();
        customAdapter = new CustomAdapter(this,arrayListChannel);

        // Register sportsAdapter
        arrayListSports = new ArrayList<>();
        sportsAdapter = new SportsAdapter(this,arrayListSports);

        // Initialize the progress dialog
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setIndeterminate(true);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Progress dialog title
        mProgressDialog.setTitle(getString(R.string.updating_application));
        // Progress dialog message
        mProgressDialog.setMessage(getString(R.string.downloading_content));

        try {
            urlTVChannel = new URL(getString(R.string.urlTVChannel));
            urlCompetitions = new URL(getString(R.string.urlCompetitions));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        boolean isConnected = checkInternetConnection();

        switch (state.getState()){
            case 1:
                if(isConnected) {
                    arrayListChannel.clear();
                    new Main2Activity.getJson().execute(urlTVChannel);
                } else {
                    if(state.getArrayListChannel()!=null)
                        this.arrayListChannel = state.getArrayListChannel();
                    customAdapter = new CustomAdapter(this,arrayListChannel);
                }
                channelList.setAdapter(customAdapter);
                channelList.setClickable(true);
                channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        Intent infoChannel = new Intent(mContext, info_channel.class);
                        infoChannel.putExtra("tvChannel",arrayListChannel.get(i));
                        startActivity(infoChannel);
                    }
                });
                break;
            case 2:
                this.setTitle(R.string.sports_list);
                if(isConnected) {
                    arrayListSports.clear();
                    new Main2Activity.getJsonSports().execute(urlCompetitions);
                } else {
                    this.arrayListSports = state.getArrayListSports();
                    sportsAdapter = new SportsAdapter(this,arrayListSports);
                }
                channelList.setAdapter(sportsAdapter);
                channelList.setClickable(true);
                channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        Intent infoSport = new Intent(mContext, info_sport.class);
                        infoSport.putExtra("tvSports",arrayListSports.get(i));
                        startActivity(infoSport);
                    }
                });
                break;
            default:
                break;
        }

        swipeRefreshLayout=findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor(COLOR_BACKGROUND));

        // Register swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                boolean isConnected = checkInternetConnection();
                Log.d(TAG,"State -> " + Integer.toString(state.getState()));
                if (isConnected) {
                    switch (state.getState()) {
                        case 1:
                            arrayListChannel.clear();
                            new Main2Activity.getJson().execute(urlTVChannel);
                            break;
                        case 2:
                            arrayListSports.clear();
                            new Main2Activity.getJsonSports().execute(urlCompetitions);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private boolean checkInternetConnection() {

        View view = findViewById(R.id.lst2Canais);

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        Log.d("CONNECTIVITY" , "INTERNET -> " + isConnected);
        if(!isConnected){
            if(swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);
            // Setting Snackbar
            Snackbar snackbar = Snackbar.make(view, R.string.no_internet, Snackbar.LENGTH_LONG).setAction("Action", null);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAlert));
            snackbar.show();
        } else {
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            state.setWiFi(isWiFi);

            if(!state.isInternetStatus()) {
                // Setting Snackbar
                //Snackbar snackbar = Snackbar.make(view, "Com ligação à internet", Snackbar.LENGTH_LONG).setAction("Action", null);
                //snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorOks));
                //snackbar.show();
            }
        }
        state.setInternetStatus(isConnected);
        return isConnected;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(mContext, info_settings.class);
            startActivity(settings);
            return true;
        }

        if (id == R.id.action_cleancache){
            cleanCache();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_channels) {
            this.state.setState(1);
            try {
                saveState(state);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setTitle(R.string.channel_list);

            boolean isConnected = checkInternetConnection();

            if(isConnected) {
                arrayListChannel.clear();
                new Main2Activity.getJson().execute(urlTVChannel);
            } else {
                this.arrayListChannel = state.getArrayListChannel();
                customAdapter = new CustomAdapter(this,arrayListChannel);
            }
            channelList.setAdapter(customAdapter);
            channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    Intent infoChannel = new Intent(mContext, info_channel.class);
                    infoChannel.putExtra("tvChannel", arrayListChannel.get(i));
                    startActivity(infoChannel);
                }
            });
        } else if (id == R.id.nav_sports) {
            try {
                this.state.setState(2);
                saveState(state);
                this.setTitle(R.string.sports_list);
                boolean isConnected = checkInternetConnection();

                if(isConnected) {
                    arrayListSports.clear();
                    new Main2Activity.getJsonSports().execute(urlCompetitions);
                } else {
                    this.arrayListSports = state.getArrayListSports();
                    sportsAdapter = new SportsAdapter(this,arrayListSports);
                }
                channelList.setAdapter(sportsAdapter);

                channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        Intent infoSport = new Intent(mContext, info_sport.class);
                        infoSport.putExtra("tvSports", arrayListSports.get(i));
                        startActivity(infoSport);
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_teams) {

        } else if (id == R.id.nav_login) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_close) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } else if (id == R.id.nav_calendar) {
            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            CalendarItem calendarItem = new CalendarItem(cal.get(Calendar.MONTH),cal.get(Calendar.YEAR),209901, "FC Porto");
            Intent calendar = new Intent(mContext, CalendarActivity.class);
            calendar.putExtra("calendarItem", calendarItem);
            startActivity(calendar);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

        return jsonObj;
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

                    Log.d(TAG, channel_id + " - " + channel + " - " + image + " - " + fileName + " - " + url);
                    TvChannel nChannel = new TvChannel(Integer.parseInt(channel_id),
                            channel, image, type, url);

                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    File file = wrapper.getDir(DISK_CACHE_SUBDIR,MODE_PRIVATE);
                    file = new File(file, fileName);

                    if (!file.exists()) {
                        // Execute the async task
                        new Main2Activity.DownloadTask(nChannel).execute(image, fileName);
                    } else {
                        nChannel.setAbsImgFileName(file.getAbsolutePath());
                    }
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
            customAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getJsonSports extends AsyncTask<URL, Integer, Long> {

        JSONObject jsonObj = null;

        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                try {
                    jsonObj = getJsonUrl(urls[i]);
                    switch (i){
                        case 0:
                            jsonSports = jsonObj;
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
                mydata = jsonSports.getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray sports = null;
            try {
                sports = mydata.getJSONArray("COMPETS");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // looping through All Channels
            for (int i = 0; i < sports.length(); i++) {
                JSONObject sport = null;
                try {
                    sport = sports.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    String competition_id =  getJsonString(sport,"ID");
                    String sport_id = getJsonString(sport,"SPORTID");
                    String name = getJsonString(sport,"NAME");
                    String image = getJsonString(sport,"IMAGE");
                    String ongoing = getJsonString(sport,"ONGOING");
                    String normalcolor = getJsonString(sport,"NORMALCOLOR");
                    String darkcolor = getJsonString(sport,"DARKCOLOR");

                    Log.d(TAG, competition_id + " - " + sport_id + " - " + name + " - " + image);
                    Competition nSport = new Competition(Integer.parseInt(competition_id),name,image,Integer.parseInt(sport_id),Integer.parseInt(ongoing),normalcolor,darkcolor);

                    String fileName = image.substring(image.lastIndexOf('/') + 1);

                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    File file = wrapper.getDir(DISK_CACHE_SUBDIR,MODE_PRIVATE);
                    file = new File(file, fileName);

                    if (!file.exists()) {
                        // Execute the async task
                        new Main2Activity.DownloadCompetitionTask(nSport).execute(image, fileName);
                    } else {
                        nSport.setAbsImgFileName(file.getAbsolutePath());
                    }

                    arrayListSports.add(nSport);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            state.setArrayListSports(arrayListSports);
            try {
                saveState(state);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sportsAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
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

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


    /**
     * Store Images on local filesystem
     */
    @SuppressLint("StaticFieldLeak")
    private class DownloadTask extends AsyncTask<String, Void, Bitmap>{

        TvChannel tvChannel;

        private DownloadTask(TvChannel tvChannel) {
            this.tvChannel = tvChannel;
        }
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
            mProgressDialog.show();
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
                tvChannel.setAbsImgFileName(imageInternalUri.toString());

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
            // Hide the progress dialog
            mProgressDialog.dismiss();
            customAdapter.notifyDataSetChanged();
        }
    }


    /**
     * Store Images on local filesystem
     */
    @SuppressLint("StaticFieldLeak")
    private class DownloadCompetitionTask extends AsyncTask<String, Void, Bitmap>{

        Competition competition;

        private DownloadCompetitionTask(Competition competition) {
            this.competition = competition;
        }
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
            mProgressDialog.show();
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
                competition.setAbsImgFileName(imageInternalUri.toString());

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
            // Hide the progress dialog
            mProgressDialog.dismiss();
            customAdapter.notifyDataSetChanged();
        }
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
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir(DISK_CACHE_SUBDIR,MODE_PRIVATE);

        // Create a file to save the image
        file = new File(file, imgName);

        try{
            // Initialize a new OutputStream
            OutputStream stream;

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

    private String getJsonString(JSONObject c, String key) throws JSONException {
        String val = null;
        if (c!=null)
            val = c.getString(key);
        return val;
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

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    private void cleanCache(){
        try {
            ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
            File dirImages = wrapper.getDir(DISK_CACHE_SUBDIR,MODE_PRIVATE);
            File dirTeams = wrapper.getDir(DISK_CACHE_TEAMS,MODE_PRIVATE);
            deleteDir(dirImages);
            deleteDir(dirTeams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
