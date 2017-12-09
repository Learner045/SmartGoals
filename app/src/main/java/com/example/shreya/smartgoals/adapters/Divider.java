package com.example.shreya.smartgoals.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.shreya.smartgoals.R;

/**
 * Created by Shreya on 16-11-2017.
 */

public class Divider extends RecyclerView.ItemDecoration {

    //anything around recycler view can be thought of as decoration
    //there's a specific class fot that use..ItemDecoration

    private int mOrientation; //we have to keep track of orientation of linear layout which is used in arranging items of recycler view
    private Drawable mDivider;

    public Divider(Context context,int mOrientation){
        mDivider= ContextCompat.getDrawable(context,R.drawable.divider);
        if(mOrientation!= LinearLayoutManager.VERTICAL){
            throw new IllegalArgumentException("This item decoration can only be used for LinearLayoutManager with vertical orientation");
        }
        this.mOrientation=mOrientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
       if(mOrientation==LinearLayoutManager.VERTICAL){
           drawHorizontalDivider(c,parent,state);
       }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int left,top,right,bottom;
        left=parent.getPaddingLeft(); //if we have set padding to recyclerview..we have to take that into account
        right=parent.getWidth()-parent.getPaddingRight();

        int childCount=parent.getChildCount(); //getting total no. of children in recyclerview

        for(int i=0;i<childCount;i++){

            if(AdapterGoals.FOOTER!=parent.getAdapter().getItemViewType(i))
            {
                View current=parent.getChildAt(i); //getting current child

                RecyclerView.LayoutParams params=(RecyclerView.LayoutParams) current.getLayoutParams(); //to get margin for els of recy view

                top=current.getBottom()+params.bottomMargin;
                bottom=top+mDivider.getIntrinsicHeight(); //height mentioned in xml file

                mDivider.setBounds(left,top,right,bottom);
                mDivider.draw(c);
            }
           //we only draw divier if the el of recy view is not footer
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(mOrientation==LinearLayoutManager.VERTICAL){
            outRect.contains(0,0,0,mDivider.getIntrinsicHeight());
        }
    }

}
