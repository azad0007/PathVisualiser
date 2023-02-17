package com.example.pathvisualiser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.widget.NumberPicker;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private NumberPicker numberPickerTurn,numberPickerMove,numberPickerTraffic;
    private SeekBar waitingTimeSearch,waitingTimeBike;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);



        numberPickerTurn = (NumberPicker)findViewById(R.id.ID_turn_cost);
        numberPickerTurn.setMinValue(0);
        numberPickerTurn.setMaxValue(200);
        numberPickerTurn.setWrapSelectorWheel(true);

        numberPickerTurn.setValue(MainActivity.turnCost);

        numberPickerTurn.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                MainActivity.turnCost = newVal;
            }
        });

        numberPickerMove = (NumberPicker)findViewById(R.id.ID_move_cost);
        numberPickerMove.setMinValue(0);
        numberPickerMove.setMaxValue(200);
        numberPickerMove.setWrapSelectorWheel(true);

        numberPickerMove.setValue(MainActivity.moveCost);

        numberPickerMove.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                MainActivity.moveCost = newVal;
            }
        });


        numberPickerTraffic = (NumberPicker)findViewById(R.id.ID_traffic_cost);
        numberPickerTraffic.setMinValue(0);
        numberPickerTraffic.setMaxValue(200);
        numberPickerTraffic.setWrapSelectorWheel(true);

        numberPickerTraffic.setValue(MainActivity.WeightValue);

        numberPickerTraffic.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                MainActivity.WeightValue = newVal;
            }
        });





        waitingTimeSearch = (SeekBar)findViewById(R.id.ID_seekbar_searchspeed);
        waitingTimeSearch.setMin(1);
        waitingTimeSearch.setMax(450);
        waitingTimeSearch.setProgress( 450-MainActivity.WaitingTimeSearch );

        waitingTimeSearch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int waitingTime = 450-progress;
                MainActivity.WaitingTimeSearch=waitingTime;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        waitingTimeBike = (SeekBar)findViewById(R.id.ID_seekbar_bikespeed);
        waitingTimeBike.setMin(1);
        waitingTimeBike.setMax(450);
        waitingTimeBike.setProgress( 450-MainActivity.WaitingTimeBike );

        waitingTimeBike.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int waitingTime = 450-progress;
                MainActivity.WaitingTimeBike=waitingTime;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });







    }
}
