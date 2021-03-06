package com.example.shreya.smartgoals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.shreya.smartgoals.beans.Goal;
import com.example.shreya.smartgoals.widgets.BucketPickerView;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Shreya on 06-11-2017.
 */

public class DialogAdd extends DialogFragment {

    private ImageButton mBtnClose;
    private EditText mInputWhat;
    private BucketPickerView mInputWhen;
    private Button mBtnAdd;

    private View.OnClickListener mBtnListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id= view.getId();
            switch(id){
                case R.id.btn_add_it:
                    addAction();
                    break;
                
            }
            dismiss();
        }
    };

    //adding data to database here
    private void addAction() {
        String what=mInputWhat.getText().toString();

        long now=System.currentTimeMillis();

        Realm.init(getActivity());             //DONE DIFFERENTLY
        Realm realm=Realm.getDefaultInstance();
        Goal goal=new Goal(what,now, mInputWhen.getTime() ,false);
        realm.beginTransaction();
        realm.copyToRealm(goal);
        realm.commitTransaction();
        realm.close();
    }

    public DialogAdd(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting style to our date picker widget
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnClose=(ImageButton) view.findViewById(R.id.btn_close);
        mInputWhat=(EditText)view.findViewById(R.id.et_goal);
        mInputWhen=(BucketPickerView) view.findViewById(R.id.gpv_date);
        mBtnAdd=(Button)view.findViewById(R.id.btn_add_it);

        mBtnClose.setOnClickListener(mBtnListener);
        mBtnAdd.setOnClickListener(mBtnListener);
    }
}
