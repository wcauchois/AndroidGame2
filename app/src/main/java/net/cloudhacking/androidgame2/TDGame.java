package net.cloudhacking.androidgame2;

import android.os.Bundle;

import net.cloudhacking.androidgame2.engine.Camera;
import net.cloudhacking.androidgame2.engine.GameSkeleton;

/**
 * Created by Andrew on 1/17/2015.
 */
public class TDGame extends GameSkeleton {

    // TODO: create config/preferences system
    private static final float SCROLL_SPEED = 1.1f;


    @Override
    public void onInitGame(Bundle savedInstanceState) {
        Camera.setScrollSpeed(SCROLL_SPEED);

        setScene( new TestScene() );
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
