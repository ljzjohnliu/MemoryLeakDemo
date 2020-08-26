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

        mThread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    //todo
                }
            }
        };
        mThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mThread){
            //interrupt()和Thread.interrupt()的区别
            //其中interrupt()是作用于调用线程的，比如我们下面调用的，他是作用于mThread这个线程的
            //如果我们在下面使用Thread.interrupt()那么就是作用于主线程的。
            mThread.interrupt();
        }
    }
}