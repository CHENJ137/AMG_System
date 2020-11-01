package com.example.amg_system.activity.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.amg_system.R;
import com.example.amg_system.service.KillProcessService;
import com.example.amg_system.utils.ServiceStatusUtils;

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
            cbShowSystemProcess.setText("Status: show system activities");
        } else {
            cbShowSystemProcess.setText("Status: hide system activities");
        }
        cbShowSystemProcess.setChecked(showsystem);
        cbShowSystemProcess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbShowSystemProcess.setText("Status: show system activities");
                } else {
                    cbShowSystemProcess.setText("Status: show system activities");
                }

                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("showsystem", isChecked);
                editor.commit();
            }
        });


        killProcessIntent = new Intent(this, KillProcessService.class);
        boolean inRunningService = ServiceStatusUtils.inRunningService(this, "com.example.amg_system.service.KillProcessService");
        if (inRunningService) {
            cbKillProcess.setText("Status: kill activities when screen locked");
        } else {
            cbKillProcess.setText("Status: keep activities when screen locked");
        }
    }

}
