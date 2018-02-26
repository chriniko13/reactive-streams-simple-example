package com.chriniko.reactive.streams.examples.fourth;

import com.chriniko.reactive.streams.examples.Example;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class FourthExample implements Example {

    @Override
    public void run() {

        // NOTE: Throttling == [Windowing, Buffering, Sampling]

        // create a connectable stream...
        ConnectableFlux<Object> connectableFlux = Flux
                .create(
                        fluxSink -> {
                            while (true) {
                                fluxSink.next(ThreadLocalRandom.current().nextGaussian());
                            }
                        },
                        FluxSink.OverflowStrategy.BUFFER
                )
                .timeoutMillis(1000)
                .publish();


        // register subscribers...
        connectableFlux
                .subscribeOn(Schedulers.elastic())
                .publishOn(Schedulers.elastic())
                .sample(Duration.ofSeconds(2))
                .subscribe(randomNum -> System.out.println("randomNumber(sub1) = " + randomNum));

        connectableFlux
                .subscribeOn(Schedulers.elastic())
                .publishOn(Schedulers.elastic())
                .sample(Duration.ofSeconds(2))
                .subscribe(randomNum -> System.out.println("randomNumber(sub2) = " + randomNum));


        // start stream...
        connectableFlux.connect();


        // block main thread in order to see the example...
        while (true) ;
    }
}
