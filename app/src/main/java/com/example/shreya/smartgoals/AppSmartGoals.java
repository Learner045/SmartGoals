package com.example.shreya.smartgoals;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.shreya.smartgoals.adapters.Filter;

/**
 * Created by Shreya on 10-12-2017.
 */

public class AppSmartGoals extends Application {



    public static void save(Context context, int filterOption){


        SharedPreferences pef =  PreferenceManager.getDefaultSharedPreferences(context);
        pef.edit().putInt("filter",filterOption).apply();//ASYNC
    }

    public static int load(Context context){

        SharedPreferences pef =  PreferenceManager.getDefaultSharedPreferences(context);
        return pef.getInt("filter", Filter.NONE);
    }
}
