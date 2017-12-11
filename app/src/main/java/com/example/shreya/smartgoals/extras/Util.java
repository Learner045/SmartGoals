package com.example.shreya.smartgoals.extras;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import java.util.List;

/**
 * Created by Shreya on 13-11-2017.
 */

public class Util {
    public static void showViews(List<View> views){

        for(View view:views){
            view.setVisibility(View.VISIBLE);
        }
    }
    public static void hideViews(List<View> views){

        for(View view:views){
            view.setVisibility(View.GONE);
        }
    }

    public static boolean isMoreThanJellyBean(){
       return  Build.VERSION.SDK_INT>15;
    }

    public static void setBackground(View view, Drawable drawable){
        if(isMoreThanJellyBean()){
            view.setBackground(drawable);
        }else{
            view.setBackgroundDrawable(drawable);
        }
    }
}
