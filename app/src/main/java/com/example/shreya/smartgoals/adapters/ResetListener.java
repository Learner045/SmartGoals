package com.example.shreya.smartgoals.adapters;

/**
 * Created by Shreya on 10-12-2017.
 */

public interface ResetListener {
    //to reset the screen to home screen when there are truly no items to display even in complete/incomplete mode
    //instead of showing the "no item to displ messg"
    void onReset();
}
