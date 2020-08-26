package com.ljz.leak.activity;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.leak.R;

public class AsyncTaskActivity extends AppCompatActivity {

    private AsyncTask mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_asynctask);

        //如下这样的写法在退出的时候mAsyncTask是无法被销毁的,会导致Activity无法被销毁：
        mAsyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                while (true) ;
            }
        };
        mAsyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}