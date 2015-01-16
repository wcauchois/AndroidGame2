package net.cloudhacking.androidgame2;

import android.util.Log;

import net.cloudhacking.androidgame2.engine.SceneInfo;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by wcauchois on 1/3/15.
 */
public class GameState extends Loggable {
    /*
     * This should be where most of the game logic is managed.  The game level should interact
     * with the game UI.
     */

    private float mArenaWidth, mArenaHeight;

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
        Vec2 arenaSize = mLevel.getLevelSize();
        mArenaWidth = arenaSize.getX();
        mArenaHeight = arenaSize.getY();
        i("Arena size set (width="+ mArenaWidth +"px, height="+ mArenaHeight +"px)");
    }
    public float getArenaWidth()  { return mArenaWidth; }
    public float getArenaHeight() { return mArenaHeight; }


    /*
     * Update game state here
     */
    public void update() {
        mLevel.update();
    }

}
