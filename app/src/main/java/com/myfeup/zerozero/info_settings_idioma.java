package com.myfeup.zerozero;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class info_settings_idioma extends AppCompatActivity {

    private static final String FILE_STATE = "out.txt";

    private String TAG = info_settings_idioma.class.getSimpleName();

    private Context mContext;
    private Activity mActivity;

    private State state = new State(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_settings_idioma);
        setTitle(R.string.action_settings);
        // Get the application context
        mContext = getApplicationContext();
        mActivity = info_settings_idioma.this;

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
            LocaleHelper.setLocale(info_settings_idioma.this, state.getIdioma(),state.getIdiomaCountry());
        }

        RadioButton rBtnDefault = findViewById(R.id.radioBtnDefault);
        RadioButton rBtnPt = findViewById(R.id.radioBtnPt);
        RadioButton rBtnEs = findViewById(R.id.radioBtnEs);
        RadioButton rBtnBr = findViewById(R.id.radioBtnBr);

        if (state.getIdioma().equals("default")){
            rBtnDefault.setChecked(true);
        } else if (state.getIdioma().equals("pt") && state.getIdiomaCountry().equals("PT")){
            rBtnPt.setChecked(true);
        } else if (state.getIdioma().equals("es") && state.getIdiomaCountry().equals("ES")){
            rBtnEs.setChecked(true);
        } else if (state.getIdioma().equals("pt") && state.getIdiomaCountry().equals("BR")){
            rBtnBr.setChecked(true);
        }

    }

    public void onRadioButtonClicked(View view) throws IOException {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioBtnDefault:
                if (checked){
                    Log.d(TAG,"Default");
                    state.setIdioma("default");
                    saveState(state);}
                    break;
            case R.id.radioBtnPt:
                if (checked){
                    Log.d(TAG,"PT");
                    state.setIdioma("pt");
                    state.setIdiomaCountry("PT");
                    saveState(state);}
                    break;
            case R.id.radioBtnEs:
                if (checked){
                    Log.d(TAG,"ES");
                    state.setIdioma("es");
                    state.setIdiomaCountry("ES");
                    saveState(state);}
                    break;
            case R.id.radioBtnBr:
                if (checked){
                    state.setIdioma("pt");
                    state.setIdiomaCountry("BR");
                    saveState(state);
                    Log.d(TAG,"BR");}
                    break;
        }
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