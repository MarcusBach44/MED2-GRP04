package com.example.med2_grp04;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


public class OverlayProcessor {
    public void InkyIdleToWalking(long AmountOfTime){
        long Milisecounds = AmountOfTime * 1000;
        final long[] ExtraTime = new long[1];
        OverlayManager.InkyIdleAnimation();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("", "DurractionIdle Is Calculated"+OverlayManager.inkyOverlayIdleGif.getDuration());
                long frames = 8 - OverlayManager.inkyOverlayIdleGif.getCurrentFrameIndex() + 2;
                ExtraTime[0] = 300 * frames;

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OverlayManager.inkyOverlayIdleGif.stop();
                        OverlayManager.InkyWalkingAnimation();
                    }
                }, ExtraTime[0]);
            }
        }, Milisecounds);
    }
}

