# MemoryLeakDemo
通过Demo模拟各种内存泄漏场景
## 一、Activity造成的内存泄漏
### 1.在Android开发中，最容易引发的内存泄漏问题的是Activity的Context
    在类中定义了静态Activity变量，把当前运行的Activity实例赋值于这个静态变量。如果这个静态变量在Activity生命周期结束后没有清空，就导致内存泄漏。
    因为static变量是贯穿这个应用的生命周期的，所以被泄漏的Activity就会一直存在于应用的进程中，不会被垃圾回收器回收。
    static Activity activity; //这种代码要避免

### 2.单例中保存Activity
    在单例模式中，如果Activity经常被用到，那么在内存中保存一个Activity实例是很实用的。但是由于单例的生命周期是应用程序的生命周期，这样会强制延长Activity的生命周期，
    这是相当危险而且不必要的，无论如何都不能在单例子中保存类似Activity的对象。
    在调用Singleton的getInstance()方法时传入了Activity。那么当instance没有释放时，这个Activity会一直存在。因此造成内存泄露。
    解决方法:
    可以将new Singleton(context)改为new Singleton(context.getApplicationContext())即可，这样便和传入的Activity没关系了。

    同理，静态的View也是不建议的

### 要解决Activity的长期持有造成的内存泄漏，可以通过以下方法：
    1.传入Application 的 Context，因为 Application 的生命周期就是整个应用的生命周期，所以这将没有任何问题。
    2.如果此时传入的是 Activity 的 Context，当这个 Context 所对应的 Activity 退出时，主动结束执行的任务，并释放Activity资源。
    3.将线程的内部类，改为静态内部类。
    因为非静态内部类会自动持有一个所属类的实例，如果所属类的实例已经结束生命周期，但内部类的方法仍在执行，就会hold其主体（引用）。也就使主体不能被释放，亦即内存泄露。
    静态类编译后和非内部类是一样的，有自己独立的类名。不会悄悄引用所属类的实例，所以就不容易泄露。

    备注，切记以下两点：
    如果需要引用Acitivity，使用弱引用。
    谨慎对context使用static关键字。


## 二、静态引用造成的内存泄漏
    当静态引用一个Activity的上下文Context的时候，会导致MainActivity无法被销毁，这是一种比较低级的错误，一般我们不建议这么使用，如果一定要使用，就需要在最后将sInstance置空。

## 三、Inner Classes 内部类持有外部类引用造成的内存泄漏
    内部类的优势可以提高可读性和封装性，而且可以访问外部类，不幸的是，导致内存泄漏的原因，就是内部类持有外部类实例的强引用。 例如在内部类中持有Activity对象
    我们在处理Android内存泄漏的时候，经常会遇到一些是由于内部类持有外部类引用而引发的。严格来说是非static的内部类的非static对象或匿名内部类的非static对象持有了当前所在外部类的引用造成的。
    断点调试可验证该问题，并且有截图能清晰看到

    解决方法：
    1.将内部类变成静态内部类;
    2.如果有强引用Activity中的属性，则将该属性的引用方式改为弱引用;
    3.在业务允许的情况下，当Activity执行onDestory时，结束这些耗时任务;

## 四、使用AsyncTask导致的内存泄漏
    解决办法：在Activity销毁的时候取消正在运行的AsyncTask

## 五、Handle造成的内存泄漏
    用过内部类创建的Handler对象，持有Activity的引用。当执行postDelayed()方法时，会把Handler装入一个Message，并把Message推到MessageQueue中，MessageQueue在一个Looper线程中不断轮询处理消息。
    因为延迟的时间足够长，当Activity退出时，消息队列还未处理Message，从而引发OOM
    另外，handler中，Runnable内部类会持有外部类的隐式引用，被传递到Handler的消息队列MessageQueue中，在Message消息没有被处理之前，Activity实例不会被销毁了，于是导致内存泄漏。

    解决方式有几种：
    1、在Activity销毁的时候，释放handler资源，将Handler里面的信息清除，但是这个可能未必是你想要的！！
    2、思路就是不适用非静态内部类，继承Handler时，要么是放在单独的类文件中，要么就是使用静态内部类。
    因为静态的内部类不会持有外部类的引用，所以不会导致外部类实例的内存泄露。
    另外关于同样也需要将Runnable设置为静态的成员属性。
    注意：一个静态的匿名内部类实例不会持有外部类的引用。
    3.如果想在Handler内部去调用所在的Activity,那么可以在handler内部使用弱引用的方式去指向所在Activity.使用Static + WeakReference的方式来达到断开Handler与Activity之间存在引用关系的目的. 

    同样还有其他匿名类实例，如TimerTask、Threads等，执行耗时任务持有Activity的引用，都可能导致内存泄漏。
    线程产生内存泄露的主要原因在于线程生命周期的不可控。如果我们的线程是Activity的内部类，所以MyThread中保存了Activity的一个引用，当MyThread的run函数没有结束时，MyThread是不会被销毁的，
    因此它所引用的老的Activity也不会被销毁，因此就出现了内存泄露的问题。

## 六、Thread引起的内存泄漏
    如何解决,无非就是在Activity销毁的时候去暂停Thread那么我们应该怎么做呢?
    interrupt()和Thread.interrupt()的区别:
    其中interrupt()是作用于调用线程的，比如我们上面调用的，他是作用于mThread这个线程的，如果我们在上面使用Thread.interrupt()那么就是作用于主线程的。

## 七、Bitmap没调用recycle()</h3>
    Bitmap对象在不使用时,我们应该先调用recycle()释放内存，然后才设置为null.

## 八、集合中对象没清理造成的内存泄露
    我们通常把一些对象的引用加入到了集合中，当我们不需要该对象时，并没有把它的引用从集合中清理掉，这样这个集合就会越来越大。如果这个集合是static的话，那情况就更严重了。
    解决方案：
     在Activity退出之前，将集合里的东西clear，然后置为null，再退出程序。
     
## 九、注册没取消造成的内存泄露
    这种Android的内存泄露比纯Java的内存泄漏还要严重，因为其他一些Android程序可能引用系统的Android程序的对象（比如注册机制）。
    即使Android程序已经结束了，但是别的应用程序仍然还有对Android程序的某个对象的引用，泄漏的内存依然不能被垃圾回收。
    解决方案：
     1.使用ApplicationContext代替ActivityContext;
     2.在Activity执行onDestory时，调用反注册;

## 十、资源对象没关闭造成的内存泄露
     资源性对象比如（Cursor，File文件等）往往都用了一些缓冲，我们在不使用的时候，应该及时关闭它们，以便它们的缓冲及时回收内存。而不是等待GC来处理。

## 十一、占用内存较多的对象(图片过大)造成内存溢出
    因为Bitmap占用的内存实在是太多了，特别是分辨率大的图片，如果要显示多张那问题就更显著了。Android分配给Bitmap的大小只有8M.
    解决方法：
     1.等比例缩小图片
       BitmapFactory.Options options = new BitmapFactory.Options();
       options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一
     2.对图片采用软引用，及时地进行recycle()操作
      //软引用
      SoftReference<Bitmap> bitmap = new SoftReference<Bitmap>(pBitmap); 
      //回收操作
      if(bitmap != null) {  
            if(bitmap.get() != null && !bitmap.get().isRecycled()){ 
                bitmap.get().recycle(); 
                bitmap = null;  
            } 
     } 

## 十二、WebView内存泄露（影响较大）
    WebView在解析网页时，会申请Native堆内存，保存页面元素（图片、history等）
    解决方案:
    用新的进程起含有WebView的Activity,并且在该Activity 的onDestory() 最后加上 System.exit(0); 杀死当前进程。

## 其他原因
    ContentObserver
    Cursor
    Stream
   
