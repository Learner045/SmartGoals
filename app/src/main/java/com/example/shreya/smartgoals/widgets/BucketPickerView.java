package com.example.shreya.smartgoals.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shreya.smartgoals.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Shreya on 12-12-2017.
 */

public class BucketPickerView extends LinearLayout {
    private TextView mTextDate, mTextMonth, mTextYear;
    private Calendar calendar;
    private SimpleDateFormat mFormatter;

    public BucketPickerView(Context context) {
        super(context);
        init(context);
    }

    public BucketPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BucketPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //getting view from xml
        View view= LayoutInflater.from(context).inflate(R.layout.bucket_picker_view,this);
        calendar=Calendar.getInstance();
        mFormatter=new SimpleDateFormat("MMM");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //setting today's date in our date picker
        mTextDate=(TextView)this.findViewById(R.id.tv_date);
        mTextMonth=(TextView)this.findViewById(R.id.tv_month);
        mTextYear=(TextView)this.findViewById(R.id.tv_year);
        int date=calendar.get(Calendar.DATE);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);
        update(date,month,year,0,0,0);
    }

    private void update(int date,int month,int year,int hour,int minute,int second){
        calendar.set(Calendar.DATE,date);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.HOUR,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);

        mTextDate.setText(date+"");
        mTextMonth.setText(mFormatter.format(calendar.getTime()));
        mTextYear.setText(year+"");
    }

    public long getTime(){
        return calendar.getTimeInMillis();
    }
}
