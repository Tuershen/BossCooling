package pers.tuershen.bosscooling.calculation.util;

@Deprecated
public class BlockPos {

    private static int[][] posX;

    private static int[][] posZ;

    private static int[] posY;

    static {

        posY    = new int[]  {  0, 1, 2 };


        posX[0] = new int[]  { +1, +1, +1 };
        posX[1] = new int[]  {  0,  0,  0 };
        posX[3] = new int[]  { -1, -1, -1 };


        posZ[0] = new int[]  { +1, +1, +1 };
        posZ[1] = new int[]  {  0,  0,  0 };
        posZ[3] = new int[]  { -1, -1, -1 };
    }




}
