package com.example.amg_system.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.amg_system.R;
import com.example.amg_system.activity.task.TaskManager;

public class MainActivity extends AppCompatActivity {
    Button btnActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnActivity = findViewById(R.id.button4);
        btnActivity.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, TaskManager.class);
                startActivity(intent);
            }
        });
    }

}