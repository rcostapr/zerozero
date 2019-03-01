package com.myfeup.zerozero;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SportsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Sport> arrayListSport;
    private ViewGroup parent;

    public SportsAdapter(Context context, ArrayList<Sport> array){
        this.arrayListSport=array;
        this.context = context;
    }
    @Override
    public int getCount() {
        return arrayListSport.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListSport.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_sports, parent, false);
        }

        TextView txtView1 = convertView.findViewById(R.id.chntxt1);
        TextView txtView2 = convertView.findViewById(R.id.chntxt2);
        txtView1.setText(Integer.toString(this.arrayListSport.get(i).getId()));
        txtView2.setText(this.arrayListSport.get(i).getName());

        // returns the view for the current row
        return convertView;
    }
}
