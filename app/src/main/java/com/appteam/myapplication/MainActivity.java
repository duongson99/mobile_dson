package com.appteam.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.appteam.myapplication.databinding.ActivityMainBinding;

import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    String height,weight = "";
    double mHeight, mWeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnCalc.setOnClickListener(this);
    }
    private Error checkInputValid(){
        try{
           mHeight = Double.parseDouble(height);
        }catch (Exception ex){
            return Error.HEIGHT;
        }
        try{
           mWeight = Double.parseDouble(weight);
        }catch (Exception ex){
            return Error.WEIGHT;
        }
        double test = mHeight/mWeight;
        if(test == Double.POSITIVE_INFINITY || test == Double.NEGATIVE_INFINITY){
            return Error.DIV_ZERO;
        }
        return Error.NONE;
    }

    @Override
    public void onClick(View v) {
        height = binding.editHeight.getText().toString();
        weight = binding.editWeight.getText().toString();
        switch (checkInputValid()){
            case NONE:{
                String result = String.valueOf((double) (mWeight/Math.pow(mHeight,2.0)));
                binding.textResult.setText(result);
                break;
            }
            case HEIGHT:{
                binding.editHeight.setError("Input Invalid");
                binding.editHeight.requestFocus();
                break;
            }
            case WEIGHT:{
                binding.editWeight.setError("Input Invalid");
                binding.editWeight.requestFocus();
                break;
            }
            case DIV_ZERO:{
                binding.editWeight.setError("Can not divide to 0");
                binding.editWeight.requestFocus();
                break;
            }
        }
    }
    enum Error{
        WEIGHT,
        HEIGHT,
        NONE,
        DIV_ZERO
    }
}