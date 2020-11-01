package com.example.amg_system.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.List;

public class SystemInfoUtils {

    public  static  int getRunningProcessCount() {
        List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
        return processes.size();
    }

    public static long getAvailRam(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static long getTotalRam(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.totalMem;
    }
}
