package net.cloudhacking.androidgame2;

import android.content.Context;
import android.util.Log;

/**
 * This represents a texture containing many uniformly-sized tiles, and
 * provides the capability to render a single tile from that texture.
 * Created by wcauchois on 1/4/15.
 */
public class TileSet {
    private static final String TAG = TileSet.class.getSimpleName();
    private int mTileWidth;
    private int mTileHeight;
    private int mSetWidth;
    private int mSetHeight;
    public int mTextureID; // XXX donot commit

    public TileSet(int tileWidth, int tileHeight) {
        mTileWidth = tileWidth;
        mTileHeight = tileHeight;
    }

    public float getTileWidth() {
        return (float) mTileWidth;
    }

    public float getTileHeight() {
        return (float) mTileHeight;
    }

    public void loadTexture(Context context, int resourceId) {
        Util.BitmapInfo info = new Util.BitmapInfo();
        mTextureID = Util.loadTexture(context, resourceId, info);

        mSetWidth = info.getWidth() / mTileWidth;
        mSetHeight = info.getHeight() / mTileHeight;

        // this prints 960x960
        Log.d(TAG, "Tileset bitmap: width="+info.getWidth()+"px, height="+info.getHeight()+"px");
        Log.d(TAG, "Tileset tilesize: width="+mTileWidth+"px, height="+mTileHeight+"px");

        Log.i(TAG, "Loaded tileset of width " + mSetWidth + " and height " + mSetHeight);
    }

    // Caller is responsible for calling beginDraw and endDraw!
    public void drawTile(QuadDrawer quadDrawer, int tileIndex, float x, float y, float sx, float sy) {
        // TODO(wcauchois): Some of these should probably be precomputed.
        float tw = 1.0f / (float) mSetWidth;
        float th = 1.0f / (float) mSetHeight;
        float tx = ((float) (tileIndex % mSetWidth)) * tw;
        float ty = (float) Math.floor((float) tileIndex /
                (float) mSetWidth) * th;
        // FIXME(wcauchois): I was seeing some weird float issues here, like numbers like 0.900004

        quadDrawer.draw(mTextureID, x, y, (float) mTileWidth * sx, (float) mTileHeight * sy,
                tx, ty, tw, th);
    }
}
