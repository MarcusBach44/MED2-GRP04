package com.example.med2_grp04;

import static android.content.Context.USAGE_STATS_SERVICE;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class Helper {
    public static String getCurrentForeground(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);

        long time = System.currentTimeMillis();

        UsageEvents events = usm.queryEvents(time - 5000, time);
        UsageEvents.Event event = new UsageEvents.Event();

        String lastApp = getDefaultLauncherPackage(context);
        while (events.hasNextEvent()) {
            events.getNextEvent(event);

            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                String pkg = event.getPackageName();

                if (pkg != null){
                    lastApp = pkg;
                }
            }
        }
        return lastApp;
    }

    public static String getDefaultLauncherPackage(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        ResolveInfo resolveInfo = context.getPackageManager()
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
