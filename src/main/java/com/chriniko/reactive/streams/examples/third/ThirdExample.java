package com.chriniko.reactive.streams.examples.third;

import com.chriniko.reactive.streams.examples.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ThirdExample implements Example {

    @Override
    public void run() {

        // create a stream...
        Flux<String> stream = Flux.<String>create(
                fluxSink -> {

                    fluxSink.serialize();
                    fluxSink.setCancellation(() -> System.out.println("Cancellation occurred!"));

                    try {

                        for (; ; ) {

                            try {
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException ignored) {
                            }


                            fluxSink.next(UUID.randomUUID().toString());
                        }

                    } catch (Exception error) {
                        fluxSink.error(error);
                    }

                },
                FluxSink.OverflowStrategy.DROP);


        // create a subscriber...
        stream
                .subscribeOn(Schedulers.parallel()) /* producing flow */
                .publishOn(Schedulers.parallel()) /* receiving flow */
                .retry(5)
                .map(uuidAsString -> uuidAsString.split("-")[0])
                .filter(p -> p != null && !p.isEmpty())
                .zipWith(
                        Flux.<Integer>create(
                                fluxSink -> {

                                    fluxSink.serialize();
                                    fluxSink.setCancellation(() -> System.out.println("Cancellation occurred!"));

                                    try {
                                        while (true) {

                                            try {
                                                TimeUnit.SECONDS.sleep(7);
                                            } catch (InterruptedException ignored) {
                                            }

                                            int randomInt = ThreadLocalRandom.current().nextInt();
                                            fluxSink.next(randomInt);

                                        }
                                    } catch (Exception error) {
                                        fluxSink.error(error);
                                    }

                                },
                                FluxSink.OverflowStrategy.DROP
                        ),
                        (firstPartOfUuidAsString, randomInteger) -> firstPartOfUuidAsString + "#" + randomInteger
                )
                .subscribe(
                        s -> {
                            System.out.println("onNext, item = " + s);
                        },

                        throwable -> {
                            System.out.println("onError, item = " + throwable);
                        },

                        () -> {
                            System.out.println("onCompleted");
                        }
                );


        // in order to block main thread and see the example...
        for (; ; ) ;
    }
}
