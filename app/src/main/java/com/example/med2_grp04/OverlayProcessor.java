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

    /*
    public void InkyIntroToIdle(long AmountOfTime) {
        long Milisecounds = AmountOfTime * 1000;
        final long[] ExtraTime = new long[1];
        OverlayManager.InkyIntroAnimation();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("", "DurractionIdle Is Calculated" + OverlayManager.inkyOverlayIdleGif.getDuration());
                long frames = 24 - OverlayManager.inkyOverlayIdleGif.getCurrentFrameIndex() + 2;
                ExtraTime[0] = 300 * frames;

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OverlayManager.InkyIdleAnimation();
                    }
                }, ExtraTime[0]);
            }
        }, Milisecounds);
    }


     */
    /*
    private void InkyWalkingForce() {
        Handler handler = new Handler(Looper.getMainLooper());

        ImageView imageView = (ImageView) OverlayManager.inkyOverlay.get().mView.findViewById(R.id.inky_overlay);
        Runnable physicsLoop = new Runnable() {
            @Override
            public void run() {
                Log.d("", "FrameWalking Is Calculated" + OverlayManager.inkyOverlayWalkingGif.getCurrentFrameIndex());
                if(OverlayManager.inkyOverlayWalkingGif.getCurrentFrameIndex() > 4 || OverlayManager.inkyOverlayWalkingGif.getCurrentFrameIndex() < 7){
                    Log.d("", "Im inside Wahoo");
                    InkyVelocityY = -40f;

                }
                if(OverlayManager.inkyOverlayWalkingGif.getCurrentFrameIndex() == 7){
                    InkyVelocityY = 0;

                }
                Log.d("", "Velocity Is Calculated" + InkyVelocityY);
                InkyVelocityY += gravity;
                InkyVelocityY = Math.min(InkyVelocityY,0);
                InkyVelocityX = Math.min(InkyVelocityX,0);
                Log.d("", "Volocity now minimized Is Calculated" + InkyVelocityY);

                imageView.setX(imageView.getX() + InkyVelocityX);
                imageView.setY(imageView.getY() + InkyVelocityY);

                handler.postDelayed(this, 16);

            }
        };

        physicsLoop.run();
    }
    */

}

