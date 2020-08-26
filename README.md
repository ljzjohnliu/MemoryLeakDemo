# MemoryLeakDemo
通过Demo模拟各种内存泄漏场景

## 静态引用，会导致MainActivity无法被销毁，这是一种比较低级的错误，一般我们不建议这么使用，如果一定要使用，就需要在最后将sInstance置空。


## 内部类持有外部类引用
我们在处理Android内存泄漏的时候，经常会遇到一些是由于内部类持有外部类引用而引发的。严格来说是非static的内部类的非static对象或匿名内部类的非static对象持有了当前所在外部类的引用造成的。
断点调试可验证该问题，并且有截图能清晰看到

## 使用AsyncTask导致的内存泄漏
解决办法：在Activity销毁的时候取消正在运行的AsyncTask

## Handle造成的内存泄漏
用过内部类创建的Handler对象，持有Activity的引用。当执行postDelayed()方法时，会把Handler装入一个Message，并把Message推到MessageQueue中，MessageQueue在一个Looper线程中不断轮询处理消息。
因为延迟的时间足够长，当Activity退出时，消息队列还未处理Message，从而引发OOM

解决方式有几种：
1、在Activity销毁的时候，将Handler里面的信息清除，但是这个可能未必是你想要的！！
2、思路就是不适用非静态内部类，继承Handler时，要么是放在单独的类文件中，要么就是使用静态内部类。
因为静态的内部类不会持有外部类的引用，所以不会导致外部类实例的内存泄露。
当你需要在静态内部类中调用外部的Activity时，我们可以使用弱引用来处理。
另外关于同样也需要将Runnable设置为静态的成员属性。
注意：一个静态的匿名内部类实例不会持有外部类的引用。 

