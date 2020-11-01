package com.example.amg_system.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.amg_system.R;
import com.example.amg_system.domain.TaskInfo;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.Statm;

import java.util.ArrayList;
import java.util.List;

public class TaskInfoProvider {

    public static List<TaskInfo> getAllTaskInfos(Context context) {
        List<TaskInfo> taskInfos = new ArrayList<>();

        List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
        PackageManager pm = context.getPackageManager();

        for (AndroidAppProcess process : processes) {
            TaskInfo taskInfo = new TaskInfo();
            try {
                Statm statm = process.statm();

                String packName = process.getPackageName();
                taskInfo.setPackname(packName);

                long totalSizeOfProcess = statm.getSize();
                long residentSetSize = statm.getResidentSetSize();
                taskInfo.setMeninfosize(residentSetSize);

                PackageInfo packageInfo = process.getPackageInfo(context, 0);

                Drawable drawable = packageInfo.applicationInfo.loadIcon(pm);
                taskInfo.setIcon(drawable);

                String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                taskInfo.setName(appName);

                int flag = packageInfo.applicationInfo.flags;
                if ((flag & ApplicationInfo.FLAG_SYSTEM) == 0) {

                    taskInfo.setUser(true);
                } else {

                    taskInfo.setUser(false);
                }

            } catch (Exception e) {
                e.printStackTrace();
                taskInfo.setName(process.getPackageName());
                taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher_foreground));
            }

            taskInfos.add(taskInfo);
        }

        return taskInfos;
    }

}
