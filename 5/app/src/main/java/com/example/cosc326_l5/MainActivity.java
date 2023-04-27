package com.example.cosc326_l5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    MyService boundService;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            boundService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

//    UI Button functionality
    public void on_create_channel_click(View view) {
        if (isBound) {
            EditText id = (EditText) findViewById(R.id.channel_id);
            EditText name = (EditText) findViewById(R.id.channel_name);
            EditText description = (EditText) findViewById(R.id.channel_description);
            CheckBox importance = (CheckBox) findViewById(R.id.channel_importance);
            if(importance.isChecked()) {
                boundService.createChannel(id.getText().toString(), name.getText().toString(), description.getText().toString(), 4);
            } else {
                boundService.createChannel(id.getText().toString(), name.getText().toString(), description.getText().toString(), 4);
            }
        }
    }

    public void on_create_notification_click(View view) {
        if (isBound) {
            EditText id = (EditText) findViewById(R.id.notification_id);
            EditText title = (EditText) findViewById(R.id.notification_name);
            EditText message = (EditText) findViewById(R.id.notification_message);
            CheckBox autocancel = (CheckBox) findViewById(R.id.notification_dismiss);
            if(autocancel.isChecked()) {
                PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_IMMUTABLE);
                boundService.createNotification(id.getText().toString(), message.getText().toString(), title.getText().toString(), intent);
            } else {
                boundService.createNotification(id.getText().toString(), message.getText().toString(), title.getText().toString(), null);
            }
        }
    }

    public void on_delete_click(View view) {
        if (isBound) {
            boundService.deleteLast();
        }
    }

    public void on_oneshot_click(View view) {
        Intent intent = new Intent(this, MyService.class);
        getApplicationContext().startService(intent);
    }

    public void on_unbind_click(View view) {
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
}