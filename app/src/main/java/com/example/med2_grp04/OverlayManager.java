package com.example.med2_grp04;

import android.widget.ImageView;
import java.lang.ref.WeakReference;
import pl.droidsonroids.gif.GifDrawable;
import androidx.appcompat.app.AppCompatActivity;


public class OverlayManager extends AppCompatActivity {
    public static GifDrawable inkOverlayGif;
    public static GifDrawable inkOverlayReverseGif;
    public static float spreadSpeed = 0.05f;
    private static WeakReference<Window> overlay;

    public static void updateOverlayWindow(Window window){
        overlay = new WeakReference<>(window);
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
}
