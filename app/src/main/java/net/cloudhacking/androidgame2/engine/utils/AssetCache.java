package net.cloudhacking.androidgame2.engine.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.element.Sprite;
import net.cloudhacking.androidgame2.engine.gl.PreRenderedTexture;
import net.cloudhacking.androidgame2.engine.gl.Texture;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Andrew on 1/17/2015.
 */
public class AssetCache extends Loggable {

    /**
     * Use this cache in order to get Textures and Sprites.  These are cached because the vertex
     * buffers don't really change and we wouldn't want to store multiple copies of any bitmaps.
     */

    private static AssetCache sInstance;
    public static AssetCache getInstance() {
        if (sInstance == null) {
            sInstance = new AssetCache(GameSkeleton.getInstance());
        }
        return sInstance;
    }
    public static void clearInstance() {
        sInstance = null;
    }


    /***********************************************************************************************
     * TEXTURE CACHE
     */

    private Context mContext;

    private HashMap<Asset, Texture> mTextureCache;
    private HashMap<Integer, PreRenderedTexture> mPreRenderedCache;


    private AssetCache(Context context) {
        mContext = context;
        mTextureCache = new HashMap<Asset, Texture>();
        mPreRenderedCache = new HashMap<Integer, PreRenderedTexture>();
        PreRenderedTexture.resetID();

        d("initialized asset cache");
    }


    /**
     * Config
     */
    private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    static {
        sBitmapOptions.inScaled = false;
        sBitmapOptions.inDither = false;
        sBitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }

    private static final Texture.TextureOptions sDefaultTextureOptions;
    static {
        sDefaultTextureOptions = new Texture.TextureOptions();
        sDefaultTextureOptions.minMode   = Texture.FilterType.NEAREST;
        sDefaultTextureOptions.magMode   = Texture.FilterType.NEAREST;
        sDefaultTextureOptions.wrapModeS = Texture.WrapType.CLAMP;
        sDefaultTextureOptions.wrapModeT = Texture.WrapType.CLAMP;
    }


    /**
     * Retrieves the texture mapped to the given asset.  If the asset has not been previously
     * loaded or the texture options are different, this will create a new texture and put it
     * into the cache.
     */
    public Texture getTexture(Asset asset) {
        return getTexture(asset, sDefaultTextureOptions, true);
    }

    public Texture getTexture(Asset asset, Texture.TextureOptions opts) {
        return getTexture(asset, opts, true);
    }

    public Texture getTexture(Asset asset, Texture.TextureOptions opts, boolean checkOpts) {

        if (!mTextureCache.containsKey(asset)) {
            try {
                InputStream stream = mContext.getAssets().open(asset.getFileName());
                Bitmap bmp = BitmapFactory.decodeStream(stream, null, sBitmapOptions);

                Texture tex = new Texture(bmp, opts);
                tex.setAsset(asset);
                mTextureCache.put(asset, tex);

                d("successfully loaded texture into cache: " + asset.getFileName());
                return tex;

            } catch(IOException e) {
                return null;
            }

        } else if (checkOpts) {
            Texture tex = mTextureCache.get(asset);

            // check if the stored textures options equal the requested options
            if (!tex.getOptions().equals(opts)) {
                tex = new Texture(tex.getBitmap(), opts);
                tex.setAsset(asset);
                mTextureCache.put(asset, tex);
                return tex;

            } else {
                return tex;
            }

        } else {
            return mTextureCache.get(asset);
        }

    }

    public void addPreRendered(PreRenderedTexture pre) {
        d("adding new pre-rendered texture to cache, ID: " + pre.getId());
        mPreRenderedCache.put(pre.getId(), pre);
    }

    public void removePreRendered(int id) {
        mPreRenderedCache.remove(id);
    }

    public PreRenderedTexture getPreRendered(int id) {
        return mPreRenderedCache.get(id);
    }

    public void reloadTextures() {
        for (Texture t : mTextureCache.values()) {
            t.reload();
            d("reloading texture: " + t.getAsset().getFileName());
        }
        for (PreRenderedTexture p : mPreRenderedCache.values()) {
            p.reload();
            d("re-rendering texture, ID: "+p.getId());
        }
    }


    /***********************************************************************************************
     * SPRITE CACHE
     */

    /**
     * Cache sprite objects in order for l33t optimization, and so that we don't have to
     * regenerate the same vertex buffers for a hundred different creeps using the same sprite.
     */
    private HashMap<SpriteAsset, Sprite> sSpriteCache
            = new HashMap<SpriteAsset, Sprite>();


    public Sprite getSprite(SpriteAsset asset) {

        if (!sSpriteCache.containsKey(asset)) {
            Sprite sprite = new Sprite(asset);
            sSpriteCache.put(asset, sprite);
            return sprite;

        } else {
            return sSpriteCache.get(asset);
        }
    }


    /**********************************************************************************************/

    public void clear() {
        d("clearing asset cache");
        for (Texture t : mTextureCache.values()) {
            t.delete();
        }
        for (PreRenderedTexture p : mPreRenderedCache.values()) {
            p.delete();
        }
        mTextureCache.clear();
        mPreRenderedCache.clear();
        sSpriteCache.clear();
    }

}
