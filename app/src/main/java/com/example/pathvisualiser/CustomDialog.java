package com.example.pathvisualiser;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomDialog extends Dialog {

    private TextView textView;
    private ImageView imageView;


    public CustomDialog(Context context,int reached) {
        super(context);

        setContentView(R.layout.dialog_layout);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        textView = (TextView) findViewById(R.id.ID_tv_explore);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        if(reached==1)
            reachedSuccessfully() ;
        else
            failedToReach() ;

    }

    private void reachedSuccessfully(){
        textView = (TextView)findViewById(R.id.ID_tv_distance);
        textView.setVisibility(View.VISIBLE);
        textView.setText("DISTANCE\n"+MainActivity.totalDistance + "m");

        textView = (TextView)findViewById(R.id.ID_tv_traffics);
        textView.setVisibility(View.VISIBLE);
        textView.setText("TRAFFIC\n"+MainActivity.totalTraffic+"s");

        textView = (TextView)findViewById(R.id.ID_tv_time);
        textView.setVisibility(View.VISIBLE);
        textView.setText("TIME\n"+MainActivity.totalTime+"s");

        textView = (TextView)findViewById(R.id.ID_tv_turns);
        textView.setVisibility(View.VISIBLE);
        textView.setText("TURNS\n"+MainActivity.totalTurns);
    }

    private void failedToReach(){

        imageView = (ImageView)findViewById(R.id.ID_iv_IMG_OOPS_FAIL);
        imageView.setVisibility(View.VISIBLE);

        textView = (TextView)findViewById(R.id.ID_tv_failedReaching);
        textView.setVisibility(View.VISIBLE);
        textView.setText("OOPS CAN'T FIND A WAY");

    }


}
