package com.example.mathgame;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void changeViewBtn(View view) {
        setContentView(R.layout.comparegame);

        game = new CompareGame();

        //startgame
        game.startGame();

        compareGame((CompareGame) game);

    }

    public void gotoSortingPage(View view){
        setContentView(R.layout.sortinggame);

        game = new SortingGame();

        game.startGame();

        sortingGame((SortingGame) game);
    }

    public void gotoAddingPage(View view){
        setContentView(R.layout.addinggame);

        game = new AddingGame();

        game.startGame();

        addingGame((AddingGame) game);
    }

    public void homePageBtn(View view) {
        setContentView(R.layout.activity_main);
    }

    private void restartGameWithDelay(CompareGame game, int delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> compareGame(game), delayMillis);
    }
    public void compareGame(CompareGame game){
        Button btn1 = findViewById(R.id.button2);
        Button btn2 = findViewById(R.id.button3);
        View block = findViewById(R.id.blockContent);

        if (block != null) {
            block.setVisibility(View.GONE);
        }

        game.generateLevel();

        btn1.setText(String.valueOf(game.getNum1()));
        btn2.setText(String.valueOf(game.getNum2()));

        // Reset button colors to default (Gray)
        btn1.setBackgroundColor(Color.parseColor("#FF2196F3"));
        btn2.setBackgroundColor(Color.parseColor("#FF2196F3"));

        TextView isBiggerText = findViewById(R.id.isBiggerText);
        isBiggerText.setText(game.getIsBigger() ? "Bigger" : "Smaller");

        View.OnClickListener buttonClickListener = v -> {
            //Once clicked, don't allow anymore press by blocking the whole view
            if (block != null) {
                block.setVisibility(View.VISIBLE);
            }

            Button clickedButton = (Button) v;
            int selectedNumber = Integer.parseInt(clickedButton.getText().toString());

            if (game.isCorrect(selectedNumber)) {
                clickedButton.setBackgroundColor(Color.GREEN);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    compareGame(game); // New level
                    game.increaseLevel();
                }, 1000); // Restart after 1 sec
            } else {
                clickedButton.setBackgroundColor(Color.RED);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    setContentView(R.layout.failed);

                    Button restartButton = findViewById(R.id.button5);
                    restartButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setContentView(R.layout.comparegame);
                            compareGame(game); // Call the restart method
                        }
                    });
                    game.stopGame();
                }, 500);
            }
        };

        btn1.setOnClickListener(buttonClickListener);
        btn2.setOnClickListener(buttonClickListener);


    }

    public void checkSorting(){
        SortingGame currentGame = (SortingGame) game;
        LinearLayout numContainer = findViewById(R.id.sortedContainer);

        boolean[] results = currentGame.isCorrect();
        for(int j = 0; j < results.length; j++){
            numContainer.getChildAt(j).setBackgroundColor(results[j] ? Color.GREEN : Color.RED);
        }

        if(currentGame.isComplete()){
            //Prolly add some success or overlay to prevent further clicking
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                sortingGame(currentGame); // New level
                currentGame.increaseLevel();
            }, 1000); // Restart after 1 sec
        }
    }
    public void removeItem(View view){
        ((SortingGame) game).removeNumber();
        LinearLayout numContainer = findViewById(R.id.sortedContainer);

        int childCount = numContainer.getChildCount();
        Log.d("count", Integer.toString(childCount));
        if(childCount == 0) return; //Nothing to remove

        int removedValue = parseInt((String) ((TextView) numContainer.getChildAt(childCount - 1)).getText());

        numContainer.removeViewAt(childCount - 1);// Remove the last item


        TextView[] options = {
                findViewById(R.id.textView10),
                findViewById(R.id.textView11),
                findViewById(R.id.textView12),
                findViewById(R.id.textView13)
        };

        for(int i = 0; i < 4; i++){
            if(parseInt((String) options[i].getText()) == removedValue){
                options[i].setVisibility(View.VISIBLE); //restore back the removed value
                checkSorting();
                return; //Exit
            }
        }
    }

    public TextView generateOption(String text){
        TextView tv = new TextView(this);

        tv.setText(text);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(30);
        tv.setGravity(Gravity.CENTER); // Center text inside the TextView
        tv.setBackground(ContextCompat.getDrawable(this, R.drawable.rectangle_border));

        // Set LayoutParams to make it fill the container
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,  // Width = 0 so weight distributes space
                LinearLayout.LayoutParams.MATCH_PARENT,  // Height = full parent height
                1   // Weight = 1 (distribute width evenly)
        );
        params.setMargins(10, 10, 10, 10); // Optional: Add some spacing
        tv.setLayoutParams(params);
        return tv;
    }

    public void sortingGame(SortingGame game){
        game.generateLevel();

        TextView arrangement = findViewById(R.id.addingFinalAnswer);
        arrangement.setText(game.getIsAscending() ? "Ascending" : "Descending"); // If isbigger is true, we go by ascending

        LinearLayout numContainer = findViewById(R.id.sortedContainer);

        TextView[] options = {
                findViewById(R.id.textView10),
                findViewById(R.id.textView11),
                findViewById(R.id.textView12),
                findViewById(R.id.textView13)
        };

        int[] values = game.getValues();

        for(int i = 0; i < 4; i++){
            options[i].setText(Integer.toString(values[i]));
            options[i].setVisibility(View.VISIBLE);
            numContainer.removeAllViews(); // remove all previous options

            final int index = i;

//            if(options[i].hasOnClickListeners()) continue; //prevent adding multiple listeners

            options[i].setOnClickListener((v) -> {
                if(((LinearLayout) findViewById(R.id.sortedContainer)).getChildCount() >= 4) return; //Dont add if alrd have 4
                game.addNumber(values[index]);
                numContainer.addView(generateOption(Integer.toString(values[index])));
                v.setVisibility(View.GONE);

                //Check result everytime they click
                checkSorting();

            });
        }
    }

    public void addingGame(AddingGame game){
        game.generateLevel();

        int[] values = game.getValues();

        TextView answer = findViewById(R.id.addingFinalAnswer);

        answer.setText(Integer.toString(game.getAnswer()));

        TextView[] options = {
                findViewById(R.id.textView10),
                findViewById(R.id.textView11),
                findViewById(R.id.textView12),
                findViewById(R.id.textView13)
        };

        for(int i = 0; i < 4; i++){
            final int index = i;
            options[i].setText(Integer.toString(values[i]));

            options[i].setOnClickListener(v -> {
                ColorDrawable viewColor = (ColorDrawable) v.getBackground();
                int colorId = viewColor != null ? viewColor.getColor() : 0;

                if(colorId != Color.TRANSPARENT){
                    v.setBackgroundColor(Color.TRANSPARENT);
                    game.removeAnswer(values[index]);

                    if(game.getSelectedCount() != 0){
                        //If there is still selected numbers, especially single nuumber, we change red back to green
                        for(int j = 0; j < 4; j++){
                            ColorDrawable textBgColor = (ColorDrawable) options[j].getBackground();
                            int textColorId = textBgColor != null ? textBgColor.getColor() : 0;

                            if(textColorId == Color.RED){
                                options[j].setBackgroundColor(Color.GREEN);
                            }
                        }
                    }
                }else{
                    if(game.getSelectedCount() == 2) return; //Dont check for answer if didnt click 2

                    v.setBackgroundColor(Color.GREEN);
                    game.addAnswer(values[index]);

                    if(game.checkAnswer()){
                        //go to next level
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            for(int j = 0; j < 4; j++){ //Reset level
                                options[j].setBackgroundColor(Color.TRANSPARENT);
                            }

                            addingGame(game); // New level
                            game.increaseLevel();

                        }, 1000);
                    }else{
                        if(game.getSelectedCount() == 2) v.setBackgroundColor(Color.RED);
                    }
                }
            });
        }



    }
}