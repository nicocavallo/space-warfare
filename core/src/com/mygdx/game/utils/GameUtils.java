package com.mygdx.game.utils;

import java.util.Random;

/**
 * Created by Nicolas on 28/02/2016.
 */
public class GameUtils {

    public static final Random RANDOM = new Random();

    public static <T> T choose( T... elems ) {
        int p = RANDOM.nextInt(elems.length);
        return elems[p];
    }

    public static float abs(float a) {
        return (a <= 0.0F) ? 0.0F - a : a;
    }

}
