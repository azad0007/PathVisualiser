package com.example.pathvisualiser;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class tutorial_2 extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.tutorial_2);

    }

    public void onclick(View view){
        Intent intent = new Intent(this,tutorial_3.class) ;
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
    }

}
