package com.ljz.leak.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.leak.R;

public class StaticReferenceLeakActivity extends AppCompatActivity {

    public static Activity sInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_static_reference);
        sInstance = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 静态引用 会导致内存泄漏
         * 比如这个例子中代码，定义了sInstance来传递和使用，会导致StaticReferenceActivity无法被销毁，这是一种比较低级的错误，一般我们不建议这么使用，如果一定要使用，就需要在最后将sInstance置空。
         *
         */
        sInstance = null;
    }
}