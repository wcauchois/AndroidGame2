package net.cloudhacking.androidgame2.engine.utils;

/**
 * Created by Andrew on 1/17/2015.
 */
public class GameTime {

    /**
     * Can access this in a static context.  Get the elapsed time during a frame by
     * calling getFrameDelta() which returns the elapsed time in seconds.
     */

    private static float sTimeScale = 1f;

    private static long sTimeNow;
    private static long sTimeLastTick;

    public static void setTimeScale(float timeScale) {
        sTimeScale = timeScale;
    }

    public static void start() {
        sTimeNow = 0;
    }

    public static void tick() {
        sTimeLastTick = sTimeNow;
        sTimeNow = System.currentTimeMillis();
    }

    /**
     * @return Seconds since last frame (scaled by the set time scale)
     */
    public static float getFrameDelta() {
        if (sTimeLastTick == 0) {
            return 0f;
        }
        return sTimeScale * (sTimeNow - sTimeLastTick) * 0.001f;  // convert from ms to s
    }

}
