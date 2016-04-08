package com.daylon.numbers;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class TitlePage extends AppCompatActivity {
    ImageView title;
    CountDownTimer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_title_page);
        title = (ImageView) findViewById(R.id.title);
        animateTitle();

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
