package com.example.mathgame;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CompareGame extends Game{
    private int num1;
    private int num2;
    private boolean isBigger;


    Random randomSeed = new Random();

    public void setNum1(int num){
        num1 = num;
    }

    public int getNum1(){
        return num1;
    }


    public void setNum2(int num){
        num2 = num;
    }

    public int getNum2(){
        return num2;
    }

    public boolean getIsBigger(){
        return isBigger;
    }

    private int generateNumber(){
        return randomSeed.nextInt(999) + 1; // Generates number from 1 to 999
    }

    private void setIsBigger(){
        isBigger = (randomSeed.nextInt(99) + 1) > 50; //If more than 50 we set the game to find the bigger number
    }

    public void generateLevel(){
        num1 = generateNumber();
        do {
            num2 = generateNumber();
        } while (num1 == num2);

        setIsBigger(); // Generate this level is choose bigger or smaller
    }

    public boolean isCorrect(int num){
        if(isBigger){
            return Math.max(num1, num2) == num;
        }else{
            return Math.min(num1, num2) == num;
        }
    }
}

