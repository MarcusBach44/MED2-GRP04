package com.example.med2_grp04;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class DetectAppChanges extends AccessibilityService {
    private static String lastPackage;
    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d("ACCESS", "Service Connected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            CharSequence pkgCs = event.getPackageName();
            if (pkgCs == null) return;

            String pkg = pkgCs.toString();
            if (!pkg.equals(lastPackage)){
                lastPackage = pkg;
                Log.d("SCREEN CHANGE", "App Changed");
                onAppChange(pkg);
            }
        }
    }

    public void onAppChange(String pkg){
        Intent intent = new Intent("APP_CHANGED");
        intent.putExtra("package", pkg);
        sendBroadcast(intent);
    }
}
