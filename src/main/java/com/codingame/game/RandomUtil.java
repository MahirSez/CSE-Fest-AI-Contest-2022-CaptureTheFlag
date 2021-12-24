package com.codingame.game;

import javax.inject.Singleton;
import java.util.Random;

@Singleton
public class RandomUtil {
    public static Random random;

    public static int randomInt(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public static int randomOddInt(int min, int max) {
        return 2 * randomInt(min / 2, (max - 1) / 2) + 1;
    }

    void init(long seed) {
        random = new Random(seed);
    }
}
