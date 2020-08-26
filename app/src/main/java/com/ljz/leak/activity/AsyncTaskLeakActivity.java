package com.ljz.leak.activity;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.leak.R;

public class AsyncTaskLeakActivity extends AppCompatActivity {

    private AsyncTask mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_asynctask);

        //如下这样的写法在退出的时候mAsyncTask是无法被销毁的,会导致Activity无法被销毁：
//        mAsyncTask = new AsyncTask() {
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                while (true) ;
//            }
//        };
//        mAsyncTask.execute();

        //改进后的代码 解决办法如下代码：在Activity销毁的时候取消正在运行的AsyncTask
        mAsyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                while (true){
                    if(isCancelled()){
                        break;
                    }
                }
                return null;
            }
        };
        mAsyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //改进后的代码，可解决内存泄漏问题
        if(null != mAsyncTask && !mAsyncTask.isCancelled()){
            mAsyncTask.cancel(true);
        }
        mAsyncTask = null;
    }
}