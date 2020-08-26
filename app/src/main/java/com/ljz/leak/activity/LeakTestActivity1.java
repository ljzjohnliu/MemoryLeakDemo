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
     * 此时存在内存泄漏--持有外部类的引用
     */
    //解决内存泄漏的方式一：直接定义一个static类型的匿名内部类变量 不再持有外部类的引用
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 成员变量
     * static内部类实例
     *
     * 解决内存泄漏的方式二：定义一个static类型的内部类，继承匿名内部类，再定义一个它的实例
     */
    private MyHandler myHandler = new MyHandler();


    static class MyHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    }

    /**
     * 可以看到经过上面的处理，在handler和myHandler中都已经没有对所处外部类引用的this$0
     *
     * ps：
     *
     * 1.这里提供了两种方式，都能达到切断隐式的this$0，至于具体选择哪一种就得根据具体情况具体选择了
     *
     * 2.如果我们首先定义一个static类型的内部类，再定义一个static类型的它的实例，这个static类型的实例同样也不会有this$0引用指向外部类，读者朋友可以自行测试下
     *
     */


    /**
     * 成员变量
     * 非static内部类对象
     * 非static类型
     */
    //不能在外部类新建一个非static内部类的static实例  修饰myClass为static，必须要MyClass也是static才行
    //static内部类的实例都不会持有外部类的引用
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
                myHandler.removeMessages(0);
            }
        };
        /**
         * 方法局部变量
         * 非static内部类的实例
         */
        //static内部类的实例都不会持有外部类的引用
        MyClass myClass2 = new MyClass("This is a inner calss");
        myClass.toString();
        myClass2.toString();
        testBtn.setOnClickListener(clickListener);
    }

    static class MyClass {
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
