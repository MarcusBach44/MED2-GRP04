package com.example.med2_grp04;
import android.health.connect.datatypes.units.Velocity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

public class OverlayProcessor {
    private float InkyVelocityX = 0;
    private float InkyVelocityY = 0;
    private float gravity = 3f;


    public void InkyIntroToIdle(long AmountOfTime) {
        long Milisecounds = AmountOfTime * 1000;
        final long[] ExtraTime = new long[1];
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("", "DurractionIdle Is Calculated" + OverlayManager.inkyOverlayIntroGif.getDuration());
                long frames = 24 - OverlayManager.inkyOverlayIntroGif.getCurrentFrameIndex() + 2;
                ExtraTime[0] = 300 * frames;

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OverlayManager.InkyIdleAnimation();
                        InkyIdleToFrustrated(480);
                    }
                }, ExtraTime[0]);
            }
        }, Milisecounds);
    }


    public void InkyIdleToFrustrated(long AmountOfTime) {
        long Milisecounds = AmountOfTime * 1000;
        final long[] ExtraTime = new long[1];
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("", "DurractionIdle Is Calculated" + OverlayManager.inkyOverlayIdleGif.getDuration());
                long frames = 8 - OverlayManager.inkyOverlayIdleGif.getCurrentFrameIndex() + 2;
                ExtraTime[0] = 300 * frames;

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OverlayManager.InkyFrustatedAnimation();
                    }
                }, ExtraTime[0]);
            }
        }, Milisecounds);
    }

}

