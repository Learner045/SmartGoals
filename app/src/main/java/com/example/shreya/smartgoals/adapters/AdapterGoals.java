package com.example.shreya.smartgoals.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shreya.smartgoals.R;

import java.util.ArrayList;

/**
 * Created by Shreya on 08-11-2017.
 */

public class AdapterGoals extends RecyclerView.Adapter<AdapterGoals.GoalHolder>{

    private LayoutInflater mInflater;
    private ArrayList<String> mItems=new ArrayList<>();
    public AdapterGoals(Context context){
        mInflater=LayoutInflater.from(context); //we get inflater here because our onCreateView is called multiple times throughtout
        mItems=generateValues();
    }

    @Override
    public GoalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.row_goal,parent,false);
        GoalHolder holder=new GoalHolder(view);
        return holder;
    }

    public static ArrayList<String> generateValues(){
        ArrayList<String>dummyValues=new ArrayList<>();
        for(int i=0;i<100;i++){
            dummyValues.add("Item "+i);
        }
        return dummyValues;
    }

    @Override
    public void onBindViewHolder(GoalHolder holder, int position) {
        holder.mTextWhat.setText(mItems.get(position));

    }

    @Override
    public int getItemCount() {
        return mItems.size();
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
