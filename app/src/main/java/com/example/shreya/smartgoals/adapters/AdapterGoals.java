package com.example.shreya.smartgoals.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shreya.smartgoals.AppSmartGoals;
import com.example.shreya.smartgoals.R;
import com.example.shreya.smartgoals.beans.Goal;
import com.example.shreya.smartgoals.extras.Util;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Shreya on 08-11-2017.
 */

public class AdapterGoals extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener{

    private LayoutInflater mInflater;
    private RealmResults<Goal> mResults;

    public static final int COUNT_FOOTER=1;
    public static final int COUNT_NO_ITEMS=1;

    //these represent diff views we want
    private static final int ITEM=0; //normal row
    public static final int NO_ITEM=1;//row "No items to disp"
    public static final int FOOTER=2;//footer row
    private int filterOption;

    private AddListener mAddListener;
    private MarkListener markListener;

    private Realm mRealm;
    private Context context;

    public AdapterGoals(Context context, RealmResults<Goal> results){
        mInflater=LayoutInflater.from(context); //we get inflater here because our onCreateView is called multiple times throughtout
       update(results);
    }
    public AdapterGoals(Context context,Realm realm, RealmResults<Goal> results){
        this.context=context;
        mRealm=realm;
        mInflater=LayoutInflater.from(context); //we get inflater here because our onCreateView is called multiple times throughtout
        update(results);

    }

    public void setAddListener(AddListener listener){
        mAddListener=listener;
    }
    public void setMarkListener(MarkListener markListener){this.markListener=markListener;}

    public void update(RealmResults<Goal>results){
        mResults=results;
        filterOption= AppSmartGoals.load(context);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==FOOTER){

            View view=mInflater.inflate(R.layout.footer,parent,false);
            return new FooterHolder(view);
        }else if(viewType==NO_ITEM){
            View view=mInflater.inflate(R.layout.no_item,parent,false);
            return new NoItemsHolder(view);
        }
        else{
            View view=mInflater.inflate(R.layout.row_goal,parent,false);
            return new GoalHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        //if there are items then display normally like earlier
       if(!mResults.isEmpty()){
           if(position<mResults.size()){
               return ITEM;
           }else{
               return FOOTER;
           }
       }else{
           //no items but we are under complte & incomplete screens
           if(filterOption==Filter.COMPLETE || Filter.INCOMPLETE==filterOption){
               if(position==0){

                   return NO_ITEM;//display the row"no item to display"

               }else{
                   return FOOTER; //display footer if pos 1
               }

           }else{
               return ITEM; //default
           }
       }
    }

    private static ArrayList<String> generateValues(){
        ArrayList<String>dummyValues=new ArrayList<>();
        for(int i=0;i<100;i++){
            dummyValues.add("Item "+i);
        }
        return dummyValues;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof GoalHolder){

           GoalHolder goalHolder=(GoalHolder)holder;
            Goal goal=mResults.get(position);
            goalHolder.mTextWhat.setText(goal.getWhat());//done differently
            goalHolder.mTextWhen.setText(processTime(goal.getWhen()));
            goalHolder.setBackground(goal.isCompleted());//change color
        }

    }

    private String processTime(long when) {
        // our goal date, current time, days diff ,abbreviate everything
        return ""+ DateUtils.getRelativeTimeSpanString(when,System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS,DateUtils.FORMAT_ABBREV_ALL);
    }

    @Override
    public int getItemCount() {
       // return mResults.size();

      if(!mResults.isEmpty()){

          return mResults.size()+COUNT_FOOTER;

      }else{

          //if we are trying to check ascending/descending view &
          // there are no items in the database then we need to show empty screen as earlier
          if(filterOption==Filter.LEAST_TIME_LEFT || filterOption==Filter.MOST_TIME_LEFT || filterOption==Filter.NONE){
              return 0;

          }else{
              //we display "No  items to display" and a footer
              return COUNT_NO_ITEMS+COUNT_FOOTER;
          }
      }

    }

    @Override
    public void onSwipe(int position) {
        //del item but NOT FOOTER
        if(position<mResults.size()){
            mRealm.beginTransaction();
            mResults.get(position).deleteFromRealm();
            mRealm.commitTransaction();
            notifyItemRemoved(position);
        }

    }

    public void markComplete(int position) {

        if(position<mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).setCompleted(true);
            mRealm.commitTransaction();
            notifyDataSetChanged();
        }
    }

    public  class GoalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextWhat,mTextWhen;
        View itemview;

        public GoalHolder(View itemView) {
            super(itemView);
            itemview=itemView;
            context=itemView.getContext();
            itemView.setOnClickListener(this); //setting listener on row
            mTextWhat= (TextView)itemView.findViewById(R.id.tv_what);
            mTextWhen=(TextView)itemView.findViewById(R.id.tv_when);

        }

        @Override
        public void onClick(View view) {
            //we need to show dialog_mark but fragment manager is avail to activity
            markListener.onMark(getAdapterPosition());
        }

        public void setBackground(boolean completed) {
            //changing color for views marked as complete
            Drawable drawable;
            if(completed){
               drawable= ContextCompat.getDrawable(context,R.color.bg_goal_complete);
            }else{
               drawable= ContextCompat.getDrawable(context,R.drawable.bg_row_goal);
            }

            Util.setBackground(itemView,drawable);

        }
    }
    public class NoItemsHolder extends RecyclerView.ViewHolder {

        public NoItemsHolder(View itemView) {
            super(itemView);
        }
    }
    public  class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       Button mBtnAdd;
        public FooterHolder(View itemView) {
            super(itemView);
            mBtnAdd=(Button)itemView.findViewById(R.id.btn_footer);
            mBtnAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mAddListener.add();//showing dialog to add & adding data from it
        }
    }
}
