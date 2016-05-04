package com.daylon.numbers;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

import pl.droidsonroids.gif.GifTextView;

public class MultiplayerGame extends AppCompatActivity {

    final int MAX_SCORE = 30;
    String gameRoom;
    GifTextView readyGo;
    long players;
    TextView lhs;
    TextView rhs;
    TextView tLevel;
    GifTextView waitingForOpponent;
    Firebase myFirebaseRef;
    ImageButton xButton;
    ImageButton checkButton;
    ImageView topPlayer;
    ImageView botPlayer;
    TextView equals;
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
        Firebase disconnectRef = new Firebase("https://daylonnumbers.firebaseio.com").child(gameRoom).child("Players");
        disconnectRef.onDisconnect().setValue(players);
        pb_1 = (ProgressBar) findViewById(R.id.pb_1);
        pb_2 = (ProgressBar) findViewById(R.id.pb_2);
        pb_1.setMax(MAX_SCORE);
        pb_2.setMax(MAX_SCORE);

        topPlayer = (ImageView) findViewById(R.id.topPlayer);
        botPlayer = (ImageView) findViewById(R.id.botPlayer);

        //readyGo View
        readyGo = (GifTextView) findViewById(R.id.readygo);

        myFirebaseRef.child("p1Score").setValue(0);
        myFirebaseRef.child("p2Score").setValue(0);
        myFirebaseRef.child("Players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                players = (long) dataSnapshot.getValue();
                if(!idSet) {
                    myID = (long) dataSnapshot.getValue();
                    idSet = true;
                    myFirebaseRef.child("Players").setValue(myID+1);
                }
                if(players==2){
                    readyAnimation();
                }
                if(players==1)
                    readyGo.setBackgroundResource(R.drawable.waiting_for_opponent);
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
                    endGame();
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
                if (scores[1] == MAX_SCORE) {
                    endGame();
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
                            xButton.setImageResource(R.drawable.stillx);
                            testCheck(false);
                            return true;
                        }
                    case MotionEvent.ACTION_UP:
                        xButton.setImageResource(R.drawable.stillx);
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
                            checkButton.setImageResource(R.drawable.stillcheck);
                            testCheck(true);
                            return true;
                        }
                    case MotionEvent.ACTION_UP:
                        checkButton.setImageResource(R.drawable.stillcheck);
                        return true;

                }
                return false;
            }
        });


        //TextViews
        lhs = (TextView) findViewById(R.id.lhs);
        rhs = (TextView) findViewById(R.id.rhs);
        tLevel = (TextView) findViewById(R.id.level);
        equals = (TextView) findViewById(R.id.equalsign);


        level = 1;
        random = new Random();

        Typeface font = Typeface.createFromAsset(getAssets(), "Grundschrift-Bold.otf");
        lhs.setTypeface(font);
        lhs.setTextSize(30);
        rhs.setTypeface(font);
        rhs.setTextSize(30);
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
            gameOn = false;
            equals.setVisibility(View.INVISIBLE);
            lhs.setText("Wrong!");
            timer = new CountDownTimer(2000,10) {
                @Override
                public void onTick(long millisUntilFinished) {
                    rhs.setText("Penalty: " + millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    gameOn = true;
                    equals.setVisibility(View.VISIBLE);
                    newLevel();
                }
            }.start();
        }
    }

    public void readyAnimation(){
        readyGo.setVisibility(View.VISIBLE);
        readyGo.setBackgroundResource(R.drawable.readygo);
        topPlayer.setImageResource(myID == 0 ? R.drawable.you : R.drawable.your_opponent);
        botPlayer.setImageResource(myID == 1 ? R.drawable.you : R.drawable.your_opponent);
        topPlayer.setVisibility(View.VISIBLE);
        botPlayer.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(4500,10) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                readyGo.setVisibility(View.INVISIBLE);
                startGame();
            }
        }.start();
    }
    public void startGame(){
        gameOn = true;
        level = 1;
        ans = setExpressions();
        equals.setVisibility(View.VISIBLE);
        equals.setText(" = ");
        rhs.setVisibility(View.VISIBLE);
        lhs.setVisibility(View.VISIBLE);
        xButton.setImageResource(R.drawable.stillx);
        checkButton.setImageResource(R.drawable.stillcheck);
        xButton.setVisibility(View.VISIBLE);
        checkButton.setVisibility(View.VISIBLE);
        newLevel();
    }
    public void endGame(){
        gameOn = false;
        readyGo.setBackgroundResource(R.drawable.game_over_animated);
        readyGo.setVisibility(View.VISIBLE);
        xButton.setVisibility(View.INVISIBLE);
        checkButton.setVisibility(View.INVISIBLE);
        rhs.setVisibility(View.INVISIBLE);
        lhs.setVisibility(View.INVISIBLE);
        equals.setVisibility(View.INVISIBLE);
        timer = new CountDownTimer(4500,10) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                readyGo.setVisibility(View.INVISIBLE);
                equals.setText(scores[(int)myID]==30 ? "You win!!!" : "You lose!!!");
                equals.setVisibility(View.VISIBLE);
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


    public void newLevel(){
        tLevel.setText("Level: " + level);
        ans = setExpressions();

    }

}
