package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.ui.element.MenuItem;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

import java.util.LinkedList;

/**
 * Created by research on 1/30/15.
 */
public class Engineer extends Unit implements Signal.Listener<MenuItem> {

    public enum Action {
        BUILD, MOVE, ATTACK
    }

    private LinkedList<Action> mActionQueue;


    public Engineer(SpriteAsset asset) {
        super(asset);
        mActionQueue = new LinkedList<Action>();
    }

    @Override
    public boolean onSignal(MenuItem item) {
        return true;
    }





}
