package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.view.KeyEvent;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Get text input object via ID
        EditText tedit = (EditText) findViewById(R.id.textinput);

//        Get text output object via ID
        TextView tview = (TextView) findViewById(R.id.textoutput);

//        Setup listener on it to listen for the keyboard being closed/enter key pressed
        tedit.setOnEditorActionListener((new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    tview.setText(tedit.getText().toString().toUpperCase());
                }
                return false;
            }
        }));
    }
}