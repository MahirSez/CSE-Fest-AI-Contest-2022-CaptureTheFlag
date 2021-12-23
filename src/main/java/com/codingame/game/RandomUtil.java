package com.codingame.game;

import javax.inject.Singleton;
import java.util.Random;

@Singleton
public class RandomUtil {
    private static Random random;

    public static int randomInt(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    void init(long seed) {
        random = new Random(seed);
    }
}
