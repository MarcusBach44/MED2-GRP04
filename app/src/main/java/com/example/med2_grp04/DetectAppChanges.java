package com.example.med2_grp04;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class DetectAppChanges extends AccessibilityService {
    public static String currentPackage;
    public static String previousPackage;
    @Override
    public void onInterrupt() {}

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d("ACCESS", "Service Connected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            CharSequence newPackageChar = event.getPackageName();
            if (newPackageChar == null) return;

            String newPackage = newPackageChar.toString();
            if (!newPackage.equals(currentPackage)){
                previousPackage = currentPackage;
                currentPackage = newPackage;
                Log.d("SCREEN CHANGE", "App Changed");
                onAppChange(currentPackage);
            }
        }
    }

    public void onAppChange(String pkg){
        Intent intent = new Intent("APP_CHANGED");
        intent.putExtra("package", pkg);
        sendBroadcast(intent);
    }

}
