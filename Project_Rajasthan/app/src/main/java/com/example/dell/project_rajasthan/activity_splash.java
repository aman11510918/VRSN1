package com.example.dell.project_rajasthan;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

public class activity_splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler h1 = new Handler();
        h1.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(activity_splash.this, activity_home.class));
                finish();
            }
        }, 2500);
    }
}