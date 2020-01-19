package com.drm.mimicapp_v20;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        animationBegin();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent().setClass(
                        SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 2100);
    }

    void animationBegin() {
        ImageView ivIcon = findViewById(R.id.ivIcon);

        //ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(ivTitle, View.ALPHA, 1.0f, 0.2f, 1.0f);
        //animatorAlpha.setDuration(3000);
        ObjectAnimator animatorRotation = ObjectAnimator.ofFloat(ivIcon, "rotation", 0f, 360f);
        animatorRotation.setDuration(2000);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorRotation);
        animatorSet.start();
    }
}
