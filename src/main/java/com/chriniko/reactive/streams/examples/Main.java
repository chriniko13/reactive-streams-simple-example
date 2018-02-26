package com.chriniko.reactive.streams.examples;

import com.chriniko.reactive.streams.examples.fifth.FifthExample;
import com.chriniko.reactive.streams.examples.first.FirstExample;
import com.chriniko.reactive.streams.examples.fourth.FourthExample;
import com.chriniko.reactive.streams.examples.second.SecondExample;
import com.chriniko.reactive.streams.examples.third.ThirdExample;

public class Main {


    public static void main(String[] args) {

        Example[] examples = new Example[]{
                new FirstExample(),
                new SecondExample(),
                new ThirdExample(),
                new FourthExample(),
                new FifthExample()
        };

        final int exampleToRun = 5; // Note: touch only this.

        examples[exampleToRun - 1].run();
    }
}
