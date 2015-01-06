package net.cloudhacking.androidgame2;

import android.util.Log;

/**
 * Created by wcauchois on 1/3/15.
 */
public class GameState {
    private static final String TAG = GameSurfaceView.class.getSimpleName();

    /*
     * Set up game arena here (basically the GL view port)
     */
    private int arenaWidth, arenaHeight;

    public void setArenaSize(int[] arenaSize) {
        arenaWidth = arenaSize[0];
        arenaHeight = arenaSize[1];
        Log.d(TAG, "Arena size set (width="+arenaWidth+"px, height="+arenaHeight+"px)");
    }

    public int getArenaWidth() { return arenaWidth; }

    public int getArenaHeight() { return arenaHeight; }


    /*
     * Initialize game level here.
     */
    private GameLevel mLevel;

    public void setUpGameLevel(SceneInfo sceneInfo) {
        mLevel = new GameLevel(sceneInfo);

        // Set arena size to size of GameLevel (must be done after GameLevel resources
        // are prepared).  Projection matrix is based on the arena size, so this ensures that
        // the game level will be nice and centered on the screen.
        //
        // However in the future, aspect ratio of the screen should not matter, so we need to make
        // sure that we have some sort of a camera that can handle any aspect ratio.
        setArenaSize(mLevel.getLevelSize());
    }


    /*
     * Initialize game UI here
     */
    // private GameUI mGameUI;


    /*
     * Update game state here
     */
    public void update() {
        mLevel.update();
    }

}
