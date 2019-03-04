package com.myfeup.zerozero;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class info_settings extends AppCompatActivity {

    private static final String FILE_STATE = "out.txt";

    private String TAG = info_settings.class.getSimpleName();

    private Context mContext;
    private Activity mActivity;

    private State state = new State(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_settings);
        setTitle(R.string.action_settings);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = info_settings.this;

        // GET File Status
        File file = getApplicationContext().getFileStreamPath(FILE_STATE);
        if(file.exists()) {
            try {
                Log.d(TAG,"File Exists State -> " + Integer.toString(state.getState()));
                this.state = getState();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(!state.getIdioma().equals("default")) {
            //Change Application level locale
            LocaleHelper.setLocale(info_settings.this, state.getIdioma(),state.getIdiomaCountry());
        }

        Button btn = findViewById(R.id.btnIdiomas);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIdiomas = new Intent(mContext, info_settings_idioma.class);
                startActivity(settingsIdiomas);
            }
        });


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
