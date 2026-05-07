package com.example.med2_grp04;

import android.widget.ImageView;
import java.lang.ref.WeakReference;
import pl.droidsonroids.gif.GifDrawable;
import androidx.appcompat.app.AppCompatActivity;


public class OverlayManager extends AppCompatActivity {
    public static GifDrawable inkOverlayGif;
    public static GifDrawable inkOverlayReverseGif;
    public static float inkSpreadSpeed = 0.05f;
    public static float inkRecoveryRate = 0.05f;
    private static WeakReference<InkOverlayWindow> inkOverlay;

    public static void updateInkOverlayWindow(InkOverlayWindow window){
        inkOverlay = new WeakReference<>(window);
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
}
