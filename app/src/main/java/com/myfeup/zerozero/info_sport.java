package com.myfeup.zerozero;

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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class info_sport extends AppCompatActivity {

    private String TAG = info_sport.class.getSimpleName();
    private static final String DISK_CACHE_SUBDIR = "Teams";

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private Activity mActivity;

    private JSONObject jsonTVChannel = null;

    private ArrayList<Match> arrayListChannel;
    private Sport tvSport;
    private URL urlChannel;
    private ListView channelList;
    private MatchAdapter matchAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String COLOR_BACKGROUND = "#FF4081";
    private static final String FILE_STATE = "out.txt";
    private State state = new State(1);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_channel);

        boolean isConnected = checkInternetConnection();
        File file = getApplicationContext().getFileStreamPath(FILE_STATE);
        if(file.exists()) {
            try {
                Log.d(TAG,"File Exists State -> " + Integer.toString(state.getState()));
                this.state = getState();
                Log.d("ArrayTvSportList","Before SIZE -> " + this.state.getArrayTvChannelList().size());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(!state.getIdioma().equals("default")) {
            //Change Application level locale
            LocaleHelper.setLocale(info_sport.this, state.getIdioma(),state.getIdiomaCountry());
        }

        switch (state.getState()){
            case 1:

                break;
            case 2:
                this.setTitle(R.string.back_to_sports);
                break;
            default:
                break;
        }

        channelList = findViewById(R.id.lstMatch);
        arrayListChannel = new ArrayList<>();
        matchAdapter = new MatchAdapter(this,arrayListChannel);
        channelList.setAdapter(matchAdapter);
        channelList.setClickable(true);
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent infoMatch = new Intent(mContext, MatchInfo.class);
                infoMatch.putExtra("tvMatch",arrayListChannel.get(i));
                startActivity(infoMatch);
            }
        });

        // Get the application context
        mContext = getApplicationContext();
        mActivity = info_sport.this;

        // Initialize the progress dialog
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setIndeterminate(true);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Progress dialog title
        mProgressDialog.setTitle(getString(R.string.updating_app));
        // Progress dialog message
        mProgressDialog.setMessage(getString(R.string.downloading_new));

        Intent intent = getIntent();
        this.tvSport =  (Sport) intent.getSerializableExtra("tvSports");

        TextView txtView1 = findViewById(R.id.chntxt1);
        TextView txtView2 = findViewById(R.id.chntxt2);
        txtView1.setText(Integer.toString(tvSport.getId()));
        txtView2.setText(tvSport.getName());
        ImageView iv = findViewById(R.id.imgChannelId);
        iv.setImageResource(R.drawable.ic_video_camera);

        this.urlChannel = getUrl(getString(R.string.urlChannel)
                + Integer.toString(tvSport.getId()));

        swipeRefreshLayout=findViewById(R.id.swiperefreshsport);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor(COLOR_BACKGROUND));

        // Register swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                boolean isConnected = checkInternetConnection();

                if (isConnected) {
                    arrayListChannel.clear();
                    new getJson().execute(urlChannel);
                } else {
                    // TODO
                    importTvListFromCache();
                }
            }
        });

        if (isConnected) {
            new getJson().execute(this.urlChannel);
        } else {
            // TODO Try to get from cache
            importTvListFromCache();
        }
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
                tvchannels = mydata.getJSONArray("ZAPPING");
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
                    String channelId = getJsonString(c,"CHANNELID");
                    String domain = getJsonString(c,"DOMAIN");
                    String matchDay = getJsonString(c,"DATA");
                    String homeTeamId = getJsonString(c,"HOMETEAMID");
                    String awayTeamId = getJsonString(c,"AWAYTEAMID");
                    String homeTeam = getJsonString(c,"HOMETEAM");
                    String awayTeam = getJsonString(c,"AWAYTEAM");
                    String sportsId = getJsonString(c,"SPORTSID");
                    String channel = getJsonString(c,"CHANNEL");
                    String sports = getJsonString(c,"SPORTS");
                    String imgHomeTeam = getJsonString(c,"HOMETEAM_LOGO");
                    String fileImgHomeTeam = imgHomeTeam.substring(imgHomeTeam.lastIndexOf('/') + 1);
                    String imgAwatTeam = getJsonString(c,"AWAYTEAM_LOGO");
                    String fileImgAwatTeam = imgAwatTeam.substring(imgAwatTeam.lastIndexOf('/') + 1);

                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    File file1 = wrapper.getDir(DISK_CACHE_SUBDIR,MODE_PRIVATE);
                    file1 = new File(file1, fileImgHomeTeam);
                    File file2 = wrapper.getDir(DISK_CACHE_SUBDIR,MODE_PRIVATE);
                    file2 = new File(file2, fileImgAwatTeam);



                    Log.d(TAG, channelId + " - " + homeTeam + " - " + awayTeam + " - " + matchDay);
                    Match nMatch = new Match(Integer.parseInt(matchId),
                            Integer.parseInt(channelId),
                            domain,
                            matchDay,
                            Integer.parseInt(homeTeamId),
                            Integer.parseInt(awayTeamId),
                            homeTeam,
                            awayTeam,
                            Integer.parseInt(sportsId),
                            channel,
                            sports);


                    if (!file1.exists()) {
                        new DownloadTask(nMatch,1).execute(imgHomeTeam, fileImgHomeTeam);
                    } else {
                        nMatch.setAbsfileImgHomeTeam(file1.getAbsolutePath());
                    }
                    if (!file2.exists()) {
                        new DownloadTask(nMatch,2).execute(imgAwatTeam, fileImgAwatTeam);
                    } else {
                        nMatch.setAbsfileImgAwayTeam(file2.getAbsolutePath());
                    }


                    arrayListChannel.add(nMatch);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            TvSportList nTvSportList = new TvSportList(tvSport.getId(),arrayListChannel);
            state.addArrayTvSportList(nTvSportList);
            try {
                saveState(state);
            } catch (IOException e) {
                e.printStackTrace();
            }
            matchAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);

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

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
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
            // Hide the progress dialog
            mProgressDialog.dismiss();
            matchAdapter.notifyDataSetChanged();
        }
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

        View view = findViewById(R.id.lst2Canais);

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        Log.d("CONNECTIVITY" , "INTERNET -> " + isConnected);
        if(!isConnected){
            if(!state.isInternetStatus()) {
                // Setting Toast
                Context context = getApplicationContext();
                CharSequence text = getResources().getText(R.string.no_internet);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        } else {
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            state.setWiFi(isWiFi);
        }
        state.setInternetStatus(isConnected);
        return isConnected;
    }

    private void importTvListFromCache(){
        ArrayList<TvSportList> arrayTvSportList = state.getArrayTvSportList();
        for(int k=0;k<arrayTvSportList.size();k++){
            if(arrayTvSportList.get(k).getId()==tvSport.getId()){
                this.arrayListChannel = arrayTvSportList.get(k).getArrayListChannel();
                matchAdapter = new MatchAdapter(this,arrayListChannel);
                channelList.setAdapter(matchAdapter);
                break;
            }
        }
        swipeRefreshLayout.setRefreshing(false);
    }
}
