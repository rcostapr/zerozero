package com.myfeup.zerozero;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog mProgressDialog;

    private Context mContext;
    private Activity mActivity;

    private Button btn, btnClose;
    private ListView channelList;
    private ArrayList<TvChannel> arrayListChannel;

    private URL urlTVChannel = null;
    private JSONObject jsonTVChannel = null;

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "Images";

    private CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnCanais);
        btnClose = findViewById(R.id.btnClose);
        channelList = findViewById(R.id.lstCanais);
        arrayListChannel = new ArrayList<>();
        customAdapter = new CustomAdapter(this,arrayListChannel);
        channelList.setAdapter(customAdapter);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        // Initialize the progress dialog
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setIndeterminate(true);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Progress dialog title
        mProgressDialog.setTitle("Updating Application");
        // Progress dialog message
        mProgressDialog.setMessage("Please wait, we are downloading new actualization...");

        try {
            urlTVChannel = new URL("http://www.zerozero.pt/api/v1/getTVChannel/AppKey/6RaJ9G7G/DomainID/pt");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        arrayListChannel.clear();
        new getJson().execute(urlTVChannel);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayListChannel.clear();
                new getJson().execute(urlTVChannel);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        channelList.setClickable(true);
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent infoChannel = new Intent(mContext, info_channel.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tvChannel", arrayListChannel.get(i));
                //infoChannel.putExtras(bundle);
                infoChannel.putExtra("tvChannel",arrayListChannel.get(i));
                //infoChannel.putExtra("arrayListChannel", arrayListChannel);
                //infoChannel.putExtra("tvChannel", arrayListChannel.get(i));
                startActivity(infoChannel);
            }
        });

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
        if (jsonStr != null) {
            try {
               jsonObj = new JSONObject(jsonStr);
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
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
                tvchannels = mydata.getJSONArray("TVCHANNEL");
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
                if (c != null) {
                    String channel_id = null;
                    try {
                        channel_id = c.getString("CHANNELID");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String channel = null;
                    try {
                        channel = c.getString("CHANNEL");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String domain = null;
                    try {
                        domain = c.getString("DOMAIN");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String img_path = null;
                    String fileName = null;
                    try {
                        img_path = c.getString("IMGPATH");
                        fileName = img_path.substring(img_path.lastIndexOf('/') + 1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String img_height = null;
                    try {
                        img_height = c.getString("IMGHEIGHT");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String img_width = null;
                    try {
                        img_width = c.getString("IMGWIDTH");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // tmp hash map for single contact
                    HashMap<String, String> channels = new HashMap<>();

                    // adding each child node to HashMap key => value
                    channels.put("channel_id", channel_id);
                    channels.put("channel", channel);
                    channels.put("domain", domain);
                    channels.put("img_path", img_path);
                    channels.put("img_height", img_height);
                    channels.put("img_width", img_width);

                    Log.d(TAG, channel_id + " - " + channel + " - " + img_path + " - " + fileName);
                    TvChannel nChannel = new TvChannel(Integer.parseInt(channel_id),
                            channel, img_path, Integer.parseInt(img_width),
                            Integer.parseInt(img_height), domain);

                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    File file = wrapper.getDir(DISK_CACHE_SUBDIR,MODE_PRIVATE);
                    file = new File(file, fileName);

                    if (!file.exists()) {
                        // TODO: Get Image and Cache Image TvChannel
                        // Execute the async task
                        new DownloadTask(nChannel).execute(img_path, fileName);
                    } else {
                        nChannel.setAbsImgFileName(file.getAbsolutePath());
                    }
                    arrayListChannel.add(nChannel);
                }

            }
            customAdapter.notifyDataSetChanged();
            //Log.d(TAG,"Downloaded " + result + " bytes");
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

}

