package com.ljz.leak.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.leak.R;

import java.lang.ref.WeakReference;

public class HandleLeakActivity extends AppCompatActivity {

    /**
     * 用过内部类创建的Handler对象，持有Activity的引用。
     * 当执行postDelayed()方法时，会把Handler装入一个Message，并把Message推到MessageQueue中，MessageQueue在一个Looper线程中不断轮询处理消息。
     * 因为延迟的时间足够长，当Activity退出时，消息队列还未处理Message，从而引发OOM
     *
     */
    //解决方案一：将mHandler定义为static类型 这样它就不在持有外部Activity的引用了，但是new Runnable的时候一样会持有外部Activity的引用,
    //所以如下只是添加个static类型并不能根除问题
//    private static Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };


    //

    /**
     *
     * 解决方案二：采用弱引用的形式 静态内部类
     * 思路就是不适用非静态内部类，继承Handler时，要么是放在单独的类文件中，要么就是使用静态内部类。
     * 因为静态的内部类不会持有外部类的引用，所以不会导致外部类实例的内存泄露。
     * 当你需要在静态内部类中调用外部的Activity时，我们可以使用弱引用来处理。
     * 另外关于同样也需要将Runnable设置为静态的成员属性。
     * 注意：一个静态的匿名内部类实例不会持有外部类的引用。
     * 修改后不会导致内存泄露的代码如下
     */
    public static class MyHandler extends Handler{
        private WeakReference<Activity> mActivity;
        public MyHandler(WeakReference<Activity> activityWeakReference){
            mActivity = activityWeakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(null != mActivity){
                Activity activity = mActivity.get();
                if(null != activity && !activity.isFinishing()){
                    //TODO do something...
                }
            }
        }
    }

    private final MyHandler mHandler = new MyHandler(new WeakReference<Activity>(this));

    /**
     * Instances of anonymous classes do not hold an implicit
     * reference to their outer class when they are static.
     */
    private static final Runnable sRunnable = new Runnable() {
        @Override
        public void run() {
            //todo
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_asynctask);

        mHandler.postDelayed(sRunnable, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不做以上改动的情况下,在Activity销毁的时候，将Handler里面的信息清除，但是这个可能未必是你想要的！！
        mHandler.removeCallbacksAndMessages(null);
    }
}