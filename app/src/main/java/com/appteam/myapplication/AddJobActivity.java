package com.appteam.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.appteam.myapplication.databinding.ActivityAddJobBinding;

import java.util.Calendar;

import javax.xml.transform.Result;

public class AddJobActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAddJobBinding binding;
    public static final int CODE_IMAGE = 1234;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnAddJob.setOnClickListener(this);
        binding.addImage.setOnClickListener(this);
        binding.editDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_image: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, CODE_IMAGE);
                break;
            }
            case R.id.btn_add_job: {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("new_job", new Job(binding.editName.getText().toString(),
                        Double.parseDouble(binding.editSalary.getText().toString()),
                        binding.editDate.getText().toString(),
                        binding.btnActivated.isSelected()
                ));
                startActivity(intent);
                break;
            }
            case R.id.edit_date: {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.editDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
                break;
            }
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_IMAGE) {
                Uri imageUri = data.getData();
                Log.d("Image Log", imageUri.toString());
                binding.addImage.setImageURI(imageUri);
            }
        }
    }
}