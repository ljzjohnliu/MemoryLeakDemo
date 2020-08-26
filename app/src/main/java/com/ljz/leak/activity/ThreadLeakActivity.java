package com.ljz.leak.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.leak.R;

public class ThreadLeakActivity extends AppCompatActivity {

    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_static_reference);

        mThread = new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){

                }
            }
        };
        mThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}