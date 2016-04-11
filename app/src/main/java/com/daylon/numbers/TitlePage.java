package com.daylon.numbers;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class TitlePage extends AppCompatActivity {
    ImageView title;
    CountDownTimer timer;
    ImageButton singleplayer;
    ImageButton versus;
    ImageButton leaderboard;
    ImageButton rm1;
    ImageButton rm2;
    ImageButton rm3;
    RelativeLayout roomLayout;
    TextView rm1Occupancy;
    TextView rm2Occupancy;
    TextView rm3Occupancy;
    Firebase ref;
    boolean roomsVisible;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_title_page);
        title = (ImageView) findViewById(R.id.title);
        animateTitle();

        //buttons!
        singleplayer = (ImageButton) findViewById(R.id.singleplayer);
        versus = (ImageButton) findViewById(R.id.versus);
        leaderboard = (ImageButton) findViewById(R.id.leaderboard);
        roomLayout = (RelativeLayout) findViewById(R.id.roomLayout);
        rm1 = (ImageButton) findViewById(R.id.rm1);
        rm2 = (ImageButton) findViewById(R.id.rm2);
        rm3 = (ImageButton) findViewById(R.id.rm3);
        rm1Occupancy = (TextView) findViewById(R.id.rm1Occupancy);
        rm2Occupancy = (TextView) findViewById(R.id.rm2Occupancy);
        rm3Occupancy = (TextView) findViewById(R.id.rm3Occupancy);
        roomsVisible = false;
        ref = new Firebase("https://daylonnumbers.firebaseio.com");
        ref.child("Room 1").child("Players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rm1Occupancy.setText(((long)dataSnapshot.getValue()+"/2").toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ref.child("Room 2").child("Players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rm2Occupancy.setText(((long)dataSnapshot.getValue()+"/2").toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ref.child("Room 3").child("Players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rm3Occupancy.setText(((long)dataSnapshot.getValue()+"/2").toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        singleplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TitlePage.this, MainActivity.class);
                startActivity(i);
            }
        });

        versus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!roomsVisible) {
                    roomLayout.setVisibility(View.VISIBLE);
                    roomsVisible = true;
                }
                else{
                    roomLayout.setVisibility(View.INVISIBLE);
                    roomsVisible = false;
                }
            }
        });

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TitlePage.this, leaderboard.class);
                startActivity(i);
            }
        });

        rm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TitlePage.this, MultiplayerGame.class);
                i.putExtra("room", 1);
                startActivity(i);
            }
        });

        rm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TitlePage.this, MultiplayerGame.class);
                i.putExtra("room", 2);
                startActivity(i);
            }
        });

        rm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TitlePage.this, MultiplayerGame.class);
                i.putExtra("room", 3);
                startActivity(i);
            }
        });

    }

    protected void onResume(){
        super.onResume();
        roomLayout.setVisibility(View.INVISIBLE);
    }
    public void animateTitle(){
        timer = new CountDownTimer(60000,500) {
            int i = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                if(i%2==0)
                    title.setImageResource(R.drawable.titleanimation1);
                else{
                    title.setImageResource(R.drawable.titleanimation2);
                }
                i++;
            }

            @Override
            public void onFinish() {
                animateTitle();
            }
        }.start();

    }



}
