package com.example.shreya.smartgoals.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shreya.smartgoals.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Shreya on 12-12-2017.
 */

public class BucketPickerView extends LinearLayout implements View.OnTouchListener {

    private TextView mTextDate, mTextMonth, mTextYear;
    private Calendar calendar;
    private SimpleDateFormat mFormatter;

    //drawable positions in array LTRB
    public static final int LEFT=0;
    public static final int TOP=1;
    public static final int RIGHT=2;
    public static final int BOTTOM=3;

    private int MESSAGE_WHAT = 123;//distinguishes multiple messgs when sent to handler, here random
    private boolean mIncrement;
    private boolean mDecrement;

    private int activeId; //stores view's id which is currently being touched
    public static final int DELAY=250;

    private Handler mHandler = new Handler(new Handler.Callback(){

        @Override
        public boolean handleMessage(Message message) {
            if(mIncrement){
                increment(activeId);
            }
            if(mDecrement){
                decrement(activeId);
            }

            if(mIncrement || mDecrement){
                mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT,DELAY);
                //creates a loop as long as view is int pressed/touch state
            }
            return true;
        }

    });

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
    protected Parcelable onSaveInstanceState() {

        Bundle bundle=new Bundle();
        bundle.putParcelable("super",super.onSaveInstanceState()); //we need to also return config setup for linearlayout in superclass
        bundle.putInt("date",calendar.get(Calendar.DATE));
        bundle.putInt("month",calendar.get(Calendar.MONTH));
        bundle.putInt("year",calendar.get(Calendar.YEAR));
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Parcelable){
            Bundle bundle=(Bundle)state;
            state=bundle.getParcelable("super");
            int date=bundle.getInt("date");
            int month=bundle.getInt("month");
            int year=bundle.getInt("year");
            update(date,month,year,0,0,0);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //setting today's date in our date picker
        mTextDate=(TextView)this.findViewById(R.id.tv_date);
        mTextMonth=(TextView)this.findViewById(R.id.tv_month);
        mTextYear=(TextView)this.findViewById(R.id.tv_year);

        //to detect user touch when user tries to change the date/month/year
        mTextDate.setOnTouchListener(this);
        mTextMonth.setOnTouchListener(this);
        mTextYear.setOnTouchListener(this);

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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch(view.getId()){
            case R.id.tv_month:
                processEventsFor(mTextMonth,motionEvent);
                break;
            case R.id.tv_date:
                processEventsFor(mTextDate,motionEvent);
                break;
            case R.id.tv_year:
                processEventsFor(mTextYear,motionEvent);
                break;
        }
        return true;
    }

    private void processEventsFor(TextView textview,MotionEvent event){

        //L T R B
        Drawable drawables[]=textview.getCompoundDrawables();

        if(hasDrawableTop(drawables) && hasDrawableBottom(drawables)){

            Rect topBounds=drawables[TOP].getBounds(); //LTRB
            Rect bottomBounds=drawables[BOTTOM].getBounds();

            float x=event.getX();
            float y=event.getY();
            activeId= textview.getId(); //required so handler can increment value

            if(topDrawableHit(textview, topBounds.height(),x,y)){
                //all actions corresponding to incrementing values
                if(isActionDown(event)){
                    mIncrement=true;
                    increment(textview.getId());
                    mHandler.removeMessages(MESSAGE_WHAT); //removes already sent messgs when press down happens again
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT,DELAY); //messg arrives after a specific delay
                    toggleDrawable(textview,true);
                }

                if(isActionUpOrCancel(event)){
                    mIncrement=false;
                    toggleDrawable(textview,false);
                }


            }else if(bottomDrawableHit(textview, bottomBounds.height(), x,y)){
                //all actions corresponding to decrementing values
                if(isActionDown(event)){
                    mDecrement=true;
                    decrement(textview.getId());
                    mHandler.removeMessages(MESSAGE_WHAT);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT,DELAY);
                    toggleDrawable(textview,true);
                }

                if(isActionUpOrCancel(event)){
                    mDecrement=false;
                    toggleDrawable(textview,false);
                }

            }else{
                //reset
                mIncrement=false;
                mDecrement=false;
                toggleDrawable(textview,false);
            }
        }


    }

    //changing vals in calendar & in textviews
    private void increment(int id){

        switch(id){
            case R.id.tv_month:
                calendar.add(Calendar.MONTH,1);
                break;
            case R.id.tv_date:
                calendar.add(Calendar.DATE,1);
                break;
            case R.id.tv_year:
                calendar.add(Calendar.YEAR,1);
                break;
        }

        set(calendar);
    }

    private void set(Calendar calendar) {
        int date=calendar.get(Calendar.DATE);
        int year=calendar.get(Calendar.YEAR);
        mTextDate.setText(date+"");
        mTextMonth.setText(mFormatter.format(calendar.getTime()));
        mTextYear.setText(year+"");
    }

    private void decrement(int id){

        switch(id){
            case R.id.tv_month:
                calendar.add(Calendar.MONTH,-1);
                break;
            case R.id.tv_date:
                calendar.add(Calendar.DATE,-1);
                break;
            case R.id.tv_year:
                calendar.add(Calendar.YEAR,-1);
                break;
        }
        set(calendar);
    }

    private boolean isActionDown(MotionEvent event){
        return event.getAction()==MotionEvent.ACTION_DOWN;
    }

    private boolean isActionUpOrCancel(MotionEvent event){
        return event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL;
    }

    private boolean topDrawableHit(TextView textview, int drawableHeight, float x, float y){

        int xmin=textview.getPaddingLeft();
        int xmax=textview.getWidth()-textview.getPaddingRight();
        int ymin=textview.getPaddingTop();
        int ymax=textview.getPaddingTop()+drawableHeight;

        return x>xmin && x<xmax && y>ymin && y<ymax;
    }

    private boolean bottomDrawableHit(TextView textview, int drawableHeight, float x, float y){

        int xmin=textview.getPaddingLeft();
        int xmax=textview.getWidth()-textview.getPaddingRight();
        int ymax=textview.getHeight()-textview.getPaddingBottom();
        int ymin=ymax-drawableHeight;

        return x>xmin && x<xmax && y>ymin && y<ymax;
    }

    private boolean hasDrawableTop(Drawable[] drawables){
        return drawables[TOP]!=null;
    }
    private boolean hasDrawableBottom(Drawable[] drawables){
        return drawables[BOTTOM]!=null;
    }


    private void toggleDrawable(TextView textView, boolean pressed){
        if(pressed){
            //which drawable is pressed
            if(mIncrement){
                textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_pressed,0,R.drawable.down_normal);
            }else{
                //decrement pressed
                textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_normal,0,R.drawable.down_pressed);
            }

        }else{
            //reset as nothing is pressed
            textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_normal,0,R.drawable.down_normal);
        }
    }


}
