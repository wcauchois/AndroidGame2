package net.cloudhacking.androidgame2;

import android.util.Log;

/**
 * Created by wcauchois on 1/3/15.
 */
public class GameState {
    private static final String TAG = GameSurfaceView.class.getSimpleName();

    private int arenaWidth, arenaHeight;

    public void setArenaSize(int[] arenaSize) {
        arenaWidth = arenaSize[0];
        arenaHeight = arenaSize[1];
        Log.d(TAG, "Arena size set (width="+arenaWidth+"px, height="+arenaHeight+"px)");
    }

    public int getArenaWidth() { return arenaWidth; }

    public int getArenaHeight() { return arenaHeight; }

}
