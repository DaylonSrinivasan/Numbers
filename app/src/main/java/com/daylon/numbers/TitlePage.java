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

public class TitlePage extends AppCompatActivity {
    ImageView title;
    CountDownTimer timer;
    ImageButton singleplayer;
    ImageButton versus;
    ImageButton leaderboard;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_title_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (ImageView) findViewById(R.id.title);
        animateTitle();

        //buttons!
        singleplayer = (ImageButton) findViewById(R.id.singleplayer);
        versus = (ImageButton) findViewById(R.id.versus);
        leaderboard = (ImageButton) findViewById(R.id.leaderboard);

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
                Intent i = new Intent(TitlePage.this, MultiplayerGame.class);
                startActivity(i);
            }
        });

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
