package com.example.shreya.smartgoals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ActivityMain extends AppCompatActivity {

    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    public void showDialog(View view){
               showDialogAdd();
    }

    private void showDialogAdd() {
        DialogAdd dialog=new DialogAdd();
        dialog.show(getSupportFragmentManager(),"Add");
    }
}
