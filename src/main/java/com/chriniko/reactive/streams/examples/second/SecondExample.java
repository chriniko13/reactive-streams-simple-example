package com.chriniko.reactive.streams.examples.second;

import com.chriniko.reactive.streams.examples.Example;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class SecondExample implements Example {

    @Override
    public void run() {

        // create stream...
        Mono<List<Long>> mono = Mono.<List<Long>>fromSupplier(() -> Arrays.asList(1L, 2L, 3L));

        mono.subscribe(new Subscriber<List<Long>>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(List<Long> longs) {
                System.out.println("onNext = " + longs);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError = " + throwable);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });


    }
}
