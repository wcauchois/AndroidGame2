package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

/**
 * Created by Andrew on 2/1/2015.
 */
public class SelectableUnit extends Unit {

    private boolean mSelected;

    public SelectableUnit(SpriteAsset asset) {
        super(asset);
        mSelected = false;
    }

    public void setHoverColor(boolean bool) {
        // triggered on click-down, set to false on click-up/cancel
        // when unit is "hovered" on by a touch event, set highlight around unit or
        // something.
    }

    public void select() {
        mSelected = true;
        onSelect();
    }

    public void unSelect() {
        mSelected = false;
    }

    public boolean isSelected() {
        return mSelected;
    }

    protected void onSelect() {}

}
