package net.cloudhacking.androidgame2.engine.utils;

/**
 * Created by Andrew on 1/17/2015.
 */
public class GameTime {

    private static float sTimeScale;

    private static long sTimeNow;
    private static long sTimeSinceLastTick;

    private static boolean sFirstTick = true;

    public static void setTimeScale(float timeScale) {
        sTimeScale = timeScale;
    }

    public static void start() {
        sTimeNow = 0;
    }

    public static void reset() {
        sTimeNow = 0;
        sFirstTick = true;
    }

    public static void tick() {
        sTimeSinceLastTick = sTimeNow;
        sTimeNow = System.currentTimeMillis();
    }

    public static float getFrameDelta() {
        if (sFirstTick) {
            sFirstTick = false;
            return 0f;
        }
        return sTimeScale * (sTimeNow - sTimeSinceLastTick);
    }

}
