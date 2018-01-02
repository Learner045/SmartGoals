package com.example.shreya.smartgoals;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.example.shreya.smartgoals.adapters.Filter;

/**
 * Created by Shreya on 10-12-2017.
 */

public class AppSmartGoals extends Application {


      //filteroption based on item selected from menu
    public static void save(Context context, int filterOption){

        SharedPreferences pef =  PreferenceManager.getDefaultSharedPreferences(context);
        pef.edit().putInt("filter",filterOption).apply();//ASYNC
    }

    public static int load(Context context){

        SharedPreferences pef =  PreferenceManager.getDefaultSharedPreferences(context);
        return pef.getInt("filter", Filter.NONE);
    }

    public static void setRalewayRegular(Context context, TextView textView){
        Typeface typeface= Typeface.createFromAsset(context.getAssets(),"fonts/ralewaythin.ttf");
        textView.setTypeface(typeface);
    }

    public static void setRalewayRegular(Context context, TextView ...textViews){
        Typeface typeface= Typeface.createFromAsset(context.getAssets(),"fonts/ralewaythin.ttf");
        for(TextView textView:textViews){
            textView.setTypeface(typeface);
        }

    }
}
