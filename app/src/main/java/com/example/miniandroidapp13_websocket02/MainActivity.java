package com.example.miniandroidapp13_websocket02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.miniandroidapp13_websocket02.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView);
        Button button_left = (Button) findViewById(R.id.button_left);
        Button button_center = (Button) findViewById(R.id.button_center);
        Button button_right = (Button) findViewById(R.id.button_right);
        button_left.setOnClickListener(this::onClick);
        button_center.setOnClickListener(this::onClick);
        button_right.setOnClickListener(this::onClick);
        textView.setText("未接続");

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getTextLiveData().observe (
                this,
                textObserver -> {
                    textView.setText(textObserver);
                }
        );
    }

    public void onClick(View v) {
        if (v.getId() == R.id.button_left) {
            mainViewModel.openWebSocket();
        } else if (v.getId() == R.id.button_center) {
            mainViewModel.closeWebSocket();
        } else if (v.getId() == R.id.button_right) {
            mainViewModel.sendMessageWebSocket("Hello from Android");
        }
    }

}