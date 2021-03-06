package com.myfeup.zerozero;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MatchAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Match> arrayListChannel;
    private ViewGroup parent;
    private State state;

    public MatchAdapter(Context context, ArrayList<Match> array, State state){
        this.arrayListChannel=array;
        this.context = context;
        this.state = state;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_tvmatch2, parent, false);
        }

        int layoutVersion = 2;
        Date matchDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            matchDate = format.parse(arrayListChannel.get(i).getMatchDay());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (layoutVersion){
            case 1:
                TextView txtTop = convertView.findViewById(R.id.txtTop);
                TextView txtBottom = convertView.findViewById(R.id.txtBottom);
                TextView txtHomeTeam = convertView.findViewById(R.id.txtHomeTeam);
                TextView txtAwayTeam = convertView.findViewById(R.id.txtAwayTeam);

                SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");

                txtBottom.setText(getChannelName(this.arrayListChannel.get(i).getChannelId())+"     "+timeFormat.format(matchDate));
                txtTop.setText(this.arrayListChannel.get(i).getSportsId() + "     " + dateFormat.format(matchDate));
                txtHomeTeam.setText(this.arrayListChannel.get(i).getHomeTeam());
                txtAwayTeam.setText(this.arrayListChannel.get(i).getAwayTeam());

                if(this.arrayListChannel.get(i).getAbsfileImgHomeTeam()!= null) {
                    ImageView ivh = convertView.findViewById(R.id.imgHomeTeam);
                    ivh.setImageURI(Uri.parse(this.arrayListChannel.get(i).getAbsfileImgHomeTeam()));

                }

                if(this.arrayListChannel.get(i).getAbsfileImgAwayTeam()!=null) {
                    ImageView iva = convertView.findViewById(R.id.imgAwayTeam);
                    iva.setImageURI(Uri.parse(this.arrayListChannel.get(i).getAbsfileImgAwayTeam()));
                }
                break;
            case 2:

                TextView txtHomeTeamNew = convertView.findViewById(R.id.txtHomeTeamNew);
                TextView txtAwayTeamNew = convertView.findViewById(R.id.txtAwayTeamNew);
                TextView txtLeague = convertView.findViewById(R.id.txtLeague);
                TextView txtHour = convertView.findViewById(R.id.txtHour);
                TextView txtMinute = convertView.findViewById(R.id.txtMinute);
                TextView txtDataLong = convertView.findViewById(R.id.txtDataLong);

                SimpleDateFormat dateFormatLong;
                Calendar cal =  Calendar.getInstance();
                cal.setTime(matchDate);

                String txtDate;
                if(isToday(matchDate)){
                    dateFormatLong = new SimpleDateFormat(", d ");
                    txtDate = context.getString(R.string.today) + dateFormatLong.format(matchDate) +
                            cal.getDisplayName(Calendar.MONTH,Calendar.LONG,context.getResources().getConfiguration().locale);
                } else {
                    dateFormatLong = new SimpleDateFormat("EEE, d ");
                    txtDate = dateFormatLong.format(matchDate) +
                            cal.getDisplayName(Calendar.MONTH,Calendar.LONG,context.getResources().getConfiguration().locale);
                }

                SimpleDateFormat horaFormat = new SimpleDateFormat("H");
                SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

                txtHour.setText(horaFormat.format(matchDate));
                txtMinute.setText(minuteFormat.format(matchDate));
                txtDataLong.setText(txtDate);

                txtHomeTeamNew.setText(this.arrayListChannel.get(i).getHomeTeam());
                txtAwayTeamNew.setText(this.arrayListChannel.get(i).getAwayTeam());
                txtLeague.setText(getChannelName(this.arrayListChannel.get(i).getChannelId()));

                if(this.arrayListChannel.get(i).getAbsfileImgHomeTeam()!= null) {
                    ImageView ivhN = convertView.findViewById(R.id.imgHomeTeamNew);
                    ivhN.setImageURI(Uri.parse(this.arrayListChannel.get(i).getAbsfileImgHomeTeam()));
                }

                if(this.arrayListChannel.get(i).getAbsfileImgAwayTeam()!=null) {
                    ImageView ivaN = convertView.findViewById(R.id.imgAwayTeamNew);
                    ivaN.setImageURI(Uri.parse(this.arrayListChannel.get(i).getAbsfileImgAwayTeam()));
                }

                TextView txtTvChannel = convertView.findViewById(R.id.txtTvChannel);
                txtTvChannel.setText(getChannelName(this.arrayListChannel.get(i).getChannelId()));

                ArrayList<TvChannel> tvChannels = state.getArrayListChannel();
                for(int k = 0; k< tvChannels.size();k++){
                    if(tvChannels.get(k).getId()==this.arrayListChannel.get(i).getChannelId()){
                        if(tvChannels.get(k).getAbsImgFileName()!= null) {
                            ImageView ivhT = convertView.findViewById(R.id.imgTvChannel);
                            ivhT.setImageURI(Uri.parse(tvChannels.get(k).getAbsImgFileName()));
                        }
                    }
                }
                break;
        }

        // returns the view for the current row
        return convertView;
    }

    public static boolean isToday(Date date){
        Calendar today = Calendar.getInstance();
        Calendar specifiedDate  = Calendar.getInstance();
        specifiedDate.setTime(date);

        return today.get(Calendar.DAY_OF_MONTH) == specifiedDate.get(Calendar.DAY_OF_MONTH)
                &&  today.get(Calendar.MONTH) == specifiedDate.get(Calendar.MONTH)
                &&  today.get(Calendar.YEAR) == specifiedDate.get(Calendar.YEAR);
    }

    private String getChannelName(int channelId){
        for(int k=0; k<state.getArrayListChannel().size(); k++){
            if(state.getArrayListChannel().get(k).getId()==channelId){
                return state.getArrayListChannel().get(k).getName();
            }
        }
        return null;
    }
}
