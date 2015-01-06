package net.cloudhacking.androidgame2;

import android.content.Context;
import android.util.Log;

/**
 * Created by Andrew on 1/5/2015.
 */
public class AnimatedSprite extends Component implements Renderable {
    private static final String TAG = AnimatedSprite.class.getSimpleName();

    private int mResourceId;
    private TileSet mTileSet;

    // animation vars...
    private final long ANIMATION_FREQUENCY=(long)(0.75*1e9);  // in nanoseconds
    private long sysTimeLastNsec=-1;
    private long threshold=0;

    private int[] animSequence = new int[] {0, 1};  // alternate between first and second tile
    private int currentFrameIdx = 0;

    private int mGridX=0, mGridY=0;
    private float mRotation=60;

    public void setGridPos(int x, int y) {
        mGridX = x;
        mGridY = y;
    }


    public AnimatedSprite(int resourceId) {
        //super();
        mResourceId = resourceId;
        mTileSet = new TileSet(32, 32);
    }

    // Automatically called in onSurfaceCreated() in GameSurfaceRenderer
    public void prepareResources(Context context) {
        mTileSet.loadTexture(context, mResourceId);

        mResourcesPrepared = true;
    }


    public void update() {
        checkResourcesPrepared(TAG);

        // on first frame...
        if (sysTimeLastNsec == -1) {
            sysTimeLastNsec = System.nanoTime();
            return;
        }

        // increment total time passed
        long nowNsec = System.nanoTime();
        threshold += (nowNsec - sysTimeLastNsec);
        sysTimeLastNsec = nowNsec;

        // if total time passed is greater than threshold, increment frame index.
        if (threshold>=ANIMATION_FREQUENCY) {
            currentFrameIdx = (currentFrameIdx+1) % animSequence.length;
            threshold -= ANIMATION_FREQUENCY;
            Log.d(TAG, "frame switched to index: "+currentFrameIdx);
            Log.d(TAG, "current rotation: "+mRotation);
        }

        // increment rotation for debug
        mRotation = (mRotation+5) % 360;
    }


    public void draw(QuadDrawer quadDrawer) {
        checkResourcesPrepared(TAG);

        mTileSet.drawTile(quadDrawer,
                animSequence[currentFrameIdx],
                mGridX*mTileSet.getTileWidth(),
                mGridY*mTileSet.getTileHeight(),
                mRotation,
                1.0f,
                1.0f
        );
    }

}
