package com.example.cosc_326_l2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Find button in activity layout via its ID
        Button settingsButton = (Button) findViewById(R.id.button_id);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Starts a new class called SettingActivity when we tap on the 'Settings' button in the primary activity
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
    }
}