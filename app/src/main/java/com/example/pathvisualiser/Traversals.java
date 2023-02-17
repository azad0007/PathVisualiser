package com.example.pathvisualiser;

import android.util.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;

public class Traversals {

    private static int[][] vis ;
    private static ArrayList<Integer> pathAL ;
    private static int check = 0 ;
    private static int[] dir = {1,0,-1,0,0,1,0,-1} ;

    public static int Traverse(int r, int c, MyRecyclerViewAdapter.UpdateChanges updateChanges, int []mData){

        vis = new int[MainActivity.Rows][MainActivity.Cols] ;
        pathAL = new ArrayList<Integer>() ;
        check = 0 ;
        int type = MainActivity.currAlgorithm;

        if(type==Utilities.Algorithm.DFS) {
            dfs(r, c, updateChanges, mData, MainActivity.bombPos,Utilities.CellStates.visCellinBombSearch);
            if(check==0)
                return 0;
            r = MainActivity.bombPos/MainActivity.Cols;
            c = MainActivity.bombPos%MainActivity.Cols;
            check=0;
            vis = new int[MainActivity.Rows][MainActivity.Cols] ;
            dfs(r,c,updateChanges,mData,MainActivity.DestPos,Utilities.CellStates.visCellinDesSearch) ;
        }

        else if( type == Utilities.Algorithm.DJISKTRA ){

            djisktra(r,c,updateChanges,mData,MainActivity.bombPos,Utilities.CellStates.visCellinBombSearch);

            if( check == 0 )
                return 0;

            check=0;
            r = MainActivity.bombPos/MainActivity.Cols;
            c = MainActivity.bombPos%MainActivity.Cols;

            djisktra(r,c,updateChanges,mData,MainActivity.DestPos,Utilities.CellStates.visCellinDesSearch);
        }

        else if( type == Utilities.Algorithm.BFS ){

            bfs(r,c,updateChanges,mData,MainActivity.bombPos,Utilities.CellStates.visCellinBombSearch);

            if( check == 0 )
                return 0;

            check=0;
            r = MainActivity.bombPos/MainActivity.Cols;
            c = MainActivity.bombPos%MainActivity.Cols;

            bfs(r,c,updateChanges,mData,MainActivity.DestPos,Utilities.CellStates.visCellinDesSearch);
        }

        if(check==0)
            return 0;

        Pair<Integer,Pair<Integer,Integer>> Details = getDetails(mData);

        MainActivity.totalTime = (int) Details.first;
        MainActivity.totalDistance = pathAL.size();
        MainActivity.totalTurns = Details.second.first;
        MainActivity.totalTraffic = Details.second.second;

        generatePath(mData,updateChanges);

        return 1;
    }

    private static void generatePath(int []mData,MyRecyclerViewAdapter.UpdateChanges updateChanges){

        if(pathAL.size()==0)
            return;

        int prev=MainActivity.StartPos;

        int pickedUp = 0 ;

        int i;

        mData[MainActivity.StartPos] = Utilities.CellStates.unVisitedCell;

        for(i=1;i<pathAL.size();i++){

            if( mData[pathAL.get(i)] == Utilities.CellStates.visBombCell )
                pickedUp=1;

            if(pickedUp==0)
                mData[pathAL.get(i)] = Utilities.CellStates.startCell ;
            else
                mData[pathAL.get(i)] = Utilities.CellStates.manWithBike ;

            updateVal(mData,Utilities.CellStates.finalPath,prev);

            updateChanges.doProgress();

            waitSometime(MainActivity.WaitingTimeBike);

            prev = pathAL.get(i) ;

        }

        mData[pathAL.get(i-1)] = Utilities.CellStates.BombCell;

        updateChanges.doProgress();

    }

    private static void waitSometime(int delay){
        Date date = new Date();
        long curr = date.getTime();

        long temp = curr ;

        while(temp<curr+delay){
            date = new Date();
            temp = date.getTime() ;
        }
    }

    private static Pair getDetails(int[]mData){

        int Time = 0 ;
        int Turns=0;
        int TrafficTime=0;

        int i=0;
        for(i=0;i<pathAL.size();i++){

            int currPos = pathAL.get(i);

            if(i>=2) {
                int prev_prevPos = pathAL.get(i - 2);
                int prevPos = pathAL.get(i - 1);

                if (is90(currPos, prevPos, prev_prevPos)) {
                    Time += MainActivity.turnCost;
                    Turns++;
                }

                else
                    Time += MainActivity.moveCost;
            }

            else
                Time += MainActivity.moveCost;

            if( mData[currPos] == Utilities.CellStates.visWeightCell ) {
                Time += MainActivity.WeightValue;
                TrafficTime+=MainActivity.WeightValue;
            }

        }

        return new Pair(Time,new Pair(Turns,TrafficTime));
    }

    public static void dfs(int r, int c, MyRecyclerViewAdapter.UpdateChanges updateChanges, int[]mData,int Target,int val){

        if(r < 0 || c < 0 || r>= MainActivity.Rows || c >= MainActivity.Cols )
            return ;

        if( vis[r][c] == 1 )
            return ;

        int pos = r*(MainActivity.Cols)+c ;

        if( mData[pos] == Utilities.CellStates.WallCell )
            return;

        if( pos == Target ) {
            pathAL.add(pos) ;
            check=1;
            return;
        }

        vis[r][c] = 1 ;

        updateVal(mData,val,pos);

        pathAL.add(pos) ;

        updateChanges.doProgress();

        waitSometime(MainActivity.WaitingTimeSearch);

        for(int di=0;di<dir.length;di+=2) {

            int rnew = r + dir[di];
            int cnew = c + dir[di+1];

            if (check == 1)
                break;

            dfs(rnew, cnew, updateChanges, mData, Target, val);
        }

        if( check==0 && pathAL.size()>=0 )
            pathAL.remove(pathAL.size()-1) ;

    }

    public static void djisktra(int r,int c,MyRecyclerViewAdapter.UpdateChanges updateChanges,int[]mData,int Target,int val){

        for(int i=0;i<MainActivity.Rows;i++) {
            for (int j = 0; j < MainActivity.Cols; j++) {
                vis[i][j] = (int) (1e9 + 5);
            }
        }

        vis[r][c]=0;


        int[] parent = new int[(MainActivity.Rows*MainActivity.Cols)];

        Arrays.fill(parent,-1);

        PriorityQueue< Pair<Integer, Pair<Integer,Integer>  > > pQ = new PriorityQueue<>(new customComparator()) ;

        pQ.add(  new Pair( 0 , new Pair( r , c ) )  ) ;



        while( pQ.isEmpty() == false ){

            Pair<Integer, Pair<Integer,Integer>  > P = pQ.poll();

            r = P.second.first;
            c = P.second.second;

            int pos = r*MainActivity.Cols+c ;

            if( pos == Target ){
                check=1;
                break ;
            }

            updateVal(mData,val,pos);
            updateChanges.doProgress();

            waitSometime(MainActivity.WaitingTimeSearch);

            int W = P.first ;

            for(int di=0;di<dir.length;di+=2){

                int rnew = r+dir[di] ;
                int cnew = c+dir[di+1] ;

                if(rnew < 0 || cnew < 0 || rnew>= MainActivity.Rows || cnew >= MainActivity.Cols )
                    continue ;

                int newPos = rnew*MainActivity.Cols+cnew ;

                if( mData[newPos] == Utilities.CellStates.WallCell )
                    continue;

                int w = 0;

                if( mData[newPos] == Utilities.CellStates.WeightCell )
                    w += MainActivity.WeightValue;

                if( is90(newPos,pos,parent[pos]) == true )
                    w += MainActivity.turnCost;
                else
                    w += MainActivity.moveCost;

                if( vis[rnew][cnew] > W+w ){
                    vis[rnew][cnew] = W+w ;

                    pQ.add( new Pair(vis[rnew][cnew], new Pair(rnew,cnew) )  ) ;

                    parent[newPos] = pos;
                }

            }

        }

        ArrayList<Integer> tempAL = new ArrayList<>() ;

        if(check==1){

            while( Target != -1 ){
                tempAL.add(Target);
                Target=parent[Target];
            }

        }

        Collections.reverse(tempAL);

        pathAL.addAll(tempAL);

        return;
    }

    public static void bfs(int r, int c, MyRecyclerViewAdapter.UpdateChanges updateChanges, int[]mData,int Target,int val){

        for(int i=0;i<MainActivity.Rows;i++){
            for(int j=0;j<MainActivity.Cols;j++){
                vis[i][j] = 0 ;
            }
        }

        ArrayDeque<Pair<Integer,Integer>> dQ = new ArrayDeque<>() ;

        dQ.addLast(new Pair(r,c));
        vis[r][c] = 1 ;

        int[] parent = new int[MainActivity.Rows*MainActivity.Cols] ;

        Arrays.fill(parent,-1);

        while(dQ.isEmpty()==false && check==0 ){

            int sz = dQ.size();

            while(sz-->0) {

                Pair<Integer,Integer> P = dQ.pollFirst();

                r = P.first;
                c = P.second;

                int pos = r*MainActivity.Cols+c;

                if(pos == Target) {
                    check=1;
                    break;
                }

                updateVal(mData,val,pos);

                for(int di=0;di<dir.length;di+=2){

                    int rnew = r+dir[di] ;
                    int cnew = c+dir[di+1] ;

                    if( rnew < 0 || cnew < 0 || rnew>=MainActivity.Rows || cnew>=MainActivity.Cols || vis[rnew][cnew]==1  )
                        continue;

                    int newPos = rnew*MainActivity.Cols+cnew ;

                    if( mData[newPos] == Utilities.CellStates.WallCell )
                        continue;

                    vis[rnew][cnew] = 1 ;

                    parent[newPos] = pos ;

                    dQ.addLast(new Pair(rnew,cnew));
                }

            }

            updateChanges.doProgress();

            waitSometime(MainActivity.WaitingTimeSearch);


        }

        ArrayList<Integer> tempAL = new ArrayList<>() ;

        if(check==1){

            while( Target != -1 ){
                tempAL.add(Target);
                Target=parent[Target];
            }

        }

        Collections.reverse(tempAL);

        pathAL.addAll(tempAL);

        return;
    }

    private static boolean is90(int pos3,int pos2,int pos1){

        if( pos1 == -1 )
            return false ;

        int r1 = pos1/MainActivity.Cols,c1= pos1%MainActivity.Cols ;
        int r2 = pos2/MainActivity.Cols,c2= pos2%MainActivity.Cols  ;
        int r3 = pos3/MainActivity.Cols,c3= pos3%MainActivity.Cols ;

        if( r1 == r2 && r2 != r3 )
            return true;

        if( c1 == c2 && c2 != c3 )
            return true;

        return false;
    }

    private static void updateVal(int[]mData,int val,int pos){

        if(pos!=MainActivity.StartPos){

            if( mData[pos] == Utilities.CellStates.WeightCell )
                mData[pos] = Utilities.CellStates.visWeightCell;

            else if( mData[pos] == Utilities.CellStates.destCell )
                mData[pos] = Utilities.CellStates.visDestCell;

            else if( mData[pos] == Utilities.CellStates.BombCell ) {
                mData[pos] = Utilities.CellStates.visBombCell;
            }
            else
                mData[pos] = val;
        }

    }

    static class customComparator implements Comparator<Pair<Integer, Pair<Integer,Integer>  >> {

        // Overriding compare()method of Comparator

        public int compare(Pair<Integer, Pair<Integer,Integer>  > s1, Pair<Integer, Pair<Integer,Integer>  > s2) {
            //some code
            int ret = s1.first.compareTo(s2.first) ;
            if(ret==1)
                return 1;
            if(ret==-1)
                return -1;
            return 0;
        }
    }
}
