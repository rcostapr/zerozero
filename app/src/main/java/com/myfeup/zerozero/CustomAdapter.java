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

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TvChannel> arrayListChannel;
    private ViewGroup parent;

    public CustomAdapter(Context context, ArrayList<TvChannel> array){
        this.arrayListChannel=array;
        this.context = context;
    }
    @Override
    public int getCount() {
        return arrayListChannel.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListChannel.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_tvchannel, parent, false);
        }

        TextView txtView1 = convertView.findViewById(R.id.chntxt1);
        TextView txtView2 = convertView.findViewById(R.id.chntxt2);
        txtView1.setText(Integer.toString(this.arrayListChannel.get(i).getId()));
        txtView2.setText(this.arrayListChannel.get(i).getName());

        if(this.arrayListChannel.get(i).getAbsImgFileName()!=null) {
            ImageView iv = convertView.findViewById(R.id.imgChannelId);
            iv.setImageURI(Uri.parse(this.arrayListChannel.get(i).getAbsImgFileName()));
        }

        // returns the view for the current row
        return convertView;
    }
}
