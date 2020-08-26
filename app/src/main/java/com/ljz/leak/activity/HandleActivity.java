package com.ljz.leak.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.leak.R;

public class HandleActivity extends AppCompatActivity {

    /**
     * 用过内部类创建的Handler对象，持有Activity的引用。
     * 当执行postDelayed()方法时，会把Handler装入一个Message，并把Message推到MessageQueue中，MessageQueue在一个Looper线程中不断轮询处理消息。
     * 因为延迟的时间足够长，当Activity退出时，消息队列还未处理Message，从而引发OOM
     *
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_asynctask);

        mHandler.sendEmptyMessageDelayed(0,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}