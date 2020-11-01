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
    private Timer mTimer;//定时器
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

        //定时器
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {

            }
        };

        mTimer.schedule(mTask, 2000, 4000);//2秒钟后开始，4秒钟轮循一次

        //监听锁屏事件
        receiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        //设置监听锁屏
        filter.addAction(Intent.ACTION_SCREEN_OFF);//ACTION_SCREEN_ON 屏幕开启
        registerReceiver(receiver, filter);
    }

    private class ScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //杀死后台进程
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消监听锁屏
        unregisterReceiver(receiver);

        receiver = null;
        mTimer.cancel();
        mTask.cancel();
        mTimer = null;
        mTask =null;

    }
}
