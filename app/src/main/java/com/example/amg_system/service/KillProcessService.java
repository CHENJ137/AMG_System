package com.example.amg_system.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class KillProcessService extends Service {
    private Timer mTimer;
    private TimerTask mTask;

    private ScreenReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {

            }
        };

        mTimer.schedule(mTask, 2000, 4000);

        receiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);
    }

    private class ScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);

        receiver = null;
        mTimer.cancel();
        mTask.cancel();
        mTimer = null;
        mTask = null;

    }
}
