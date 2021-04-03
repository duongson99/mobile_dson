package com.appteam.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.appteam.myapplication.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding binding;
    JobAdapter jobAdapter;
    List<Job> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = getIntent();
        Job object = (Job) getIntent().getSerializableExtra("new_job");
        if(object != null){
            list.add(object);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpRecyclerView();
        binding.btnAdd.setOnClickListener(this);
    }
    private void setUpRecyclerView(){
        list.add(new Job("a",1.0 ,"1/1/2021",true));
        jobAdapter = new JobAdapter(list);

        binding.recyclerJob.setAdapter(jobAdapter);
        binding.recyclerJob.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerJob.setHasFixedSize(true);
    }
    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, AddJobActivity.class));
    }
}