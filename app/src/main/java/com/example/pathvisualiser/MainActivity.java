package com.example.pathvisualiser;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static int Rows = 20 ; // for storing the number of rows
    public static int Cols = 10 ; // for storing the number of cols
    public static int StartPos = 25 ; // starting pos
    public static int DestPos = 80 ; // destination pos
    private static Button moveButton ; // visualise algorithm
    private RecyclerView mrecyclerView;  // recycler view
    private static int[] mData; // temporary dataset
    private static ArrayList<String> arrayList; // to store the values which are displayed as list in the spinner
    private static Spinner spinnerAlgorithm ; // spinner algorithm
    private static Spinner spinnerItems ; // spinner Items
    private static Spinner spinnerMaze ; // spinner maze
    public static Context context ;
    public static int currAlgorithm = Utilities.Algorithm.BFS ; // to store the algorithm selected
    public static int currItem = Utilities.Items.WALL ; // to store the item selected
    public static int bombPos = 0 ; // bomb position
    private static HashMap<String,Integer> matchStringToId ; // key = name of algo,item and values = value present in utilities
    public static int WeightValue = 15 ;
    public static int turnCost = 5 ;
    public static int moveCost = 1 ;
    public static int WaitingTimeSearch = 300 ;
    public static int WaitingTimeBike = 150 ;
    private static MyRecyclerViewAdapter myRecyclerViewAdapter ;
    private static GridLayoutManager gridLayoutManager;
    public static int value_Drag = 0;
    public static int pos_Drag = 0 ;
    public static int totalTime = 0;
    public static int totalDistance = 0;
    public static int totalTurns=0;
    public static int totalTraffic=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setEnterTransition(new Slide());

        setContentView(R.layout.activity_main);



        context=this;

        initialiseGridConstants() ;

        initialiseViews();

        setRecyclerView();


    }

    private void setRecyclerView(){
        mData = setData();
        // getting the recycler view into memory
        mrecyclerView = (RecyclerView)findViewById(R.id.ID_recyclerView);
        // getting instance of the adapter

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this,mData);
        // layout manager initialisation
        gridLayoutManager = new GridLayoutManager(this,Cols) ; // creates GDLM vertical scrollable
        // Connect the adapter and layout manager with the recycler view object

        mrecyclerView.setLayoutManager(gridLayoutManager);

        mrecyclerView.setAdapter(myRecyclerViewAdapter);

    }

    private void initialiseViews(){
        spinnerAlgorithm = (Spinner)findViewById(R.id.ID_algorithm_selector);
        spinnerItems = (Spinner)findViewById(R.id.ID_item_selector) ;
        matchStringToId = new HashMap<String,Integer>() ;

        setArrayListAlgorithms(); // fill the required array list with names of algo

        setSpinner(spinnerAlgorithm); // set the arrayList to the spinner of algos
        addClickListenerAlgorithm(spinnerAlgorithm) ; // set the onItemSelectListener to algorithm selector

        setArrayListItems() ;    // fill the required array list with names of items

        setSpinner(spinnerItems) ; // set the arrayList to the spinner of items
        addClickListenerItems(spinnerItems) ;   // set the onItemSelectListener to items selector

        moveButton = (Button)findViewById(R.id.Move_ID_BUTTON) ;

        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                myRecyclerViewAdapter.clearPath();
                myRecyclerViewAdapter.move();
            }
        }) ;

    }

    private static void initialiseGridConstants(){

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / 37 + 0.5);

        screenWidthDp = displayMetrics.heightPixels / displayMetrics.density;
        int noOfRows = (int)(screenWidthDp/37+0.5) ;

        MainActivity.Rows = noOfRows;
        MainActivity.Cols = noOfColumns;


    }

    private static int[] setData(){

        int []data = new int[Rows*Cols] ;

        data[StartPos] = Utilities.CellStates.startCell ;
        data[DestPos] = Utilities.CellStates.destCell ;
        data[bombPos] = Utilities.CellStates.BombCell ;

        return data ;
    }

    private static void setArrayListAlgorithms(){

        arrayList = new ArrayList<String>() ;

        arrayList.add("BFS") ;
        arrayList.add("DFS") ;
        arrayList.add("DIJSKTRA") ;

        matchStringToId.put("DFS",Utilities.Algorithm.DFS) ;
        matchStringToId.put("BFS",Utilities.Algorithm.BFS) ;
        matchStringToId.put("DIJSKTRA",Utilities.Algorithm.DJISKTRA) ;

        return ;
    }

    private static void setArrayListItems(){
        arrayList = new ArrayList<String>() ;

        arrayList.add("WALL") ;
        arrayList.add("WEIGHT") ;

        matchStringToId.put("WALL",Utilities.Items.WALL);
        matchStringToId.put("WEIGHT",Utilities.Items.WEIGHT);

        return;
    }

    private static void setSpinner(Spinner spinner){

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
    }

    private static void addClickListenerAlgorithm(Spinner spinner){


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String algorithm = parent.getItemAtPosition(position).toString() ;

                int Id = matchStringToId.get(algorithm) ;

                currAlgorithm = Id ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private static void addClickListenerItems(Spinner spinner){


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String algorithm = parent.getItemAtPosition(position).toString() ;

                int Id = matchStringToId.get(algorithm) ;

                currItem = Id ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if( item.getItemId() == R.id.ID_settings ){
            Intent intent = new Intent(this,Settings.class) ;
            startActivity(intent);
        }
        else if( item.getItemId() == R.id.ID_clear_path ){
            myRecyclerViewAdapter.clearPath();
        }
        else if( item.getItemId() == R.id.ID_clear_all ){
            myRecyclerViewAdapter.clearAll();
        }

        else if( item.getItemId() == R.id.ID_info ){
            Intent intent = new Intent(this,tutorial_1.class) ;
            startActivity(intent);
        }

        return true;
    }

    public static void ShowDialog(int reached){

        CustomDialog dialog = new CustomDialog(context,reached);
        dialog.show();
    }


}
