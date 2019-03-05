package com.myfeup.zerozero;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MatchAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Match> arrayListChannel;
    private ViewGroup parent;

    public MatchAdapter(Context context, ArrayList<Match> array){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_tvmatch, parent, false);
        }

        TextView txtTop = convertView.findViewById(R.id.txtTop);
        TextView txtBottom = convertView.findViewById(R.id.txtBottom);
        TextView txtHomeTeam = convertView.findViewById(R.id.txtHomeTeam);
        TextView txtAwayTeam = convertView.findViewById(R.id.txtAwayTeam);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date matchDate = null;
        try {
            matchDate = format.parse(arrayListChannel.get(i).getMatchDay());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");

        txtBottom.setText(this.arrayListChannel.get(i).getChannel()+"     "+timeFormat.format(matchDate));
        txtTop.setText(this.arrayListChannel.get(i).getSports() + "     " + dateFormat.format(matchDate));
        txtHomeTeam.setText(this.arrayListChannel.get(i).getHomeTeam());
        txtAwayTeam.setText(this.arrayListChannel.get(i).getAwayTeam());

        if(this.arrayListChannel.get(i).getAbsfileImgHomeTeam()!= null) {
            ImageView ivh = convertView.findViewById(R.id.imgHomeTeam);
            ivh.setImageURI(Uri.parse(this.arrayListChannel.get(i).getAbsfileImgHomeTeam()));
        }
        //Log.e("ARRAY",this.arrayListChannel.get(i).getAbsfileImgHomeTeam());

        if(this.arrayListChannel.get(i).getAbsfileImgAwayTeam()!=null) {
            ImageView iva = convertView.findViewById(R.id.imgAwayTeam);
            iva.setImageURI(Uri.parse(this.arrayListChannel.get(i).getAbsfileImgAwayTeam()));
        }
        // returns the view for the current row
        return convertView;
    }
}
