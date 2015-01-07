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

    public int getTileWidth() {
        return mTileWidth;
    }

    public int getTileHeight() {
        return mTileHeight;
    }

    public void loadTexture(Context context, int resourceId) {
        Util.BitmapInfo info = new Util.BitmapInfo();
        mTextureID = Util.loadTexture(context, resourceId, info);

        mSetWidth = info.getWidth() / mTileWidth;
        mSetHeight = info.getHeight() / mTileHeight;

        /*Log.d(TAG, "Tileset bitmap: width="+info.getWidth()+"px, height="+info.getHeight()+"px");
        Log.d(TAG, "Tileset tilesize: width="+mTileWidth+"px, height="+mTileHeight+"px");*/

        Log.i(TAG, "Loaded tileset "+mTextureID+" (width=" + mSetWidth + ", height=" + mSetHeight+")");
    }

    public void prepareTexture(QuadDrawer quadDrawer) {
        quadDrawer.prepareTexture(mTextureID);
    }

    // Caller is responsible for calling beginDraw and endDraw!
    public void drawTile(QuadDrawer quadDrawer, int tileIndex, float x, float y, float rot, float sx, float sy) {
        // TODO(wcauchois): Some of these should probably be precomputed.
        float tw = 1.0f / (float) mSetWidth;
        float th = 1.0f / (float) mSetHeight;
        float tx = (tileIndex % mSetWidth) * tw;
        float ty = (float) Math.floor((float) tileIndex /
                (float) mSetWidth) * th;

        /*Log.d(TAG, "quadDrawer input: x="+x+", y="+y
                +", w="+((float) mTileWidth * sx)+", h="+((float) mTileHeight * sy)
                +", tx="+tx+", ty="+ty+", tw="+tw+", th="+th
        );*/

        quadDrawer.draw(x, y, rot, (float) mTileWidth * sx, (float) mTileHeight * sy,
                tx, ty, tw, th);
    }
}
