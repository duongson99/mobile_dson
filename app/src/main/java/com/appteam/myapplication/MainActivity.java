package com.appteam.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.appteam.myapplication.adapter.JobAdapter;
import com.appteam.myapplication.model.Job;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,PassData {
    Button btnAdd;
    FrameLayout fragmentContainer;
    RecyclerView recyclerJob;
    JobAdapter jobAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        btnAdd.setOnClickListener(this);
    }

    private void initView() {
        btnAdd = findViewById(R.id.btn_add);
        fragmentContainer = findViewById(R.id.fragment_container);
        jobAdapter = new JobAdapter();
        recyclerJob = findViewById(R.id.recycler_job);
        recyclerJob.setAdapter(jobAdapter);
        recyclerJob.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerJob.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {
        btnAdd.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new AddJobFragment());
        ft.commit();
    }

    @Override
    public void onPassData(Job job) {
        btnAdd.setVisibility(View.VISIBLE);
        jobAdapter.addList(job);
        jobAdapter.notifyDataSetChanged();
    }
}