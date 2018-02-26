package com.chriniko.reactive.streams.examples.first;

import com.chriniko.reactive.streams.examples.Example;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

public class FirstExample implements Example {

    @Override
    public void run() {

        // stream creation...
        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1));


        // stream subscription...
        flux
                .log()
                .onBackpressureDrop()
                .subscribeOn(Schedulers.parallel())
                .publishOn(Schedulers.parallel())
                .buffer(10)
                .subscribe(new Subscriber<List<Long>>() {

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(List<Long> longValues) {
                        System.out.println("[threadName = "
                                + Thread.currentThread().getName()
                                + "] longValues = "
                                + longValues);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("[threadName = " + Thread.currentThread().getName() + "] error = " + throwable);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("[threadName = " + Thread.currentThread().getName() + "] completed!");
                    }

                });


        // block in order to demonstrate the example...
        for (; ; ) ;

    }

}
