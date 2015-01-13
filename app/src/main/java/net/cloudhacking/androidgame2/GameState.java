package net.cloudhacking.androidgame2;

import android.util.Log;

/**
 * Created by wcauchois on 1/3/15.
 */
public class GameState {
    /*
     * This should be where most of the game logic is managed.  The game level should interact
     * with the game UI.
     */
    private static final String TAG = "GameState";

    private int mArenaWidth, mArenaHeight;

    private GameLevel mLevel;
    //private GameUI mGameUI;


    /*
     * Initialize game level here.
     */
    public void setUpGameLevel(SceneInfo sceneInfo) {
        mLevel = new GameLevel(sceneInfo);
    }


    /*
     * Initialize game UI here.
     */


    /*
     * Set up game arena here (basically the GL view port).
     */


    public void updateArenaSize() {
        // Set arena size to size of game level (must be done after GameLevel resources
        // are prepared).  Projection matrix is based on the arena size, so this ensures that
        // the game level will be nice and centered on the screen.
        //
        // However in the future, aspect ratio of the screen should not matter, so we need to make
        // sure that we have some sort of a camera that can handle any aspect ratio.
        int[] arenaSize = mLevel.getLevelSize();
        mArenaWidth = arenaSize[0];
        mArenaHeight = arenaSize[1];
        Log.d(TAG, "Arena size set (width="+ mArenaWidth +"px, height="+ mArenaHeight +"px)");
    }
    public int getArenaWidth()  { return mArenaWidth; }
    public int getArenaHeight() { return mArenaHeight; }


    /*
     * Update game state here
     */
    public void update() {
        mLevel.update();
    }

}
