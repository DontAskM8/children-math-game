package com.example.mathgame;

import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class SortingGame extends Game {
    private int num1;
    private int num2;
    private int num3;
    private int num4;
    private boolean isAscending;
    private int [] numberlist = new int[4];
    private int numberCount = 0;

    public void addNumber(int num){
        if(numberCount >= 4) return; //Cannot add more than 4
        numberlist[numberCount++] = num;
    }

    public void removeNumber(){
        if(numberCount <= 0) return; //There nothing to remove
        numberlist[--numberCount] = isAscending ? 9999 : 0; //number is 1-999, when removing a number need to ensure it doesnt mess up the array when sorting
    }

    public int[] getValues(){
        return new int[] { num1, num2, num3, num4 };
    }


    Random randomSeed = new Random();

    public boolean getIsAscending(){
        return isAscending;
    }

    private int generateNumber(){
        return randomSeed.nextInt(999) + 1; // Generates number from 1 to 999
    }

    private void setIsAscending(){
        isAscending = (randomSeed.nextInt(99) + 1) > 50; //If more than 50 we set the game to find the bigger number
    }

    public void generateLevel(){

        setIsAscending(); // Generate this level is choose bigger or smaller
        num1 = generateNumber();
        num2 = generateNumber();
        num3 = generateNumber();
        num4 = generateNumber();



        numberlist = isAscending ? new int[] {9999, 9999, 9999, 9999} : new int[]  {0,0,0,0}; //Reset this everytime we generate new level
        numberCount = 0;
    }

    public boolean[] isCorrect(){
        boolean[] arrangement = new boolean[numberCount];

        if(numberCount == 0) return arrangement; // Theres nothing to check

        int[] sortedNumberList = Arrays.copyOf(numberlist, numberlist.length);
        Arrays.sort(sortedNumberList);
        if(!isAscending){ //Reverse the array to be descending nuumber
            for (int i = 0, j = sortedNumberList.length - 1; i < j; i++, j--) {
                int temp = sortedNumberList[i];
                sortedNumberList[i] = sortedNumberList[j];
                sortedNumberList[j] = temp;
            }
        }
        Log.d("current array", Arrays.toString(sortedNumberList));
        for(int i = 0; i < numberCount; i++){
            if(numberlist[i] != sortedNumberList[i]) arrangement[i] = false;
            else arrangement[i] = true;
        }

        return arrangement;
    }

    public boolean isComplete(){
        if(numberCount != 4) return false;
        boolean[] results = isCorrect();

        for(int i = 0; i < numberCount; i++){
            if(!results[i]) return false; // If 1 of them is wrong, can straight away skip
        }
        return true;
    }
}

