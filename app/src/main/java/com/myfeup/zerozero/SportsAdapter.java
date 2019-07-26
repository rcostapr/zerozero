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
    private ArrayList<Competition> arrayListSport;
    private ViewGroup parent;

    public SportsAdapter(Context context, ArrayList<Competition> array){
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

        TextView txtView = convertView.findViewById(R.id.chntxt);
        txtView.setText(this.arrayListSport.get(i).getName());

        if(this.arrayListSport.get(i).getAbsImgFileName()!=null) {
            ImageView iv = convertView.findViewById(R.id.imgSport);
            iv.setImageURI(Uri.parse(this.arrayListSport.get(i).getAbsImgFileName()));
        }

        // returns the view for the current row
        return convertView;
    }
}
