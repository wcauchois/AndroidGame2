package net.cloudhacking.androidgame2.engine.utils;

import android.graphics.RectF;

import java.util.Random;

/**
 * Created by wcauchois on 1/22/15.
 */
public class CommonUtils {
    private static ThreadLocal<Random> mRandGens = new ThreadLocal<Random>() {
        @Override public Random initialValue() {
            return new Random();
        }
    };

    public static Random getRandom() {
        return mRandGens.get();
    }

    public static <T> T randomChoice(T[] array) {
        return array[getRandom().nextInt(array.length)];
    }


    public static RectF makeRect(PointF topleft, float width, float height) {
        return new RectF(topleft.x, topleft.y, topleft.x + width, topleft.y + height);
    }

}
