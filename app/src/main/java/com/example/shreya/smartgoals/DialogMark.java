package com.example.shreya.smartgoals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.shreya.smartgoals.adapters.CompleteListener;

/**
 * Created by Shreya on 20-11-2017.
 */

public class DialogMark extends DialogFragment {

    private ImageButton mBtnClose;
    private Button mBtnCompleted;
    private CompleteListener mCompleteListener;

    private View.OnClickListener mBtnClickListener=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.btn_completed:
                    markAsComplete();
                    break;


            }
            dismiss();
        }
    };

    private void markAsComplete() {
        Bundle arguments=getArguments();//args contains position of item to show mark dialog for it

        if(mCompleteListener!=null && arguments!=null){
            int position=arguments.getInt("POSITION");
            mCompleteListener.onComplete(position);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_mark,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnClose=(ImageButton)view.findViewById(R.id.btn_close);
        mBtnCompleted=(Button)view.findViewById(R.id.btn_completed);
        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnCompleted.setOnClickListener(mBtnClickListener);



    }

    public void setCompleteListener(CompleteListener completeListener) {
        this.mCompleteListener = completeListener;
    }
}
