package net.cloudhacking.androidgame2;

import android.content.Context;
import android.util.Log;

/**
 * This represents a texture containing many uniformly-sized tiles, and
 * provides the capability to render a single tile from that texture.
 * Created by wcauchois on 1/4/15.
 */
public class TileSet extends Component {
    private static final String TAG = TileSet.class.getSimpleName();

    private int mTileWidth;
    private int mTileHeight;
    private int mSetWidth;
    private int mSetHeight;

    public int mResourceId;
    public TextureUtils.TextureInfo mTextureInfo;


    public TileSet(int resourceId, int tileWidth, int tileHeight) {
        mResourceId = resourceId;
        mTileWidth = tileWidth;
        mTileHeight = tileHeight;
    }

    public int getTileWidth() {
        return mTileWidth;
    }

    public int getTileHeight() {
        return mTileHeight;
    }


    @Override
    public void prepareResources(Context context) {
        loadTexture(context, mResourceId);
        mResourcesPrepared = true;
    }

    public void loadTexture(Context context, int resourceId) {
        mTextureInfo = TextureUtils.loadTexture(context, resourceId);

        mSetWidth = mTextureInfo.getBitmapWidth() / mTileWidth;
        mSetHeight = mTextureInfo.getBitmapHeight() / mTileHeight;

        Log.i(TAG, "Loaded tileset (textureUnit="+ mTextureInfo.getGLTextureUnit()+", textureHandle="+ mTextureInfo.getGLTextureHandle()+
                ", width=" + mSetWidth + ", height=" + mSetHeight+")");
    }


    public void prepareTexture(QuadDrawer quadDrawer) {
        quadDrawer.prepareTexture(mTextureInfo.getGLTextureUnit());
    }

    public void drawTile(QuadDrawer quadDrawer, int tileIndex, float x, float y, float rot, float sx, float sy) {
        // TODO(wcauchois): Some of these should probably be precomputed.
        float tw = 1.0f / (float) mSetWidth;
        float th = 1.0f / (float) mSetHeight;
        float tx = (tileIndex % mSetWidth) * tw;
        float ty = (float) Math.floor((float) tileIndex /
                (float) mSetWidth) * th;

        quadDrawer.draw(x, y, rot, (float) mTileWidth * sx, (float) mTileHeight * sy,
                tx, ty, tw, th);
    }
}
