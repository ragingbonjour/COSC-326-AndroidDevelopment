package com.example.cosc_326_l2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button backButton = (Button) findViewById(R.id.back_button_id);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                We can just end the current activity to return to the main entrypoint when the back button is pressed
                SettingActivity.this.finish();
            }
        });

//        Check what color mode the phone is in (light/dark)
        CheckBox nightMode = (CheckBox) findViewById(R.id.checkbox_dark_mode_id);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            nightMode.setChecked(true);
            nightMode.setText("Disable Dark Mode");
        } else {
            nightMode.setText("Enable Dark Mode");
        }
    }

    public void myClickMethod(View thisbox) {
        CheckBox nightMode = ((CheckBox) thisbox);
        if (nightMode.isChecked()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            nightMode.setText("Disable Dark Mode");
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            nightMode.setText("Enable Light Mode");
        }
    }
}

