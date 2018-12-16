package com.cpsir.rxproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CZYAPP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        just();
        from();
        range();
    }

    /**
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
     * 4.如果你不在意数据是否接收完或者是否出现错误，即不需要Observer的onCompleted()和onError()方法，可使用Action1
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
     * 5..just(T…): 将传入的参数依次发送出来。
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
     * 6.from生成被观察者:可以传输集合,数组类型的数据
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
     * 7.range生成被观察者：可以实现多次发送数据，每次数据加1
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
     * 8.使用defer( )，有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable：
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
     * 9.interval生成被观察者:每隔多久对外发送数字，从0开始，和Handler发送延迟消息很像,无限循环，
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
}
