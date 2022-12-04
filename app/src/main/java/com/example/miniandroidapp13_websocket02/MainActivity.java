package com.example.miniandroidapp13_websocket02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebSocketClient webSocketClient = new WebSocketClient();
        webSocketClient.send("Hello from Android");
    }

}