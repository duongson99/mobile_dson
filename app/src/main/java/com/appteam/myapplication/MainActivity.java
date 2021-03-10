package com.appteam.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnSubmit;
    CheckBox cbIPhone, cbAndroid, cbWPhone, cbTennis, cbRunning, cbSwim, cbSleep, cbRead;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    RatingBar rbLove;
    TextView tvResult;
    Spinner spCountry, spUni;
    String platform = "", gender = "", country = "", university = "", favor = "";
    int rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSubmit = findViewById(R.id.btn_submit);
        cbIPhone = findViewById(R.id.cb_iphone);
        cbAndroid = findViewById(R.id.cb_android);
        cbWPhone = findViewById(R.id.cb_wphone);

        cbTennis = findViewById(R.id.cb_tennis);
        cbRunning = findViewById(R.id.cb_run);
        cbSwim = findViewById(R.id.cb_swim);
        cbSleep = findViewById(R.id.cb_sleep);
        cbRead = findViewById(R.id.cb_read);

        rgGender = findViewById(R.id.rg_gender);
        rbLove = findViewById(R.id.rb_love);
        spCountry = findViewById(R.id.spin_country);
        spUni = findViewById(R.id.spin_uni);
        rbMale = findViewById(R.id.sexMale);
        rbFemale = findViewById(R.id.sexfemale);
        tvResult = findViewById(R.id.result);
        btnSubmit.setOnClickListener(v -> doOnClick());

    }

    private void doOnClick() {
        if (cbIPhone.isChecked()) {
            platform += cbIPhone.getText()+" ";
        }
        if (cbAndroid.isChecked()) {
            platform += cbAndroid.getText()+" ";
        }
        if (cbWPhone.isChecked()) {
            platform += cbWPhone.getText()+" ";
        }

        if (cbTennis.isChecked()) {
            favor += cbTennis.getText()+" ";
        }
        if (cbRunning.isChecked()) {
            favor += cbRunning.getText()+" ";
        }
        if (cbSwim.isChecked()) {
            favor += cbSwim.getText()+" ";
        }
        if (cbSleep.isChecked()) {
            favor += cbSleep.getText()+" ";
        }
        if (cbRead.isChecked()) {
            favor += cbRead.getText()+" ";
        }
        rate = (int) rbLove.getRating();


        country = spCountry.getSelectedItem().toString();
        university = spUni.getSelectedItem().toString();

        if (rbMale.isSelected()) {
            gender = "Male";
        } else {
            gender = "Female";
        }
        tvResult.setText("");
        tvResult.append("Platform " + platform);
        tvResult.append("\nGender " + gender);
        tvResult.append("\nRate " + rate);
        tvResult.append("\nCountry " + country);
        tvResult.append("\nUni " + university);
        tvResult.append("\nFavor " + favor);

    }
}