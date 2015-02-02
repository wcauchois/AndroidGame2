package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

/**
 * Created by Andrew on 2/1/2015.
 */
public class SelectableUnit extends ControllableUnit {

    private boolean mSelected;

    public SelectableUnit(SpriteAsset asset) {
        super(asset);
        mSelected = false;
    }

    public void select() {
        mSelected = true;
    }

    public void unSelect() {
        mSelected = true;
    }

    public boolean isSelected() {
        return mSelected;
    }

}
