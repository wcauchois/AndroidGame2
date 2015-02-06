package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.element.Animated;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

import java.util.LinkedHashSet;

/**
 * Created by Andrew on 1/28/2015.
 */
public abstract class Unit extends Animated {

    private static final float DEFAULT_HP = 1.0f;
    private static final float DEFAULT_MOVESPEED = 50;

    private boolean mAlive;
    private Grid.Cell mLocation;

    private float mHP;
    private float mMoveSpeed;

    private LinkedHashSet<Unit> mTargetedBy;
    private Unit mTarget;

    public Unit(SpriteAsset asset) {
        super(asset);
        mAlive = true;
        mLocation = null;
        mHP = DEFAULT_HP;
        mMoveSpeed = DEFAULT_MOVESPEED;
        mTargetedBy = new LinkedHashSet<Unit>();
        mTarget = null;
    }

    public boolean isAlive() {
        return mAlive;
    }

    public Grid.Cell getLocation() {
        return mLocation;
    }

    public void setLocation(Grid.Cell loc) {
        mLocation = loc;
    }

    public void setToCell(Grid.Cell loc) {
        mLocation = loc;
        setPos(loc.getCenter());
    }

    public void kill() {
        mAlive = false;
    }

    public void revive() {
        mAlive = true;
    }

    public float getHP() {
        return mHP;
    }

    public void setHP(float hp) {
        mHP = hp;
    }

    public void doDamage(float dmg) {
        mHP = Math.max(mHP-dmg, 0);
        if (mHP <= 0) {
            kill();
            onDeath();
        }
    }

    public void onDeath() {

    }

    public float getMoveSpeed() {
        return mMoveSpeed;
    }

    public void setMoveSpeed(float speed) {
        mMoveSpeed = speed;
    }

    public LinkedHashSet<Unit> getTargetedBy() {
        return mTargetedBy;
    }

    public void target(Unit other) {
        clearTarget();
        other.getTargetedBy().add(this);
        mTarget = other;
    }

    public void clearTarget() {
        if (mTarget != null) {
            mTarget.getTargetedBy().remove(this);
            mTarget = null;
        }
    }

}
