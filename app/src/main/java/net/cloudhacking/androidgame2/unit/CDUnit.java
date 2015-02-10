package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.unit.Unit;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

/**
 * Created by Andrew on 2/9/2015.
 */
public class CDUnit extends Unit {

    private static final float DEFAULT_HP = 1.0f;
    private static final float DEFAULT_MOVESPEED = 50;

    private float mHP;
    private float mMoveSpeed;

    public CDUnit(SpriteAsset asset) {
        super(asset);
        mHP = DEFAULT_HP;
        mMoveSpeed = DEFAULT_MOVESPEED;
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
        }
    }

    public float getMoveSpeed() {
        return mMoveSpeed;
    }

    public void setMoveSpeed(float speed) {
        mMoveSpeed = speed;
    }

    @Override
    public void onDeath() {}

    @Override
    public void onRevival() {}

    @Override
    public void onSelect() {}

}
