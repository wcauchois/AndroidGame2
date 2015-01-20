package net.cloudhacking.androidgame2;

import android.os.Bundle;

import net.cloudhacking.androidgame2.engine.GameSkeleton;

/**
 * Created by Andrew on 1/17/2015.
 */
public class TDGame extends GameSkeleton {

    // TODO: create config/preferences system


    @Override
    public void onGameInit(Bundle savedInstanceState) {
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
