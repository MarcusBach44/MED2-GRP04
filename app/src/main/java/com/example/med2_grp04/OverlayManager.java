package com.example.med2_grp04;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import pl.droidsonroids.gif.GifDrawable;
import androidx.appcompat.app.AppCompatActivity;


public class OverlayManager extends AppCompatActivity {
    public static GifDrawable inkOverlayGif;
    public static GifDrawable inkOverlayReverseGif;
    public static GifDrawable inkyOverlayIdleGif;
    public static GifDrawable inkyOverlayIntroGif;
    public static GifDrawable inkyOverlaySleepingGif;
    public static GifDrawable inkyOverlayFrustratedGif;

    public static float inkSpreadSpeed = 0.05f;
    public static float inkRecoveryRate = 0.05f;
    public static float movementSpeed = 5f;
    private static WeakReference<InkOverlayWindow> inkOverlay;
    public static WeakReference<InkyOverlayWindow> inkyOverlay;

    public static void updateInkOverlayWindow(InkOverlayWindow window){
        inkOverlay = new WeakReference<>(window);
    }
    public static void updateOverlayInkyWindow(InkyOverlayWindow window){
        System.out.println("Running updateOverlayInkyWindow in overlayManager");
        inkyOverlay = new WeakReference<>(window);
        System.out.println("Completing updateOverlayInkyWindow in overlayManager");
    }

    public static void OpenInkOverlay(){
        inkOverlayReverseGif.pause();
        ImageView im = (ImageView) inkOverlay.get().mView.findViewById(R.id.ink_overlay);
        im.setImageDrawable(inkOverlayGif);
        inkOverlayGif.seekToFrame((inkOverlayReverseGif.getCurrentFrameIndex()-48)*-1);
        inkOverlayGif.setSpeed(inkSpreadSpeed);
        inkOverlayGif.start();
        inkOverlay.get().Open();
    }
    public static void CloseInkOverlay(){
        inkOverlayGif.pause();
        ImageView im = (ImageView) inkOverlay.get().mView.findViewById(R.id.ink_overlay);
        im.setImageDrawable(inkOverlayReverseGif);
        inkOverlayReverseGif.seekToFrame((inkOverlayGif.getCurrentFrameIndex()-48)*-1);
        inkOverlayReverseGif.setSpeed(inkRecoveryRate);
        inkOverlayReverseGif.start();
        inkOverlay.get().Close();
    }

    public static void InkyIdleAnimation(){
        System.out.println("Running InkyIdleAnimation in overlayManager");
        ImageView im = (ImageView) inkyOverlay.get().mView.findViewById(R.id.inky_overlay);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) im.getLayoutParams();
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.width = 900;
        params.height = 900;
        params.rightMargin = -200;
        params.bottomMargin = -200;
        im.setLayoutParams(params);
        inkyOverlayIdleGif.stop();
        inkyOverlayIdleGif.reset();
        inkyOverlayIdleGif.setSpeed(movementSpeed);
        im.setImageDrawable(inkyOverlayIdleGif);
        inkyOverlayIdleGif.start();
        System.out.println("Complete InkyIdleAnimation in overlayManager");
    }

    public static void InkySleepingAnimation(){
        System.out.println("Running InkySleepingAnimation in overlayManager");
        ImageView im = (ImageView) inkyOverlay.get().mView.findViewById(R.id.inky_overlay);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) im.getLayoutParams();
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.width = 900;
        params.height = 900;
        params.rightMargin = -200;
        params.bottomMargin = -200;
        im.setLayoutParams(params);
        inkyOverlaySleepingGif.stop();
        inkyOverlaySleepingGif.reset();
        inkyOverlaySleepingGif.setSpeed(movementSpeed);
        im.setImageDrawable(inkyOverlaySleepingGif);
        inkyOverlaySleepingGif.start();
        System.out.println("Complete InkySleepingAnimation in overlayManager");
    }

    public static void InkyEnjoymentAnimation(){

    }

    public static void InkyFrustatedAnimation(){
        System.out.println("Running InkyAngryAnimation in overlayManager");
        ImageView im = (ImageView) inkyOverlay.get().mView.findViewById(R.id.inky_overlay);
        inkyOverlayFrustratedGif.stop();
        inkyOverlayFrustratedGif.reset();
        inkyOverlayFrustratedGif.setSpeed(movementSpeed);
        im.setImageDrawable(inkyOverlayFrustratedGif);
        inkyOverlayFrustratedGif.start();
        System.out.println("Complete InkyAngryAnimation in overlayManager");
    }

    public static void InkyIntroAnimation(){
        System.out.println("Running InkyWalkingAnimation in overlayManager");
        inkyOverlayIntroGif.setSpeed(movementSpeed);
        ImageView im = (ImageView) inkyOverlay.get().mView.findViewById(R.id.inky_overlay);
        im.setImageDrawable(inkyOverlayIntroGif);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) im.getLayoutParams();
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        params.bottomMargin = -300;
        params.rightMargin = 0;
        im.setLayoutParams(params);
        inkyOverlayIntroGif.reset();
        inkyOverlayIdleGif.start();
        inkyOverlay.get().Open();
        System.out.println("Complete InkyWalkingAnimation in overlayManager");

    }


    public static void InkyClose(){
        inkyOverlay.get().Close();
    }

}
