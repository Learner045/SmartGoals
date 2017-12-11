package com.example.shreya.smartgoals;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.shreya.smartgoals.adapters.AdapterGoals;
import com.example.shreya.smartgoals.adapters.AddListener;
import com.example.shreya.smartgoals.adapters.CompleteListener;
import com.example.shreya.smartgoals.adapters.Divider;
import com.example.shreya.smartgoals.adapters.Filter;
import com.example.shreya.smartgoals.adapters.MarkListener;
import com.example.shreya.smartgoals.adapters.ResetListener;
import com.example.shreya.smartgoals.adapters.SimpleTouchCallBack;
import com.example.shreya.smartgoals.beans.Goal;
import com.example.shreya.smartgoals.widgets.GoalRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class ActivityMain extends AppCompatActivity {

    Toolbar mToolbar;
    GoalRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Goal> results;
    AdapterGoals adapterGoals;
    View mEmptyView;

    //INTERFACES
    private RealmChangeListener mChangeListener =new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            adapterGoals.update(results);
        }
    };
    //implementing the interface AddListener in a diffrnt way
    private AddListener mAddListener=new AddListener() {
        @Override
        public void add() {
            showDialogAdd();
        }
    };

    private MarkListener mMarkListener=new MarkListener() {
        @Override
        public void onMark(int position) {
            showDialogMark(position);
        }
    };

    //to communicate with dialogmark
    private CompleteListener mCompleteListener=new CompleteListener() {
        @Override
        public void onComplete(int position) {
            adapterGoals.markComplete(position);
        }
    };

    private ResetListener mResetListener=new ResetListener() {
        @Override
        public void onReset() {
            AppSmartGoals.save(ActivityMain.this,Filter.NONE);
            loadResuts(Filter.NONE);
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
        int filterOption=AppSmartGoals.load(this);
        loadResuts(filterOption);//loads results into realm & adds change listener to it

        mEmptyView=findViewById(R.id.empty_goals);

        mRecycler=(GoalRecyclerView) findViewById(R.id.rv_goals);
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyView);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new Divider(this,LinearLayoutManager.VERTICAL));
        adapterGoals=new AdapterGoals(this,mRealm,results); //we pass realm instance as we are deleting item from it on swipe
        adapterGoals.setHasStableIds(true);//needed for animation & getID to work
        adapterGoals.setAddListener(mAddListener); //show add dialog
        adapterGoals.setMarkListener(mMarkListener); //show mark dialog
        adapterGoals.setResetListener(mResetListener);
        mRecycler.setAdapter(adapterGoals);
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        SimpleTouchCallBack simpleTouchCallBack=new SimpleTouchCallBack(adapterGoals);
        ItemTouchHelper helper=new ItemTouchHelper(simpleTouchCallBack);
        helper.attachToRecyclerView(mRecycler); //attaching call back functionality to recy view

    }

    @Override
    protected void onStart() {
        super.onStart();
        //as per Realm docc
        results.addChangeListener(mChangeListener); //adding chnge listener to realm results
    }

    public void showDialog(View view){
               showDialogAdd();
    }

    private void showDialogMark(int position){
        DialogMark dialogMark=new DialogMark();//system will need default constructor to create frag
        //pass position through specific method
        Bundle bundle=new Bundle();
        bundle.putInt("POSITION",position);
        dialogMark.setArguments(bundle);
        dialogMark.setCompleteListener(mCompleteListener);
        dialogMark.show(getSupportFragmentManager(),"Mark");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        boolean handled=true;
        int filterOption=Filter.NONE;
        switch(id){

            case R.id.action_add:
                showDialogAdd();
                break;

            case R.id.action_sort_ascending_date:
                filterOption=Filter.LEAST_TIME_LEFT;

                break;

            case R.id.action_sort_descending_date:
                filterOption=Filter.MOST_TIME_LEFT;

                break;

            case R.id.action_show_complete:
                filterOption=Filter.COMPLETE;

                break;

            case R.id.action_show_incomplete:
                filterOption=Filter.INCOMPLETE;

                break;
            default:handled=false;
                break;
        }
        AppSmartGoals.save(this,filterOption);
        loadResuts(filterOption);
        return handled;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    //querying the realm & loading res
    private void loadResuts(int filterOption){
        switch(filterOption){
            case Filter.NONE:
                results=mRealm.where(Goal.class).findAllAsync();
                break;
            //we dont directly sort results because that will happen in main thread so we query again instead
            case Filter.LEAST_TIME_LEFT:
                results=mRealm.where(Goal.class).findAllSortedAsync("when");
                break;
            case Filter.MOST_TIME_LEFT:
                results=mRealm.where(Goal.class).findAllSortedAsync("when", Sort.DESCENDING);
                break;
            case Filter.COMPLETE:
                results=mRealm.where(Goal.class).equalTo("completed",true).findAllAsync();
                break;
            case Filter.INCOMPLETE:
                results=mRealm.where(Goal.class).equalTo("completed",false).findAllAsync();
                break;

        }
        results.addChangeListener(mChangeListener);//we have obtained new results so we add chnagelistener to it to update adapter
    }
    //saving and loading how result should be displayed- in which order


}
