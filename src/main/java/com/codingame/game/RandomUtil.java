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

    public static int randomWeightedIndex(int[] weights) {
        int sum = 0;
        for (int x : weights) sum += x;
        int draw = randomInt(1, sum);
        sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
            if (sum >= draw) return i;
        }
        return 0;
    }

    void init(long seed) {
        random = new Random(seed);
    }
}
