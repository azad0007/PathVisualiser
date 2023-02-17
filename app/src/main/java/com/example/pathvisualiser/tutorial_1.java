package com.example.pathvisualiser;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public class tutorial_1 extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tutorial_1);

    }

    public void onclick(View view){
        Intent intent = new Intent(this,tutorial_2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
    }
}
