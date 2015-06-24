package com.MVlab.BrickBreaker.utils;

/**
 * Created by MV on 30.03.2015.
 */
public class MV_Math {
    public static float abs(float x) {
        return x < 0 ? x * -1 : x;
    }

    public static float max(float x, float y) {
        return x < y ? y : x;
    }

    public static int max(int x, int y) {
        return x < y ? y : x;
    }

    public static float min(float x, float y) {
        return x > y ? y : x;
    }

    public static int min(int x, int y) {
        return x > y ? y : x;
    }

    public static float round(float value, int amount) {
        for (int i = 0; i < amount; i++) {
            value *= 10;
        }
        int tempValue = (int) value;
        value = tempValue;
        for (int i = 0; i < amount; i++) {
            value /= 10;
        }
        return value;
    }
}
