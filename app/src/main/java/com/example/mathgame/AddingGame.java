package com.example.mathgame;

import android.util.Log;

import java.util.Arrays;
import java.util.Random;

public class AddingGame extends Game {
    private int num1;
    private int num2;
    private int num3;
    private int num4;
    private int finalAnswer;
    private int numCount = 0;
    private int[] selectedValues = new int[2];


    Random randomSeed = new Random();


    public int getSelectedCount() {
        return numCount;
    }
    private int generateNumber(){
        return randomSeed.nextInt(999) + 1; // Generates number from 1 to 999
    }
    private int generateNumber(int num){
        return randomSeed.nextInt(num) + 1; // Generates number from 1 to 999
    }

    public int[] getValues() {
        return new int[] {
                num1, num2, num3, num4
        };
    };

    public int getAnswer(){
        return finalAnswer;
    }


    private void shuffleArray(int[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = randomSeed.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public void generateLevel(){
        numCount = 0; //Reset back to 0
        selectedValues = new int[2]; //Reset selection
        finalAnswer = generateNumber();

        num1 = generateNumber(finalAnswer); //Get the 2 initial solutions first
        num2 = generateNumber(finalAnswer);

        int[] answerPool = {
                num1,
                num2,
                finalAnswer - num1,
                finalAnswer - num2
        }; // Get the list of possible answers

        shuffleArray(answerPool); //Shuuffle them

        num1 = answerPool[0]; //Reassign positions
        num2 = answerPool[1];
        num3 = answerPool[2];
        num4 = answerPool[3];
    }

    public void addAnswer(int num){
        if(numCount < 2) selectedValues[numCount++] = num;
    }

    public void removeAnswer(int num){
        Log.d("removing", Integer.toString(num));

        if(numCount <= 0) return;

        if(selectedValues[0] == num){
            selectedValues[0] = selectedValues[1];
        }//If they remove the first number, move the last number to first

        numCount--;
    }

    public boolean checkAnswer(){
        int sum = 0;

        for(int i = 0; i < 2; i++){
            Log.d("check values", Integer.toString(selectedValues[i]));
            if(selectedValues[i] == -1) continue; //Dont add if its -1
            sum += selectedValues[i];
        }

        return sum == finalAnswer;
    }



}

