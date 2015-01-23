package net.cloudhacking.androidgame2.engine.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.utils.GameTime;
import net.cloudhacking.androidgame2.engine.utils.MatrixUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by Andrew on 1/15/2015.
 */
public class Renderable extends Entity {

    /**
     * This class represents any renderable entity.  It controls the entire model matrix and
     * movement via velocity and acceleration vectors.  It also contains Vec4's representing the
     * two color uniforms.
     */

    private PointF mOrigin;
    private PointF mPos;
    private float mWidth;
    private float mHeight;

    private PointF mScale;
    private boolean mScalable;

    private Vec2 mVelocity;
    private Vec2 mAcceleration;

    private float mRotation;
    private float mRotationSpeed;
    private boolean mRotatable;

    private float[] mColorM;
    private float[] mColorA;

    private float[] mModelMatrix;


    public Renderable(int x, int y, float width, float height) {
        mOrigin = new PointF();
        mPos = new PointF(x, y);  // center point
        mWidth = width;           // width and height are unscaled,
        mHeight = height;         // use getScaledWidth(), getScaledHeight() to get scaled width/height

        mScale = new PointF(1, 1);
        mScalable = false;

        mVelocity = new Vec2();
        mAcceleration = new Vec2();

        mRotation = 0f;
        mRotationSpeed = 0f;
        mRotatable = false;

        mColorM = new float[] {1, 1, 1, 1};
        mColorA = new float[] {0, 0, 0, 0};

        mModelMatrix = new float[16];
    }

    public PointF getPos() {
        return mPos;
    }

    public void setPos(PointF pos) {
        mPos = pos;
    }

    public void setPos(int x, int y) {
        mPos.set(x, y);
    }

    public void movePos(Vec2 dir) {
        mPos.move(dir);
    }

    public void movePos(float x, float y) {
        movePos( new Vec2(x, y) );
    }

    public float getWidth() {
        return mWidth;
    }

    public float getScaledWidth() {
        return mWidth * mScale.y;
    }

    public void setWidth(float width) {
        mWidth = width;
    }

    public float getHeight() {
        return mHeight;
    }

    public float getScaledHeight() {
        return mHeight * mScale.x;
    }

    public void setHeight(float height) {
        mHeight = height;
    }

    public RectF getRect() {
        return new RectF(mPos.x, mPos.y, mPos.x + getScaledWidth(), mPos.y + getScaledHeight());
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }

    public PointF getScale() {
        return mScale;
    }

    public void setScale(float scale) {
        mScale = new PointF(scale, scale);
        mScalable = true;
    }

    public void setScale(PointF scale) {
        mScale = scale;
        mScalable = true;
    }

    public void setScaleX(float scaleX) {
        mScale.x = scaleX;
        mScalable = true;
    }

    public void setScaleY(float scaleY) {
        mScale.y = scaleY;
        mScalable = true;
    }

    public void setScalable(boolean bool) {
        mScalable = bool;
    }

    public Vec2 getVelocity() {
        return mVelocity;
    }

    public void setVelocity(Vec2 velocity) {
        mVelocity = velocity;
    }

    public Vec2 getAcceleration() {
        return mAcceleration;
    }

    public void setAcceleration(Vec2 acceleration) {
        mAcceleration = acceleration;
    }

    public float getRotation() {
        return mRotation;
    }

    public void setRotation(float rotation) {
        mRotation = rotation;
        mRotatable = true;
    }

    public float getRotationSpeed() {
        return mRotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        mRotationSpeed = rotationSpeed;
        mRotatable = true;
    }

    public void setRotatable(boolean bool) {
        mRotatable = bool;
    }

    public float[] getColorM() {
        return mColorM.clone();
    }

    public void setColorM(float[] vm) {
        mColorM = vm.clone();
    }

    public float[] getColorA() {
        return mColorA.clone();
    }

    public void setColorA(float[] va) {
        mColorA = va.clone();
    }

    public float[] getModelMatrix() {
        return mModelMatrix;
    }


    @Override
    public void draw(BasicGLScript gls) {
        updateMotion();
    }

    @Override
    public void update() {
        updateMatrix();
    }

    @Override
    public boolean isOnScreen() {
        // TODO: check if bounding box intersects camera
        return true;
    }


    private void updateMotion() {
        float delta = GameTime.getFrameDelta();  // in seconds

        mVelocity.increment(mAcceleration.scale(delta));
        mPos.move(mVelocity.scale(delta));

        mRotation += mRotationSpeed * delta;
    }


    private void updateMatrix() {
        MatrixUtils.setIdentity(mModelMatrix);
        if (!mRotatable && !mScalable) {
            MatrixUtils.translate2D(mModelMatrix, mPos.x, mPos.y);
            return;
        }
        MatrixUtils.translate2D(mModelMatrix, mOrigin.x + mPos.x, mOrigin.y + mPos.y);
        if (mRotatable) {
            MatrixUtils.rotate2D(mModelMatrix, mRotation);
        }
        if (mScalable) {
            MatrixUtils.scale(mModelMatrix, mScale.x, mScale.y);
        }
        MatrixUtils.translate2D(mModelMatrix, -mOrigin.x, -mOrigin.y);
    }


}
