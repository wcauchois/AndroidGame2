package net.cloudhacking.androidgame2.engine.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import net.cloudhacking.androidgame2.engine.element.Sprite;
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

    private static Context sContext;

    public static void setContext(Context context) {
        sContext = context;
    }


    /***********************************************************************************************
     * TEXTURE CACHE
     */

    private static HashMap<Asset, Texture> sTextureCache = new HashMap<Asset, Texture>();


    /**
     * Config
     */
    private static BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    static {
        sBitmapOptions.inScaled = false;
        sBitmapOptions.inDither = false;
        sBitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }

    private static Texture.TextureOptions sDefaultTextureOptions;
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
    public static Texture getTexture(Asset asset) {
        return getTexture(asset, sDefaultTextureOptions, true);
    }

    public static Texture getTexture(Asset asset, Texture.TextureOptions opts) {
        return getTexture(asset, opts, true);
    }

    public static Texture getTexture(Asset asset, Texture.TextureOptions opts, boolean checkOpts) {

        if (!sTextureCache.containsKey(asset)) {
            try {
                InputStream stream = sContext.getAssets().open(asset.getFileName());
                Bitmap bmp = BitmapFactory.decodeStream(stream, null, sBitmapOptions);

                Texture tex = new Texture(bmp, opts);
                sTextureCache.put(asset, tex);

                Log.d(TAG, "successfully loaded texture into cache: " + asset.getFileName());
                return tex;

            } catch(IOException e) {
                return null;
            }

        } else if (checkOpts) {
            Texture tex = sTextureCache.get(asset);

            // check if the stored textures options equal the requested options
            if (!tex.getOptions().equals(opts)) {
                tex = new Texture(tex.getBitmap(), opts);
                sTextureCache.put(asset, tex);
                return tex;

            } else {
                return tex;
            }

        } else {
            return sTextureCache.get(asset);
        }

    }

    public static void addTexture(Asset asset, Texture tex) {
        sTextureCache.put(asset, tex);
    }

    public static void reloadTextures() {
        for (Texture t : sTextureCache.values()) {
            t.reload();
        }
    }


    /***********************************************************************************************
     * SPRITE CACHE
     */

    /**
     * Cache sprite objects in order for l33t optimization, and so that we don't have to
     * regenerate the same vertex buffers for a hundred different creeps using the same sprite.
     */
    private static HashMap<SpriteAsset, Sprite> sSpriteCache
            = new HashMap<SpriteAsset, Sprite>();


    public static Sprite getSprite(SpriteAsset asset) {

        if (!sSpriteCache.containsKey(asset)) {
            Sprite sprite = new Sprite(asset);
            sSpriteCache.put(asset, sprite);
            return sprite;

        } else {
            return sSpriteCache.get(asset);
        }
    }


    /**********************************************************************************************/

    public static void clear() {
        for (Texture t : sTextureCache.values()) {
            t.delete();
        }
        sTextureCache.clear();
        sSpriteCache.clear();
    }

}
