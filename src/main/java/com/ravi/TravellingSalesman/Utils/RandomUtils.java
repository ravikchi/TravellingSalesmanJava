package com.ravi.TravellingSalesman.Utils;

/**
 * Created by ravik on 11/02/2017.
 */
public class RandomUtils {
    public static double randomDouble(){
        return Math.random();
    }

    public static int randomInd(){
        return new Double(Math.random() * 100).intValue();
    }

    public static int randomIntWithRange(int min, int max){
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
}
