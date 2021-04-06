package com.appteam.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appteam.myapplication.databinding.ActivityMainBinding;

import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtHeight,edtWeight;
    Button btnCalc;
    TextView tvResult;
    String height,weight = "";
    double mHeight, mWeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        btnCalc.setOnClickListener(this);
    }

    private void initView() {
        edtHeight = findViewById(R.id.edit_height);
        edtWeight = findViewById(R.id.edit_weight);
        btnCalc = findViewById(R.id.btn_calc);
        tvResult = findViewById(R.id.text_result);
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
        height = edtHeight.getText().toString();
        weight = edtWeight.getText().toString();
        switch (checkInputValid()){
            case NONE:{
                String result = String.valueOf((double) (mWeight/Math.pow(mHeight,2.0)));
                tvResult.setText(result);
                break;
            }
            case HEIGHT:{
                edtHeight.setError("Input Invalid");
                edtHeight.requestFocus();
                break;
            }
            case WEIGHT:{
                edtWeight.setError("Input Invalid");
                edtWeight.requestFocus();
                break;
            }
            case DIV_ZERO:{
                edtHeight.setError("Can not divide to 0");
                edtWeight.requestFocus();
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