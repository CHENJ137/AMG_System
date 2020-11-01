package com.example.amg_system.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceStatusUtils {

    /**
     * Check if service is running
     *
     * @param context
     * @param serviceName
     * @return boolean
     */

    public static boolean inRunningService(Context context, String serviceName) {
        //ActivityManager
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);

        for (ActivityManager.RunningServiceInfo service : serviceInfos) {
            String name = service.service.getClassName();
            if (serviceName.equals(name)) {
                return true;
            }
        }

        return false;
    }
}
