package com.appteam.myapplication;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.appteam.myapplication.model.Job;

import java.util.Calendar;
import java.util.Objects;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;

public class AddJobFragment extends Fragment implements View.OnClickListener {

    Button btnSubmit;
    ImageView imageView;
    EditText edtName,edtSalary;
    TextView dateCreated;
    RadioButton radioActivated;
    DatePickerDialog picker;
    final int IMAGE_CODE = 12345;
    private Uri imageUri;
    PassData passData;
    public AddJobFragment() {
        // Required empty public constructor
    }

    public static AddJobFragment newInstance(String param1, String param2) {
        AddJobFragment fragment = new AddJobFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_job, container, false);
        initView(view);
        passData = (PassData) getContext();
        btnSubmit.setOnClickListener(this);
        dateCreated.setOnClickListener(this);
        imageView.setOnClickListener(this);
        return view;
    }

    private void initView(View view) {
        btnSubmit = view.findViewById(R.id.btn_submit);
        imageView = view.findViewById(R.id.get_image);
        edtName = view.findViewById(R.id.edt_name);
        edtSalary = view.findViewById(R.id.edt_salary);
        dateCreated  = view.findViewById(R.id.edt_date);
        radioActivated = view.findViewById(R.id.rb_activated);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:{
                Job newJob = new Job(imageUri,edtName.getText().toString(),Double.parseDouble(edtSalary.getText().toString()),
                        dateCreated.getText().toString(), radioActivated.isChecked());
                passData.onPassData(newJob);
                requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            }
            case R.id.get_image:{
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_CODE);
                break;
            }
            case R.id.edt_date:{
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(requireActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateCreated.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
                break;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super method removed
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CODE) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }
}