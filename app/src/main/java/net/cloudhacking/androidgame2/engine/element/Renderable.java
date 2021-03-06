package net.cloudhacking.androidgame2.engine.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.GLColor;
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
    private float mDefaultRotation;
    private float mRotationSpeed;
    private boolean mRotatable;
    private static final float TWO_PI = 2*(float)Math.PI;

    private GLColor mColorM;
    private GLColor mColorA;

    private float[] mModelMatrix;


    public Renderable(int x, int y, float width, float height) {
        mOrigin = new PointF();
        mPos = new PointF(x, y);  // center point
        mWidth = width;           // width and height are unscaled,
        mHeight = height;         // use getWidth(), getHeight() to get scaled width/height

        mScale = new PointF(1, 1);
        mScalable = false;

        mVelocity = new Vec2();
        mAcceleration = new Vec2();

        mRotation = 0f;
        mDefaultRotation = 0f;
        mRotationSpeed = 0f;
        mRotatable = false;

        mColorM = GLColor.WHITE;
        mColorA = GLColor.TRANSPARENT;

        mModelMatrix = new float[16];
    }

    public PointF getPos() {
        return mPos;
    }

    public void setPos(PointF pos) {
        mPos = pos;
    }

    public void setPos(float x, float y) {
        mPos.set(x, y);
    }

    public void movePos(Vec2 dir) {
        mPos.move(dir);
    }

    public void movePos(float x, float y) {
        movePos(new Vec2(x, y));
    }

    public float getActualWidth() {
        return mWidth;
    }

    public float getWidth() {
        return mWidth * mScale.y;
    }

    public void setActualWidth(float width) {
        mWidth = width;
    }

    public void setWidth(float width) {
        setScaleX( width/mWidth );
    }

    public float getActualHeight() {
        return mHeight;
    }

    public float getHeight() {
        return mHeight * mScale.x;
    }

    public void setActualHeight(float height) {
        mHeight = height;
    }

    public void setHeight(float height) {
        setScaleY( height/mHeight );
    }

    public RectF getRect() {
        return new RectF(mPos.x, mPos.y, mPos.x + mWidth, mPos.y + mHeight);
    }

    public RectF getScaledRect() {
        return new RectF(mPos.x, mPos.y, mPos.x + getWidth(), mPos.y + getHeight());
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

    public void setVelocity(float x, float y) {
        mVelocity.set(x, y);
    }

    public Vec2 getAcceleration() {
        return mAcceleration;
    }

    public void setAcceleration(Vec2 acceleration) {
        mAcceleration = acceleration;
    }

    public void setAcceleration(float x, float y) {
        mAcceleration.set(x, y);
    }

    public float getRotation() {
        return mRotation - mDefaultRotation;
    }

    public void setRotation(float rotation) {
        mRotation = rotation + mDefaultRotation;
        mRotatable = true;
    }

    public void setDefaultRotation(float rotation) {
        mDefaultRotation = rotation;
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

    public GLColor getColorM() {
        return mColorM;
    }

    public void setColorM(GLColor c) {
        mColorM = c;
    }

    public void setAlpha(float a) {
        GLColor.setAlpha(mColorM, a);
    }

    public GLColor getColorA() {
        return mColorA;
    }

    public void setColorA(GLColor c) {
        mColorA = c;
    }

    public float[] getModelMatrix() {
        return mModelMatrix;
    }

    public void hide() {
        setVisibility(false);
        setInactive();
    }

    public void show() {
        setVisibility(true);
        setActive();
    }

    public boolean containsPt(PointF p) {
        float x=mPos.x, y = mPos.y, w= getWidth(), h= getHeight();
        return p.x >= x && p.x <= x+w && p.y >= y && p.y <= y+h;
    }


    @Override
    public void draw(BasicGLScript gls) {
        gls.setLighting(mColorM, mColorA);
        gls.uModel.setValueM4(mModelMatrix);
    }

    @Override
    public void update() {
        updateMotion();
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
        mRotation %= TWO_PI;
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
