package com.example.med2_grp04;
import android.os.Handler;
import android.os.Looper;


public class OverlayProcessor {
    public void InkyIdleToWalking(long AmountOfTime){
        long Milisecounds = AmountOfTime * 1000;
        OverlayManager.InkyIdleAnimation();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                OverlayManager.InkyWalkingAnimation();
            }
        }, Milisecounds);
    }
}

