package com.example.pathvisualiser;

import android.content.ClipData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {

    private LayoutInflater mInflater ;
    private int[] mData ;

    public MyRecyclerViewAdapter(Context context,int[] Data){
        mInflater = LayoutInflater.from(context);
        mData = Data;

    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.grid_cell_layout,parent,false) ;
        return new CustomViewHolder(view) ;

    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.CustomViewHolder holder, int position) {

        holder.gridCell.setVisibility(View.VISIBLE);

        if(  mData[position] == Utilities.CellStates.startCell )
            holder.gridCell.setImageResource(R.drawable.startcell);

        else if( mData[position] == Utilities.CellStates.destCell )
            holder.gridCell.setImageResource(R.drawable.endcell);

        else if( mData[position] == Utilities.CellStates.visCellinBombSearch )
            holder.gridCell.setImageResource(R.drawable.visitedinbombsearchgridcell);

        else if( mData[position] == Utilities.CellStates.visCellinDesSearch )
            holder.gridCell.setImageResource(R.drawable.visitedindestsearchgridcell);

        else if( mData[position] == Utilities.CellStates.BombCell )
            holder.gridCell.setImageResource(R.drawable.bombcell);

        else if( mData[position] == Utilities.CellStates.WallCell )
            holder.gridCell.setImageResource(R.drawable.wallcell);

        else if( mData[position] == Utilities.CellStates.WeightCell )
            holder.gridCell.setImageResource(R.drawable.weightcell);

        else if( mData[position] == Utilities.CellStates.finalPath )
            holder.gridCell.setImageResource(R.drawable.finalpath);

        else if( mData[position] == Utilities.CellStates.unVisitedCell )
            holder.gridCell.setImageResource(R.drawable.gridcell);

        else if ( mData[position] == Utilities.CellStates.visWeightCell )
            holder.gridCell.setImageResource(R.drawable.visitedweightcell);

        else if ( mData[position] == Utilities.CellStates.visDestCell )
            holder.gridCell.setImageResource(R.drawable.visiteddestination);

        else if( mData[position] == Utilities.CellStates.visBombCell )
            holder.gridCell.setImageResource(R.drawable.visitedbombcell);

        else if( mData[position] == Utilities.CellStates.manWithBike )
            holder.gridCell.setImageResource(R.drawable.man_with_bike);

        return ;
    }

    @Override
    public int getItemCount() {
        return (mData.length);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnDragListener,View.OnTouchListener  {

        ImageView gridCell ;
        public CustomViewHolder(View itemView) {
            super(itemView);
            gridCell = (ImageView)itemView.findViewById(R.id.ID_cell) ;
            itemView.setOnClickListener(this::onClick);
            itemView.setOnDragListener(this::onDrag);
            itemView.setOnTouchListener(this::onTouch);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            if(position<0)
                return;

            if( mData[position] == Utilities.CellStates.unVisitedCell ){

                if( MainActivity.currItem == Utilities.Items.WALL )
                    mData[position] = Utilities.CellStates.WallCell ;

                else
                    mData[position] = Utilities.CellStates.WeightCell ;

                notifyDataSetChanged();
            }

            else if( mData[position] == Utilities.CellStates.WallCell || mData[position] == Utilities.CellStates.WeightCell  ){
                mData[position] = Utilities.CellStates.unVisitedCell ;
                notifyDataSetChanged();
            }
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //Log.d("imp", "onTouch: ");
            int pos = getAdapterPosition();
            if( pos < 0 )
                return true ;
            if( mData[pos] == Utilities.CellStates.startCell || mData[pos] == Utilities.CellStates.BombCell || mData[pos] == Utilities.CellStates.destCell  ){

                MainActivity.value_Drag = mData[pos];
                MainActivity.pos_Drag = pos ;

                final ClipData data = ClipData.newPlainText("value","");
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v) ;
                v.startDrag(data,dragShadowBuilder,v,0);
                return true;
            }
            onClick(v);
            return true;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {

            int action = event.getAction();

            if(action==DragEvent.ACTION_DROP){
                int pos = getAdapterPosition();
                if(pos<0)
                    return true;

                mData[pos] = MainActivity.value_Drag;

                if(pos!=MainActivity.pos_Drag)
                    mData[MainActivity.pos_Drag] = Utilities.CellStates.unVisitedCell;

                updatePostion(pos) ;

                notifyDataSetChanged();
                return true;
            }

            return true;
        }


    }

    private void updatePostion(int pos){

        if( mData[pos] == Utilities.CellStates.destCell)
            MainActivity.DestPos=pos;
        else if( mData[pos] == Utilities.CellStates.BombCell )
            MainActivity.bombPos=pos;
        else
            MainActivity.StartPos=pos;
    }

    public void clearAll(){
        for(int i=0;i<MainActivity.Rows*MainActivity.Cols;i++)
                mData[i] = Utilities.CellStates.unVisitedCell;

        mData[MainActivity.StartPos] = Utilities.CellStates.startCell;
        mData[MainActivity.DestPos] = Utilities.CellStates.destCell;
        mData[MainActivity.bombPos] = Utilities.CellStates.BombCell;

        notifyDataSetChanged();

    }

    public void clearPath(){
        for(int i=0;i<MainActivity.Rows*MainActivity.Cols;i++){
            if(mData[i]!=Utilities.CellStates.visWeightCell && mData[i] != Utilities.CellStates.WeightCell && mData[i] != Utilities.CellStates.WallCell )
                mData[i] = Utilities.CellStates.unVisitedCell;
            else if( mData[i] == Utilities.CellStates.visWeightCell )
                mData[i] = Utilities.CellStates.WeightCell;
        }

        mData[MainActivity.StartPos] = Utilities.CellStates.startCell;
        mData[MainActivity.DestPos] = Utilities.CellStates.destCell;
        mData[MainActivity.bombPos] = Utilities.CellStates.BombCell;

        notifyDataSetChanged();
    }

    public void move(){

        new UpdateChanges().execute();

    }

    public class UpdateChanges extends AsyncTask<Void,Void, Integer>{


        @Override
        protected Integer doInBackground(Void... voids) {

            int r = (MainActivity.StartPos)/MainActivity.Cols;
            int c = (MainActivity.StartPos)%MainActivity.Cols;

            int check = Traversals.Traverse(r,c,this,mData) ;


            return check;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Integer check) {
            super.onPostExecute(check);

            MainActivity.ShowDialog(check);
        }


        public void doProgress(){
            publishProgress();
        }

    }



}
