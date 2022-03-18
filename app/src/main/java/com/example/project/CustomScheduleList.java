package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomScheduleList extends ArrayAdapter<String> {
    private Context context;
    private String[] name;
    private String[] job;
    private String[] date;
    private String[] datedetails;
    private String[] time;
    private String[] timedetails;
    private String[] rate;
    private String[] ratedetails;
    private Integer [] imgid;
    public CustomScheduleList(Context context, String[] name, String[] job, String[] date, String[] datedetails, String[] time, String[] timedetails, String[] rate, String[] ratedetails, Integer [] imgid) {
        super(context, R.layout.schedule_content,name);

        this.context=context;
        this.name=name;
        this.job=job;
        this.date=date;
        this.datedetails=datedetails;
        this.time=time;
        this.timedetails=timedetails;
        this.rate=rate;
        this.ratedetails=ratedetails;
        this.imgid=imgid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;

        if(view==null){
            LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.schedule_content,null,true);
           // view.setTag(viewHolder);
        }
       // else{
         //   viewHolder=(ViewHolder)view.getTag();
       // }
        ViewHolder viewHolder=new ViewHolder();
        viewHolder.imageView=(ImageView) view.findViewById(R.id.Image);
        viewHolder.t1=(TextView) view.findViewById(R.id.name);
        viewHolder.t2=(TextView) view.findViewById(R.id.job);
        viewHolder.t3=(TextView) view.findViewById(R.id.date);
        viewHolder.t4=(TextView) view.findViewById(R.id.datedetals);
        viewHolder.t5=(TextView) view.findViewById(R.id.time);
        viewHolder.t6=(TextView) view.findViewById(R.id.timedetals);
        viewHolder.t7=(TextView) view.findViewById(R.id.rate);
        viewHolder.t8=(TextView) view.findViewById(R.id.ratedetails);

        viewHolder.t1.setText(name[position]);
        viewHolder.t2.setText(job[position]);
        viewHolder.t3.setText(date[position]);
        viewHolder.t4.setText(datedetails[position]);
        viewHolder.t5.setText(time[position]);
        viewHolder.t6.setText(timedetails[position]);
        viewHolder.t7.setText(rate[position]);
        viewHolder.t8.setText(ratedetails[position]);
        viewHolder.imageView.setImageResource(imgid[position]);
        return view;
    }

    class ViewHolder
    {
        TextView t1;
        TextView t2;
        TextView t3;
        TextView t4;
        TextView t5;
        TextView t6;
        TextView t7;
        TextView t8;
        ImageView imageView;
    }
}
