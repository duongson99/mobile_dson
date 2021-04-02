package com.appteam.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.appteam.myapplication.databinding.ActivityAddJobBinding;

public class AddJobActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAddJobBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnAddJob.setOnClickListener(this);
        binding.addImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_image:{
                break;
            }
            case R.id.btn_add_job:{
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("new_job", new Job());
                startActivity(intent);
                break;
            }
        }

    }
}