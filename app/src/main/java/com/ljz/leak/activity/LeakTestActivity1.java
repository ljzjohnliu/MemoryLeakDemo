package com.ljz.leak.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ljz.leak.R;

public class LeakTestActivity1 extends AppCompatActivity {
    private Button testBtn;
    /**
     * 成员变量
     * 匿名内部类对象
     * 非static类型
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    /**
     * 成员变量
     * 非static内部类对象
     * 非static类型
     */
    private MyClass myClass = new MyClass("this is a inner class, no static.");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_handler);
        testBtn = findViewById(R.id.async_work);
        /**
         * 方法局部变量
         * 匿名内部类的实例
         * 就是平时经常注册的各种监听器
         */
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ljz", "onClick: I am just a Test!");
                handler.removeMessages(0);
            }
        };
        /**
         * 方法局部变量
         * 非static内部类的实例
         */
        MyClass myClass2 = new MyClass("This is a inner calss");
        myClass2.toString();
        testBtn.setOnClickListener(clickListener);
    }

    class MyClass {
        String des;

        public MyClass(String des) {
            this.des = des;
        }

        @NonNull
        @Override
        public String toString() {
            return des;
        }
    }
}
