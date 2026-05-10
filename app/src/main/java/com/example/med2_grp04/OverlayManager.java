package com.example.med2_grp04;

import android.os.Handler;
import android.os.Looper;
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

    public static float spreadSpeed = 0.05f;
    public static float movementSpeed = 5.0f;
    private static WeakReference<Window> overlay;
    public static WeakReference<InkyOverlayWindow> inkyOverlay;

    public static void updateOverlayWindow(Window window){
        overlay = new WeakReference<>(window);
    }
    public static void updateOverlayInkyWindow(InkyOverlayWindow window){
        System.out.println("Running updateOverlayInkyWindow in overlayManager");
        inkyOverlay = new WeakReference<>(window);
        System.out.println("Completing updateOverlayInkyWindow in overlayManager");
    }

    public static void OpenOverlay(){
        inkOverlayReverseGif.pause();
        ImageView im = (ImageView) overlay.get().mView.findViewById(R.id.ink_overlay);
        im.setImageDrawable(inkOverlayGif);
        inkOverlayGif.seekToFrame((inkOverlayReverseGif.getCurrentFrameIndex()-48)*-1);
        inkOverlayGif.setSpeed(spreadSpeed);
        inkOverlayGif.start();
        overlay.get().Open();

    }
    public static void CloseOverlay(){
        inkOverlayGif.pause();
        ImageView im = (ImageView) overlay.get().mView.findViewById(R.id.ink_overlay);
        im.setImageDrawable(inkOverlayReverseGif);
        inkOverlayReverseGif.seekToFrame((inkOverlayGif.getCurrentFrameIndex()-48)*-1);
        inkOverlayReverseGif.setSpeed(spreadSpeed);
        inkOverlayReverseGif.start();
        overlay.get().Close();
    }

    public static void InkyIdleAnimation(){
        System.out.println("Running InkyIdleAnimation in overlayManager");
        ImageView im = (ImageView) inkyOverlay.get().mView.findViewById(R.id.inky_overlay);
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
        ImageView im = (ImageView) inkyOverlay.get().mView.findViewById(R.id.inky_overlay);
        im.setImageDrawable(inkyOverlayIntroGif);
        inkyOverlayIntroGif.setSpeed(movementSpeed);
        inkyOverlayIntroGif.reset();
        inkyOverlayIdleGif.start();
        inkyOverlay.get().Open();
        System.out.println("Complete InkyWalkingAnimation in overlayManager");
    }

    public static void InkyClose(){
        inkyOverlay.get().Close();
    }


}
