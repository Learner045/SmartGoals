package com.example.shreya.smartgoals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.shreya.smartgoals.adapters.AdapterGoals;
import com.example.shreya.smartgoals.beans.Goal;
import com.example.shreya.smartgoals.widgets.GoalRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ActivityMain extends AppCompatActivity {

    Toolbar mToolbar;
    GoalRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Goal> results;
    AdapterGoals adapterGoals;
    View mEmptyView;
    private RealmChangeListener mChangeListener =new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            adapterGoals.update(results);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Realm.init(this);             //DONE DIFFERENTLY
        mRealm=Realm.getDefaultInstance();
        results=mRealm.where(Goal.class).findAll();
        Log.i("Realm Result size",""+results.size());

        mEmptyView=findViewById(R.id.empty_goals);

        mRecycler=(GoalRecyclerView) findViewById(R.id.rv_goals);
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyView);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapterGoals=new AdapterGoals(this,results);
        mRecycler.setAdapter(adapterGoals);


    }

    @Override
    protected void onStart() {
        super.onStart();
        results.addChangeListener(mChangeListener);
    }

    public void showDialog(View view){
               showDialogAdd();
    }

    private void showDialogAdd() {
        DialogAdd dialog=new DialogAdd();
        dialog.show(getSupportFragmentManager(),"Add");
    }

    @Override
    protected void onStop() {
        super.onStop();
        results.removeChangeListener(mChangeListener);
    }
}
