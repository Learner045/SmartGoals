package com.example.shreya.smartgoals.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shreya.smartgoals.R;
import com.example.shreya.smartgoals.beans.Goal;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Shreya on 08-11-2017.
 */

public class AdapterGoals extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mInflater;
    private RealmResults<Goal> mResults;

    public static final int ITEM=0;
    public static final int FOOTER=1;

    public AdapterGoals(Context context, RealmResults<Goal> results){
        mInflater=LayoutInflater.from(context); //we get inflater here because our onCreateView is called multiple times throughtout
       update(results);
    }

    public void update(RealmResults<Goal>results){
        mResults=results;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==FOOTER){

            View view=mInflater.inflate(R.layout.footer,parent,false);
           return new FooterHolder(view);
        }else{
            View view=mInflater.inflate(R.layout.row_goal,parent,false);
            return new GoalHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(mResults==null || position<mResults.size() ){
            return ITEM;
        }
         return FOOTER;

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
            goalHolder.mTextWhat.setText(goal.getWhat());
        }

    }

    @Override
    public int getItemCount() {
       // return mResults.size();
        return mResults.size()+1; //+1 because we have footer too
    }

    public static class GoalHolder extends RecyclerView.ViewHolder{

        TextView mTextWhat,mTextWhen;
        public GoalHolder(View itemView) {
            super(itemView);
            mTextWhat= (TextView)itemView.findViewById(R.id.tv_what);
            mTextWhen=(TextView)itemView.findViewById(R.id.tv_when);

        }
    }
    public static class FooterHolder extends RecyclerView.ViewHolder{

       Button mBtnAdd;
        public FooterHolder(View itemView) {
            super(itemView);
            mBtnAdd=(Button)itemView.findViewById(R.id.btn_footer);
        }
    }
}
