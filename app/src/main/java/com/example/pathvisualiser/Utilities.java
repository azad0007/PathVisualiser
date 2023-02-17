package com.example.pathvisualiser;

public class Utilities {

    // DIFF VALUES OF CELL STATES

    public class CellStates {
        public static final int unVisitedCell = 0;
        public static final int startCell = 1;
        public static final int destCell = -1;
        public static final int visCellinBombSearch = 2;
        public static final int visCellinDesSearch = 3;
        public static final int finalPath = 4;
        public static final int WeightCell = 5;
        public static final int WallCell = 6;
        public static final int BombCell = 7 ;
        public static final int visWeightCell = 8 ;
        public static final int visDestCell = 9 ;
        public static final int manWithBike = 10 ;
        public static final int visBombCell = 11 ;
    }

    // DIFF VALUES OF ALGORITHMS
    public class Algorithm {
        public static final int DFS = 0;
        public static final int BFS = 1;
        public static final int DJISKTRA = 2;
        public static final int ASEARCH = 3;
    }

    // DIFF VALUES OF ITEMS

    public class Items {

        public static final int WALL = 8;
        public static final int WEIGHT = 9;
    }


}
