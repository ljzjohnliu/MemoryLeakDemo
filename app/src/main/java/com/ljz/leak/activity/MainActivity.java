package com.ljz.leak.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.leak.R;
import com.ljz.leak.utils.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        findViewById(R.id.simple_btn).setOnClickListener(this);
        findViewById(R.id.map_btn).setOnClickListener(this);
        findViewById(R.id.scheduler_btn).setOnClickListener(this);
        findViewById(R.id.retrofit_btn).setOnClickListener(this);
        findViewById(R.id.test_action).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.simple_btn:
                intent.setClassName("com.ljz.leak", "com.ljz.leak.activity.LeakTestActivity1");
                break;
            case R.id.map_btn:
                intent.setComponent(new ComponentName("com.ljz.leak", "com.ljz.leak.activity.TestRxMapActivity"));
                break;
            case R.id.scheduler_btn:
                intent.setComponent(new ComponentName("com.ljz.leak", "com.ljz.leak.activity.SchedulerActivity"));
                break;
            case R.id.retrofit_btn:
                intent.setComponent(new ComponentName("com.ljz.leak", "com.ljz.leak.activity.RetrofitActivity"));
                break;
//            case R.id.test_action:
//                break;
            default:
                ToastUtil.toast(mContext, "没有有效的跳转页面");
                return;
        }
        startActivity(intent);
    }
}
