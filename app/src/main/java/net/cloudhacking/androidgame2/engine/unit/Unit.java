package net.cloudhacking.androidgame2.engine.unit;

import net.cloudhacking.androidgame2.engine.element.Animated;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

import java.util.LinkedHashSet;

/**
 * Created by Andrew on 1/28/2015.
 */
public abstract class Unit extends Animated {

    private boolean mAlive;
    private boolean mSelected;
    private boolean mSelectable;
    private boolean mTargetable;

    private LinkedHashSet<Unit> mTargetedBy;
    private Unit mTarget;

    public Unit(SpriteAsset asset) {
        super(asset);
        mAlive = true;
        mSelected = false;
        mSelectable = true;
        mTargetable = true;
        mTargetedBy = new LinkedHashSet<Unit>();
        mTarget = null;
    }

    protected void onDeath() {}

    protected void onRevival() {}

    protected void onSelect() {}

    public boolean isAlive() {
        return mAlive;
    }

    public void kill() {
        mAlive = false;
        onDeath();
    }

    public void revive() {
        mAlive = true;
        onRevival();
    }

    public void setSelectable(boolean bool) {
        mSelectable = bool;
        if (!bool) unSelect();
    }

    public boolean isSelectable() {
        return mSelectable;
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

    public void setTargetable(boolean bool) {
        mTargetable = bool;
        if (!bool) {
            for (Unit other : mTargetedBy) {
                other.clearTarget();
            }
        }
    }

    public boolean isTargetable() {
        return mTargetable;
    }

    public LinkedHashSet<Unit> getTargetedBy() {
        return mTargetedBy;
    }

    public boolean isTargetedBy(Unit other) {
        return mTargetedBy.contains(other);
    }

    public void target(Unit other) {
        clearTarget();
        if (!other.isTargetable()) return;
        other.getTargetedBy().add(this);
        mTarget = other;
    }

    public void clearTarget() {
        if (mTarget != null) {
            mTarget.getTargetedBy().remove(this);
            mTarget = null;
        }
    }

    public void setHoverColor(boolean bool) {
        // triggered on click-down, set to false on click-up/cancel
        // when unit is "hovered" on by a touch event, set highlight around unit or
        // something.
    }

}
