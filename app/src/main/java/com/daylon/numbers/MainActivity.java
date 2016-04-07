package com.daylon.numbers;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView lhs;
    TextView rhs;
    TextView tLevel;
    ImageButton xButton;
    ImageButton checkButton;
    ProgressBar pb;
    CountDownTimer timer;
    int level;
    boolean ans = true;
    Random random;
    boolean gameOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //Buttons!
        xButton = (ImageButton) findViewById(R.id.xbutton);
        checkButton = (ImageButton) findViewById(R.id.checkbutton);

        //progress bar! === COME BACK
        pb = (ProgressBar) findViewById(R.id.singleplayerPB);

        //Button Listeners!


        xButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent e){
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(gameOn) {
                            xButton.setImageResource(R.drawable.pressedxmark);
                            testCheck(false);
                            return true;
                        }
                    case MotionEvent.ACTION_UP:
                        xButton.setImageResource(R.drawable.xmark);
                        return true;
                }
                return false;
            }
        });


        checkButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(gameOn) {
                            checkButton.setImageResource(R.drawable.pressedcheck);
                            testCheck(true);
                            return true;
                        }
                    case MotionEvent.ACTION_UP:
                        checkButton.setImageResource(R.drawable.check);
                        return true;

                }
                return false;
            }
        });


        //TextViews
        lhs = (TextView) findViewById(R.id.lhs);
        rhs = (TextView) findViewById(R.id.rhs);
        tLevel = (TextView) findViewById(R.id.level);

        level = 1;
        random = new Random();


        startGame();
        Typeface font = Typeface.createFromAsset(getAssets(), "Grundschrift-Bold.otf");
        lhs.setTypeface(font);
        lhs.setTextSize(30);
        rhs.setTypeface(font);
        rhs.setTextSize(30);
        tLevel.setTypeface(font);


    }
    public void testCheck(boolean pAns){
        timer.cancel();
        if(pAns == ans){
            ans = setExpressions();
            level++;
            newLevel();
        }
        else{
            System.out.println(pAns);
            System.out.println(ans);
            System.out.println("Game over");
            gameOn = false;
        }
    }
    public void startGame(){
        ans = setExpressions();
        newLevel();
    }

    public void newLevel(){
        tLevel.setText("Level: " + level);
        pb.setMax(2000);
        pb.setProgress(2000);
        ans = setExpressions();
        timer = new CountDownTimer(2000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                pb.setProgress((int)millisUntilFinished);

            }
            @Override
            public void onFinish() {
                gameOn = false;
                pb.setProgress(0);
                System.out.println("time up!");
            }
        }.start();
    }

    public boolean setExpressions(){
        int maxNum = level + 9;
        int ans = random.nextInt(maxNum);
        int left;
        int right;
        boolean same;

        same = random.nextInt() % 2 == 0 ? true : false;

        //lhs
        left = random.nextInt(ans+1);
        right = ans - left;
        lhs.setText(left + " + " + right);

        //rhs
        if(same)
            rhs.setText("" + ans);
        else{
            int r = random.nextInt(maxNum);
            while(r==ans){
                r = random.nextInt(maxNum);
            }
            rhs.setText("" +  r);
        }
        return same;

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
