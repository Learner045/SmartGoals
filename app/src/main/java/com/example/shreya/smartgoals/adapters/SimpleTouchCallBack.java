package com.example.shreya.smartgoals.adapters;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Shreya on 16-11-2017.
 */

public class SimpleTouchCallBack extends ItemTouchHelper.Callback {

    private SwipeListener listener;
    public SimpleTouchCallBack(SwipeListener swipeListener) {
        listener=swipeListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,ItemTouchHelper.END); //we don't want drag (up/down) & we swipe right
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;//called when drag happens
    }

    //both methods allow only items to move by passing the moved coords in super
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        //changing the default behaviour to stop footer & no_item from getting swiped
        if(viewHolder instanceof AdapterGoals.GoalHolder) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }
    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(viewHolder instanceof AdapterGoals.GoalHolder) {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //called when swipe happens
        if(viewHolder instanceof AdapterGoals.GoalHolder){
            //its an item
            listener.onSwipe(viewHolder.getAdapterPosition());
        }

    }
}
