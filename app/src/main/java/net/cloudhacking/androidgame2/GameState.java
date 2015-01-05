package net.cloudhacking.androidgame2;

import android.util.Log;

/**
 * Created by wcauchois on 1/3/15.
 */
public class GameState {
    private static final String TAG = GameSurfaceView.class.getSimpleName();

    static int arenaWidth;
    static int arenaHeight;

    public static void setArenaSize(int[] arenaSize) {
        arenaWidth = arenaSize[0];
        arenaHeight = arenaSize[1];
        Log.d(TAG, "Arena size set to "+arenaWidth+"x"+arenaHeight);
    }

    public static int getArenaWidth() { return arenaWidth; }

    public static int getArenaHeight() { return arenaHeight; }

}
