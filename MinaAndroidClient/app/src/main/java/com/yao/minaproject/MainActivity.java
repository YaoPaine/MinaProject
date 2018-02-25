package com.yao.minaproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yao.minaproject.minatest.ConnectionManager;
import com.yao.minaproject.minatest.MinaService;
import com.yao.minaproject.minatest.SessionManager;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver messageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(ConnectionManager.MESSAGE);
            tvTitle.setText(message);
        }
    };
    private Button button1, button2;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.getInstance().writeToServer("你好哇，邓晓会");
            }
        });
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, MinaService.class));
            }
        });
        tvTitle = findViewById(R.id.tv_title);

        registerBroadcast();
    }

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter(ConnectionManager.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    private void unRegisterBroadcast() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MinaService.class));
        unRegisterBroadcast();
    }
}
