package net.cloudhacking.androidgame2;

import android.os.Bundle;

import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.utils.GameTime;

/**
 * Created by Andrew on 1/17/2015.
 */
public class TDGame extends GameSkeleton {

    // config
    // TODO: create config/preferences system
    private static final float GAME_TIME_SCALE = 1f;


    @Override
    public void onGameInit(Bundle savedInstanceState) {
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
