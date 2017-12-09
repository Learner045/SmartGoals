package com.example.shreya.smartgoals.adapters;

/**
 * Created by Shreya on 09-12-2017.
 */

public interface Filter {

    //filter to manage sorting options

    int NONE=0; //order as per the item is added
    int MOST_TIME_LEFT=1;
    int LEAST_TIME_LEFT=2;
    int COMPLETE=3;
    int INCOMPLETE=4;
}
