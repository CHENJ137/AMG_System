package com.example.amg_system.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.List;

public class SystemInfoUtils {

    /**
     * 得到当前手机运行进程的总数
     * @return
     */
    public  static  int getRunningProcessCount() {
        List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
        return processes.size();
    }

    /**
     * 得到可用，剩余内存
     * @param context
     * @return
     */
    public static long getAvailRam(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }

    /**
     * 得到总的内存
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static long getTotalRam(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.totalMem;
    }
}
