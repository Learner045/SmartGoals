package com.example.shreya.smartgoals.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.example.shreya.smartgoals.extras.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Shreya on 13-11-2017.
 */

public class GoalRecyclerView extends RecyclerView{

    private List<View> mNonEmptyViews= Collections.emptyList();
    private List<View> mEmptyViews= Collections.emptyList();

    private AdapterDataObserver mObserver= new AdapterDataObserver() {
        @Override
        public void onChanged() {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleViews();
        }
    };

    private void toggleViews() {
        if(getAdapter()!=null && !mEmptyViews.isEmpty() && !mNonEmptyViews.isEmpty()){

            if(getAdapter().getItemCount()==0){
                //no item to display so show layout with empty view

                //show all empty views
                Util.showViews(mEmptyViews);

                //hide Recyclerview
                setVisibility(View.GONE);

                //hide all views which are meant to be hidden
                Util.hideViews(mNonEmptyViews);

            }else{
                //items to display so show layout with items & toolbar & add-button

                //hide all empty views
                Util.hideViews(mEmptyViews);

                //show recyclerview
                setVisibility(View.VISIBLE);

                //hide all views which are meant to be hidden
                Util.showViews(mNonEmptyViews);

            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if(adapter!=null){
            adapter.registerAdapterDataObserver(mObserver);
        }
        mObserver.onChanged();
    }

    public GoalRecyclerView(Context context) {
        super(context);
    }

    public GoalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GoalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void hideIfEmpty(View ...views) {
        //toolbar
        mNonEmptyViews= Arrays.asList(views);
    }

    public void showIfEmpty(View ...views) {
        //empty layout with button & image
        mEmptyViews= Arrays.asList(views);

    }
    
}
