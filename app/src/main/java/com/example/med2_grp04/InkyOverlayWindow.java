package com.example.med2_grp04;

import static android.content.Context.WINDOW_SERVICE;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class InkyOverlayWindow {
    public Context context;
    public View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;
    public InkyOverlayWindow(Context context){
        this.context=context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    PixelFormat.TRANSLUCENT
            );
        }

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.activity_inky_overlay,null);
        mParams.gravity = Gravity.CENTER;
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
    }

    public void Open(){
        System.out.println("Running Open In InkyOverlayWindow");
        try{
            if (mView.getWindowToken()==null){
                if (mView.getParent()==null){
                    mWindowManager.addView(mView, mParams);
                }
            }
            System.out.println("Completing Open in MainActivity");
        }catch (Exception e){
            Log.d("Error Open", e.toString());
        }
    }

    public void Close(){
        System.out.println("Running Close In InkyOverlayWindow");
        try{
            ((WindowManager)context.getSystemService(WINDOW_SERVICE)).removeView(mView);
            mView.invalidate();
            ((ViewGroup)mView.getParent()).removeAllViews();
            System.out.println("Completing close in MainActivity");
        } catch (Exception e){
            Log.d("Error Close", e.toString());
        }
    }
}

