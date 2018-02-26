package com.chriniko.reactive.streams.examples.fifth;

import com.chriniko.reactive.streams.examples.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;

public class FifthExample implements Example {

    @Override
    public void run() {

        // create a stream...
        ParallelFlux<Integer> parallelFlux = Flux
                .fromIterable(Collections.singleton(1))
                .repeat()
                .parallel(Runtime.getRuntime().availableProcessors())
                .runOn(Schedulers.parallel())
                .doOnNext(integer -> System.out.println("incoming integer = " + ++integer));

        // create a subscriber...
        parallelFlux.subscribe(integer -> System.out.println("onNext = " + integer));

        // in order to block and see the example...
        for (; ; ) ;
    }
}
