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
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

public class MultiplayerGame extends AppCompatActivity {

    final int MAX_SCORE = 30;
    String gameRoom;
    long players;
    TextView lhs;
    TextView rhs;
    TextView tLevel;
    Firebase myFirebaseRef;
    ImageButton xButton;
    ImageButton checkButton;
    ProgressBar pb_1;
    ProgressBar pb_2;
    ProgressBar [] pbs = {pb_1, pb_2};
    long [] scores = {0, 0};
    boolean idSet = false;
    long myID;

    int level;
    CountDownTimer timer;

    boolean ans = true;
    Random random;
    boolean gameOn = false;

    /*@Override
    protected void onDestroy(){ //BAD WAY TO IMPLEMENT REMOVAL OF PLAYERS
        super.onDestroy();
        myFirebaseRef.child("Players").setValue(players-1);
    }*/

    @Override
    protected void onStop(){
        super.onStop();
        myFirebaseRef.child("Players").setValue(players - 1);

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        myFirebaseRef.child("Players").setValue(players+1);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_game);

        gameRoom = "Room " + (int)getIntent().getExtras().get("room");

        myFirebaseRef = new Firebase("https://daylonnumbers.firebaseio.com").child(gameRoom);

        pb_1 = (ProgressBar) findViewById(R.id.pb_1);
        pb_2 = (ProgressBar) findViewById(R.id.pb_2);
        pb_1.setMax(MAX_SCORE);
        pb_2.setMax(MAX_SCORE);

        System.out.println(gameRoom);
        myFirebaseRef.child("p1Score").setValue(0);
        myFirebaseRef.child("p2Score").setValue(0);
        myFirebaseRef.child("Players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                players = (long) dataSnapshot.getValue();
                if(!idSet) {
                    myID = (long) dataSnapshot.getValue();
                    Toast.makeText(MultiplayerGame.this, "Set playerID to " + myID, Toast.LENGTH_SHORT);
                    idSet = true;
                    myFirebaseRef.child("Players").setValue(myID+1);
                }
                if(players==2){
                    gameOn = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        myFirebaseRef.child("p1Score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scores[0] = (long) dataSnapshot.getValue();
                pb_1.setProgress((int) scores[0]);
                if(scores[0] == MAX_SCORE){
                    Toast.makeText(MultiplayerGame.this, myID == 0 ? "You WIN!" : "You Lose!",
                            Toast.LENGTH_SHORT);
                    gameOn = false;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        myFirebaseRef.child("p2Score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scores[1] = (long) dataSnapshot.getValue();
                pb_2.setProgress((int) scores[1]);
                if(scores[1] == MAX_SCORE){
                    Toast.makeText(MultiplayerGame.this, myID == 0 ? "You WIN!" : "You Lose!",
                            Toast.LENGTH_SHORT);
                    gameOn = false;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        xButton = (ImageButton) findViewById(R.id.xbutton);
        checkButton = (ImageButton) findViewById(R.id.checkbutton);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores[myID == 1 ? 0 : 1]++;
                myFirebaseRef.child(myID == 1 ? "p1Score" : "p2Score").setValue(myID == 1 ? scores[0] : scores[1]);
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores[myID == 1 ? 0 : 1]++;
                myFirebaseRef.child(myID == 1 ? "p1Score" : "p2Score").setValue(myID == 1 ? scores[0] : scores[1]);
            }
        });

        xButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (gameOn) {
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
                        if (gameOn) {
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
        rhs.setTypeface(font);
        tLevel.setTypeface(font);
    }

    public void testCheck(boolean pAns){
        //timer.cancel();
        if(pAns == ans){
            ans = setExpressions();
            level++;
            scores[myID == 0 ? 0 : 1]++;
            myFirebaseRef.child(myID == 0 ? "p1Score" : "p2Score").setValue(myID == 0 ? scores[0] : scores[1]);
            newLevel();
        }
        else{
            timer = new CountDownTimer(2000,100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    gameOn = false;
                }

                @Override
                public void onFinish() {
                    gameOn = true;
                }
            }.start();
        }
    }

    public void startGame(){
        ans = setExpressions();
        newLevel();
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


    public void newLevel(){
        tLevel.setText("Level: " + level);
        //pb.setMax(2000);
        //pb.setProgress(2000);
        ans = setExpressions();
        /*timer = new CountDownTimer(2000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                //pb.setProgress((int)millisUntilFinished);

            }
            @Override
            public void onFinish() {
                gameOn = false;
                //pb.setProgress(0);
                System.out.println("time up!");
            }
        }.start();*/
    }

}
