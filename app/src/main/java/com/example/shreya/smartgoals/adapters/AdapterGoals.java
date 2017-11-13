package com.example.shreya.smartgoals.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AdapterGoals extends RecyclerView.Adapter<AdapterGoals.GoalHolder>{

    private LayoutInflater mInflater;
    private RealmResults<Goal> mResults;

    public AdapterGoals(Context context, RealmResults<Goal> results){
        mInflater=LayoutInflater.from(context); //we get inflater here because our onCreateView is called multiple times throughtout
       update(results);
    }

    public void update(RealmResults<Goal>results){
        mResults=results;
        notifyDataSetChanged();
    }

    @Override
    public GoalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.row_goal,parent,false);
        GoalHolder holder=new GoalHolder(view);
        return holder;
    }

    private static ArrayList<String> generateValues(){
        ArrayList<String>dummyValues=new ArrayList<>();
        for(int i=0;i<100;i++){
            dummyValues.add("Item "+i);
        }
        return dummyValues;
    }

    @Override
    public void onBindViewHolder(GoalHolder holder, int position) {
        Goal goal=mResults.get(position);
        holder.mTextWhat.setText(goal.getWhat());


    }

    @Override
    public int getItemCount() {
       // return mResults.size();
        return mResults.size();
    }

    public static class GoalHolder extends RecyclerView.ViewHolder{

        TextView mTextWhat,mTextWhen;
        public GoalHolder(View itemView) {
            super(itemView);
            mTextWhat= (TextView)itemView.findViewById(R.id.tv_what);
            mTextWhen=(TextView)itemView.findViewById(R.id.tv_when);

        }
    }
}
