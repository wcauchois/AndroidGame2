package net.cloudhacking.androidgame2.engine;

import net.cloudhacking.androidgame2.engine.utils.GameTime;
import net.cloudhacking.androidgame2.engine.utils.MatrixUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by Andrew on 1/15/2015.
 */
public class Renderable extends Entity {

    private PointF mPos;
    private float mWidth;
    private float mHeight;

    private PointF mOrigin;
    private PointF mScale;

    private Vec2 mVelocity;
    private Vec2 mAcceleration;
    private float mRotation;
    private float mRotationSpeed;

    private float[] mModelMatrix;


    public Renderable(int x, int y, float width, float height) {
        mPos = new PointF(x, y);
        mWidth = width;
        mHeight = height;

        mOrigin = new PointF();
        mScale = new PointF(1, 1);

        mVelocity = new Vec2();
        mAcceleration = new Vec2();
        mRotation = 0f;
        mRotationSpeed = 0f;

        mModelMatrix = new float[16];
    }

    @Override
    public void draw() {
        updateMotion();
    }

    @Override
    public void update() {
        updateMatrix();
    }

    @Override
    public boolean isOnScreen() {
        return true;
    }


    public void updateMotion() {
        float delta = GameTime.getFrameDelta();

        mVelocity = mVelocity.add(mAcceleration.scale(delta));
        mPos.move(mVelocity.scale(delta));

        mRotation += mRotationSpeed * delta;
    }


    public void updateMatrix() {
        MatrixUtils.setIdentity(mModelMatrix);
        MatrixUtils.translate2D(mModelMatrix, mOrigin.x + mPos.x, mOrigin.y + mPos.y);
        if (mRotation != 0f) {
            MatrixUtils.rotate2D(mModelMatrix, mRotation);
        }
        if (mScale.x != 1f || mScale.y != 1f) {
            MatrixUtils.scale(mModelMatrix, mScale.x, mScale.y);
        }
        MatrixUtils.translate2D(mModelMatrix, -mOrigin.x, -mOrigin.y);
    }


    public boolean overlapsPoint(PointF p) {
        return p.x >= mPos.x && p.x <= mPos.x + mScale.x * mWidth &&
               p.y >= mPos.y && p.y <= mPos.y + mScale.y * mHeight;
    }


}
