package net.cloudhacking.androidgame2;

import android.content.Context;
import android.util.Log;

/**
 * Created by Andrew on 1/5/2015.
 */
public class AnimatedSprite extends Component {
    private static final String TAG = AnimatedSprite.class.getSimpleName();

    private SimpleRenderService mRenderService;
    private TileSet mTileSet;
    private int mResourceId;

    private boolean mResourcesPrepared=false;

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


    public AnimatedSprite(SimpleRenderService renderService, int resourceId) {
        mRenderService = renderService;
        mResourceId = resourceId;
        mTileSet = new TileSet(32, 32);
    }


    public void prepareResources(Context context) {
        mTileSet.loadTexture(context, mResourceId);
        mResourcesPrepared = true;
    }


    public void update() {
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


    public void draw() {
        if (!mResourcesPrepared) {
            throw new RuntimeException("GameLevel: resources not prepared");
        }
        QuadDrawer quadDrawer = mRenderService.getQuadDrawer();
        quadDrawer.beginDraw();
        mTileSet.drawTile(quadDrawer,
                animSequence[currentFrameIdx],
                mGridX*mTileSet.getTileWidth(),
                mGridY*mTileSet.getTileHeight(),
                mRotation,
                1.0f,
                1.0f
        );
        quadDrawer.endDraw();
    }

}
