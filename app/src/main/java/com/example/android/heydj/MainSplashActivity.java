package com.example.android.heydj;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainSplashActivity extends AppCompatActivity {

    private int SPLASH_TIME_OUT = 5000;

//    private TextView heyDj;
    private ImageView spinninRecords;
    private Animation myFadeInAnimation;
    private Animation mySpinAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        heyDj = (TextView) findViewById(R.id.hey_dj_intro);
        spinninRecords = (ImageView) findViewById(R.id.image_album_splash);

        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
//        heyDj.startAnimation(myFadeInAnimation);
        spinninRecords.startAnimation(myFadeInAnimation);



        spinninRecords.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent homeIntent = new Intent(getApplicationContext(), LandingActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
