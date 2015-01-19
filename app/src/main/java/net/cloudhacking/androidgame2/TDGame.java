package net.cloudhacking.androidgame2;

import android.os.Bundle;

import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.utils.GameTime;

/**
 * Created by Andrew on 1/17/2015.
 */
public class TDGame extends GameSkeleton {

    // config
    private static final float GAME_TIME_SCALE = 0.5f;


    @Override
    public void onCreateGame(Bundle savedInstanceState) {
        // can't make any opengl api calls here.

        GameTime.setTimeScale(GAME_TIME_SCALE);

        setSceneClass(TestScene.class);
    }

    @Override
    public void onPauseGame() {
        // pause game state here
    }

    @Override
    public void onResumeGame() {
        // resume game state here
    }

    @Override
    public void onDestroyGame() {
        // save game state here
    }

}
