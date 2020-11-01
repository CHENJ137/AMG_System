package com.example.amg_system.activity.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.amg_system.R;
import com.example.amg_system.service.KillProcessService;
import com.example.amg_system.utils.ServiceStatusUtils;
//import com.socks.library.KLog;

public class TaskManagerSettingActivity extends AppCompatActivity {
    private CheckBox cbShowSystemProcess;
    private CheckBox cbKillProcess;
    private Intent killProcessIntent;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmanager_setting);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        cbKillProcess = (CheckBox) findViewById(R.id.cb_kill_process);
        cbShowSystemProcess = (CheckBox) findViewById(R.id.cb_show_system_process);


        boolean showsystem = sp.getBoolean("showsystem", true);
        if (showsystem) {
            cbShowSystemProcess.setText("当前状态：显示系统进程");
        } else {
            cbShowSystemProcess.setText("当前状态：隐藏系统进程");
        }
        cbShowSystemProcess.setChecked(showsystem);
        cbShowSystemProcess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbShowSystemProcess.setText("当前状态：显示系统进程");
                } else {
                    cbShowSystemProcess.setText("当前状态：隐藏系统进程");
                }

                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("showsystem", isChecked);
                editor.commit();
            }
        });


        killProcessIntent = new Intent(this, KillProcessService.class);
        boolean inRunningService = ServiceStatusUtils.inRunningService(this, "com.fangyi.mobilesafe.service.KillProcessService");
        //检测服务是否开启
        if (inRunningService) {
            cbKillProcess.setText("当前状态：锁屏杀死后台");
        } else {
            cbKillProcess.setText("当前状态：锁屏不杀后台");
        }
    }

}
