package net.cloudhacking.androidgame2.engine.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.cloudhacking.androidgame2.engine.gl.Texture;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Andrew on 1/17/2015.
 */
public class TextureCache extends Loggable {

    private static Context sContext;

    public static void setContext(Context context) {
        sContext = context;
    }

    private static HashMap<Asset, Texture> sCache = new HashMap<Asset, Texture>();


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
    static{
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
    public static Texture get(Asset asset) {
        return get(asset, sDefaultTextureOptions, false);
    }

    public static Texture get(Asset asset, Texture.TextureOptions opts, boolean checkOpts) {
        Texture tex;

        if (!sCache.containsKey(asset)) {
            try {
                InputStream stream = sContext.getAssets().open(asset.getFileName());
                Bitmap bmp = BitmapFactory.decodeStream(stream, null, sBitmapOptions);

                tex = new Texture(bmp, opts);
                sCache.put(asset, tex);
                return tex;

            } catch(IOException e) {
                return null;
            }

        } else if (checkOpts) {
            tex = sCache.get(asset);

            if (!tex.getOptions().equals(opts)) {
                tex = new Texture(tex.getBitmap(), opts);
                sCache.put(asset, tex);
                return tex;

            } else {
                return tex;
            }

        } else {
            return sCache.get(asset);
        }

    }


    public static void clear() {
        for (Texture t : sCache.values()) {
            t.delete();
        }
        sCache.clear();
    }

    public static void reload() {
        for (Texture t : sCache.values()) {
            t.reload();
        }
    }

}
