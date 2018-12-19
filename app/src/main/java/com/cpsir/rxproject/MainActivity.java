package component.android.com.login.view.activty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import component.android.com.login.R;
import component.android.com.login.model.LanLnModel;
import component.android.com.login.network.RetrofitUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class RxJavaActivity extends AppCompatActivity {
    private static final String TAG  = "CZYAPP";
    private Button login_btn_get_server;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);
        login_btn_get_server=(Button)findViewById(R.id.login_btn_get_server);

        login_btn_get_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buffer();

            }
        });

    }

    /**
     * 一、创建操作符
     * 1.最基本的创建观察者和被观察者
     */
    private void create1(){
        /*第一种方式*/
        //创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: " + s);
            }
        };

        //创建被观察者对象
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext("被观察数据发生变化");
                    subscriber.onCompleted();
                }
            }
        });

        observable.subscribe(observer);
    }

    /**
     * 一、创建操作符
     * 2.链式创建方式
     */
    private void create2(){
        /*第二种方式，链式*/
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()){
                    subscriber.onNext("链式反应");
                    subscriber.onCompleted();
                }
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(String o) {
                Log.i(TAG, "onNext: "+o);
            }
        });
    }

    /**
     * 一、创建操作符
     * 3.使用Subscriber得观察者方式,使用方式跟Observer一样
     */
    private void create3(){
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };

    }

    /**
     * 一、创建操作符
     * 4..just(T…): 将传入的参数依次发送出来。
     */
    private void just(){
        Observable.just("aa", "bb", "cc")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i(TAG, "call: "+s);
                    }
                });

    }


    /**
     *  一、创建操作符
     * 5.from生成被观察者:可以传输集合,数组类型的数据
     */

    private void from(){
        List<String> list = new ArrayList<>();
        list.add("from1");
        list.add("from2");
        list.add("from3");
        list.add("from4");
        list.add("from5");

        Observable.from(list).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "call: " +s);
            }
        });
    }

    /**
     * 一、创建操作符
     * 6.range生成被观察者：可以实现多次发送数据，每次数据加1
     * 发送5次消息，从0开始，每次加1
     */

    private void range(){
        Observable.range(0,5).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.i(TAG, "call: " +integer);
            }
        });
    }


    /**
     *  一、创建操作符
     * 7.使用defer( )，有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable：
     */
    private void defer(){
        Observable.defer(new Func0<Observable<String>>(){
            @Override
            public Observable<String> call() {
                return Observable.just("22");
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "call: " +s);
            }
        });
    }


    /**
     * 一、创建操作符
     * 8.interval生成被观察者:每隔多久对外发送数字，从0开始，和Handler发送延迟消息很像,无限循环，
     */
    private void interval(){
        //延迟3秒发送数据，从0开始，每次加1
        Observable.interval(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {

                    }
                });
    }

    /**
     * 一、创建操作符
     * 9.使用timer( ),创建一个Observable，它在一个给定的延迟后发射一个特殊的值，
     * 等同于Android中Handler的postDelay( )方法：
     */
    private void timer(){
        Observable.timer(2,TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.i(TAG, "call: " +aLong);
                    }
                });
    }


    /**
     *  一、创建操作符
     * 10.如果你不在意数据是否接收完或者是否出现错误，即不需要Observer的onCompleted()和onError()方法，可使用Action1
     */
    private void create4(){
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "call: "+s);
            }
        };
    }


    /**
     *  一、创建操作符
     * 11.empty() 该方法创建的被观察者对象发送事件的特点：仅发送Complete事件，直接通知完成 即观察者接收后会直接调用onCompleted（）
     */
    private void empty(){
        Observable.empty()
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.i(TAG, "onNext: ");
                    }
                });
    }

    /**
     *  一、创建操作符
     * 12.error() 该方法创建的被观察者对象发送事件的特点：仅发送Error事件，直接通知异常 即观察者接收后会直接调用onError（）
     * 可自定义异常
     */

    private void error(){
        Observable.error(new RuntimeException())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: "+e.getStackTrace());
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    /**
     *  一、创建操作符
     * 13.never() 该方法创建的被观察者对象发送事件的特点：不发送任何事件 即观察者接收后什么都不调用
     * 可自定义异常
     */
    private void never(){
        Observable.never()
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
}


    /*                      变换操作符                                     */



    /**
     * 二、变换操作符
     * 1.map变换
     *
     */
    private void map(){
        Observable.just("20","30","8")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return Integer.parseInt(s);
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer o) {
                Log.i(TAG, "call: " +o);
            }
        });
    }


    /**
     * 二、变换操作符
     * 2.flatMap
     */
    private void flatMap(){
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
            }
        }).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.from(list);
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<String>() {
              @Override
              public void call(String s) {
                  Log.i(TAG, "call: " +s);
              }
          });
    }


    /**
     * 二、变换操作符
     * 3.concatMap
     */
    private void concatMap(){
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
            }
        }).concatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.from(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i(TAG, "call: " +s);
                    }
                });
    }


    /**
     * 三、变换操作符
     * 3.Buffer  定期从 被观察者（Obervable）需要发送的事件中 获取一定数量的事件 & 放到缓存区中，最终发送
     */

    private void buffer(){
        Observable.just(1,2,3,4,5)
                  .buffer(3,1)  // 设置缓存区大小 & 步长 // 缓存区大小 = 每次从被观察者中获取的事件数量 // 步长 = 每次获取新事件的数量
                  .subscribe(new Observer<List<Integer>>() {
                      @Override
                      public void onCompleted() {

                      }

                      @Override
                      public void onError(Throwable e) {

                      }

                      @Override
                      public void onNext(List<Integer> integers) {
                          Log.i(TAG, "onNext: "+integers.size());

                          for (Integer value : integers) {
                              Log.i(TAG, "onNext: 值"+value);
                          }
                      }
                  });

    }


    /**
     * 三、组合操作符
     * 1.zip
     */
    private void zip(){
        Observable.zip(getStringObservable(), getIntegerObservable(), new Func2<String, Integer, String>() {
            @Override
            public String call(String s, Integer integer) {
                return s+integer;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "zip call: " +s);
            }
        });
    }

    private Observable<String> getStringObservable(){

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("A");
                subscriber.onNext("B");
                subscriber.onNext("C");
            }
        });
    }


    private Observable<Integer> getIntegerObservable(){

        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onNext(4);
                subscriber.onNext(5);
            }
        });
    }


    /*                                 功能性操作符                                  */
    /**
     * 四、功能性操作符
     * 1.subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
        observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
     */
    private void shcedulThread(){
        Observable.just(1,2,3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i(TAG, "call: " +integer);
                    }
                });
    }

    /*               do操作符                         */
    /**
     * 五、do操作符
     * 1.doOnNext一般用于在subscribe之前对数据的一些处理，比如数据的保存等
     */

    private void doOnNext(){
        Observable.just(1,2,3,4)
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i(TAG, "call: " +integer);
                    }
                }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "onNext: " +integer);
            }
        });
    }

    /**
     * 五、do操作符
     * 2.doFinally 当它产生的Observable终止之后会被调用，无论是正常还 是异常终止
     */


 /*             RxJava+Retrofit2.0                              */
    /**
     *
     * Rx+Retrofit
     */

    private void rxRetrofit(){
        RetrofitUtils.getInstance().getLatLn("深圳","龙华","东泉新村")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LanLnModel>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: " );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " );
                    }

                    @Override
                    public void onNext(LanLnModel o) {
                        Log.i(TAG, "onNext: " +o.getLat());
                    }
                });
    }

    /**
     * 迭代轮训请求服务器
     */
    private void intervalRxRetrofit(){
        Observable.interval(2,1,TimeUnit.SECONDS)
                  .doOnNext(new Action1<Long>() {
                      @Override
                      public void call(Long aLong) {
                          rxRetrofit();
                      }
                  }).subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.i(TAG, "onNext: 第几次" +aLong);
            }
        });
    }

}
