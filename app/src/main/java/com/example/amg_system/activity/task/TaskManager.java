package com.example.amg_system.activity.task;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import com.example.amg_system.R;
import com.example.amg_system.domain.TaskInfo;
import com.example.amg_system.engine.TaskInfoProvider;
import com.example.amg_system.utils.SystemInfoUtils;

import java.util.ArrayList;
import java.util.List;


public class TaskManager extends AppCompatActivity {

    private TextView tvRunProcessCount;
    private TextView tvAvailRam;
    private ListView lvTaskmanger;
    private LinearLayout llTaskLoading;
    private TextView tvTaskStatus;

    private List<TaskInfo> taskInfos;//all active activities list
    private List<TaskInfo> systemtaskInfos;//system active activities list
    private List<TaskInfo> usertaskInfos;//user active activities list

    private int runningProcessConut;//system active activities
    private long availRam;//available ram
    private long totalRam;//total ram

    private TaskInfoAdapter adapter;
    private ActivityManager am;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    adapter = new TaskInfoAdapter();
                    lvTaskmanger.setAdapter(adapter);
                    llTaskLoading.setVisibility(View.GONE);
                    tvTaskStatus.setVisibility(View.VISIBLE);
                    break;
                case 1:

                    tvRunProcessCount.setText("Active activities：" + msg.getData().getInt("runningProcessConut") + "个");
                    tvAvailRam.setText("Available/Total Ram：" + Formatter.formatFileSize(TaskManager.this, msg.getData().getLong("availRam")) + "/" + Formatter.formatFileSize(TaskManager.this, totalRam));
                    Toast.makeText(TaskManager.this, "Killed：" + msg.getData().getInt("killedCount") +
                            "activies, release" + Formatter.formatFileSize(TaskManager.this, msg.getData().getLong("addRam")) + "M Ram", Toast.LENGTH_SHORT).show();

                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmanager);
        am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

        tvRunProcessCount = (TextView) findViewById(R.id.tv_run_process_count);
        tvAvailRam = (TextView) findViewById(R.id.tv_avail_ram);
        lvTaskmanger = (ListView) findViewById(R.id.lv_taskmanger);
        llTaskLoading = (LinearLayout) findViewById(R.id.ll_task_loading);
        tvTaskStatus = (TextView) findViewById(R.id.tv_task_status);


//        runningProcessConut = SysTemInfoUtils.getRunningProcessCount(this);//5.1挂掉了
        runningProcessConut = SystemInfoUtils.getRunningProcessCount();
        availRam = SystemInfoUtils.getAvailRam(this);
        totalRam = SystemInfoUtils.getTotalRam(this);


        tvRunProcessCount.setText("Active activities：" + runningProcessConut);
        tvAvailRam.setText("Available/Total Ram: " + Formatter.formatFileSize(this, availRam) + "/" + Formatter.formatFileSize(this, totalRam));

        fillData();

        lvTaskmanger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = lvTaskmanger.getItemAtPosition(position);
                if (object != null) {
                    TaskInfo taskInfo = (TaskInfo) object;
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.cr_taskmanager_status);
                    if (getPackageName().equals(taskInfo.getPackname())) {
                        return;
                    }
                    if (taskInfo.isChecked()) {
                        //select
                        taskInfo.setChecked(false);
                        checkBox.setChecked(false);
                    } else {
                        taskInfo.setChecked(true);
                        checkBox.setChecked(true);
                    }
                }


            }
        });

        /**
         * Scroll listener
         */
        lvTaskmanger.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (systemtaskInfos == null || usertaskInfos == null) {
                    return;
                }

                if (firstVisibleItem > usertaskInfos.size()) {
                    tvTaskStatus.setText("System Activities(" + systemtaskInfos.size() + ")");
                } else {
                    tvTaskStatus.setText("User Activities(" + usertaskInfos.size() + ")");
                }
            }
        });


    }

    /**
     * Load data
     */
    private void fillData() {
        llTaskLoading.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                taskInfos = TaskInfoProvider.getAllTaskInfos(TaskManager.this);
                systemtaskInfos = new ArrayList<TaskInfo>();
                usertaskInfos = new ArrayList<TaskInfo>();
                for (TaskInfo taskInfo : taskInfos) {
                    if (taskInfo.isUser()) {
                        usertaskInfos.add(taskInfo);
                    } else {
                        systemtaskInfos.add(taskInfo);
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }


    /**
     * Fill data
     */
    private class TaskInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            boolean showsystem = sp.getBoolean("showsystem", true);
            if (showsystem) {
                return usertaskInfos.size() + 1 + systemtaskInfos.size() + 1;
            } else {
                return usertaskInfos.size() + 1;
            }

        }

        @Override
        public Object getItem(int position) {
            TaskInfo taskInfo;
            if (position == 0) {
                return null;
            } else if (position == usertaskInfos.size() + 1) {
                return null;
            } else if (position <= usertaskInfos.size()) {
                //用户进程
                int newposition = position - 1;
                taskInfo = usertaskInfos.get(newposition);
            } else {
                int newposition = position - usertaskInfos.size() - 1 - 1;
                taskInfo = systemtaskInfos.get(newposition);
            }
            return taskInfo;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TaskInfo taskInfo;
            if (position == 0) {
                TextView tv = new TextView(TaskManager.this);
                tv.setHeight(0);
                return tv;
            } else if (position == usertaskInfos.size() + 1) {
                TextView tv = new TextView(TaskManager.this);
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("System activities(" + systemtaskInfos.size() + ")");
                tv.setTextColor(Color.WHITE);
                return tv;
            } else if (position <= usertaskInfos.size()) {
                int newposition = position - 1;
                taskInfo = usertaskInfos.get(newposition);
            } else {
                int newposition = position - usertaskInfos.size() - 1 - 1;
                taskInfo = systemtaskInfos.get(newposition);
            }
            View view;
            ViewHolder viewholder;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                viewholder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(TaskManager.this, R.layout.activity_taskmanager_item, null);
                viewholder = new ViewHolder();

                viewholder.tvTaskName = (TextView) view.findViewById(R.id.tv_task_name);
                viewholder.tvMeninFoSize = (TextView) view.findViewById(R.id.tv_meninfosize);
                viewholder.ivTaskIcon = (ImageView) view.findViewById(R.id.iv_task_icon);
                viewholder.cb_status = (CheckBox) view.findViewById(R.id.cr_taskmanager_status);

                view.setTag(viewholder);
            }
            viewholder.tvTaskName.setText(taskInfo.getName());
            viewholder.tvMeninFoSize.setText(Formatter.formatFileSize(TaskManager.this, taskInfo.getMeninfosize()));
            viewholder.ivTaskIcon.setImageDrawable(taskInfo.getIcon());

            if (taskInfo.isChecked()) {
                viewholder.cb_status.setChecked(true);
            } else {
                viewholder.cb_status.setChecked(false);
            }

            if (getPackageName().equals(taskInfo.getPackname())) {
                viewholder.cb_status.setVisibility(View.GONE);
            } else {
                viewholder.cb_status.setVisibility(View.VISIBLE);
            }

            return view;
        }
    }

    static class ViewHolder {
        TextView tvTaskName;
        TextView tvMeninFoSize;
        ImageView ivTaskIcon;
        CheckBox cb_status;
    }

    public void selectAll(View view) {
        for (TaskInfo usertaskInfo : usertaskInfos) {
            if (getPackageName().equals(usertaskInfo.getPackname())) {
                continue;
            }
            usertaskInfo.setChecked(true);
        }

        for (TaskInfo systemtaskInfo : systemtaskInfos) {
            systemtaskInfo.setChecked(true);

        }
        adapter.notifyDataSetChanged();

    }

    public void unSelect(View view) {
        for (TaskInfo usertaskInfo : usertaskInfos) {
            if (getPackageName().equals(usertaskInfo.getPackname())) {
                continue;
            }
            usertaskInfo.setChecked(!usertaskInfo.isChecked());
        }

        for (TaskInfo systemtaskInfo : systemtaskInfos) {
            systemtaskInfo.setChecked(!systemtaskInfo.isChecked());
        }
        adapter.notifyDataSetChanged();//getCount() -- getView()
    }

    public void killAll(View view) {

        final ProgressDialog dialog = new ProgressDialog(TaskManager.this);
        dialog.setMessage("Cleaning...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        new Thread() {
            int killedCount = 0;
            long addRam = 0;

            List<TaskInfo> killedTaskInfo = new ArrayList<>();

            @Override
            public void run() {

                for (TaskInfo usertaskInfo : usertaskInfos) {
                    if (usertaskInfo.isChecked()) {
                        am.killBackgroundProcesses(usertaskInfo.getPackname());
                        killedTaskInfo.add(usertaskInfo);
                        killedCount++;
                        addRam += usertaskInfo.getMeninfosize();
                    }
                }


                for (TaskInfo systemtaskInfo : systemtaskInfos) {

                    if (systemtaskInfo.isChecked()) {
                        am.killBackgroundProcesses(systemtaskInfo.getPackname());
                        killedTaskInfo.add(systemtaskInfo);
                        killedCount++;
                        addRam += systemtaskInfo.getMeninfosize();
                    }

                }

                for (TaskInfo taskInfo : killedTaskInfo) {
                    if (taskInfo.isUser()) {
                        usertaskInfos.remove(taskInfo);
                    } else {
                        systemtaskInfos.remove(taskInfo);
                    }
                }

                dialog.dismiss();


                runningProcessConut -= killedCount;
                availRam += addRam;


                Message msg = new Message();
                Bundle b = new Bundle();
                b.putInt("killedCount", killedCount);
                b.putLong("addRam", addRam);
                b.putInt("runningProcessConut", runningProcessConut);
                b.putLong("availRam", availRam);
                msg.setData(b);
                msg.what = 1;
                handler.sendMessage(msg);


            }
        }.start();

//        fillData();
    }

    public void ReEnterSetting(View view) {
        Intent intent = new Intent(TaskManager.this, TaskManagerSettingActivity.class);
        startActivityForResult(intent, 0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
    }
}