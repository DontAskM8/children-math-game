package com.example.mathgame;

public class Game {
    private int levelPassed = 0;
    private boolean isEnded = true;

    public void increaseLevel(){
        levelPassed++;
    }

    public boolean getIsEnded(){
        return isEnded;
    }
    public void startGame(){
        isEnded = false;
    }

    public void stopGame(){
        isEnded = true;
    }
}
