package com.example.soulware_week1.board.factory;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class BoardFixtureFactory {

    public static EasyRandom get() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .stringLengthRange(5, 15)
                .excludeField(f -> f.getName().equals("boardId"))
                .collectionSizeRange(1, 5);

        return new EasyRandom(parameters);
    }
}
