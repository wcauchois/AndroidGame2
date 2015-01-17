package net.cloudhacking.androidgame2.engine.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Andrew on 1/16/2015.
 */
public class BitmapCache extends Loggable {

    private static Context sContext;

    public static void setContext(Context context) {
        sContext = context;
    }



    private static String DEFAULT_LAYER_NAME = "default";

    private static class Layer extends HashMap<String, Bitmap> {
        @Override
        public void clear() {
            for (Bitmap bmp : values()) {
                bmp.recycle();
            }
            super.clear();
        }
    }

    private static HashMap<String, Layer> sLayerCache = new HashMap<String, Layer>();


    private static BitmapFactory.Options sOptions = new BitmapFactory.Options();
    static {
        sOptions.inDither = false;
    }



    public static Bitmap get(String assetName) {
        return get(DEFAULT_LAYER_NAME, assetName);
    }

    public static Bitmap get(String layerName, String assetName) {

        Layer layer;
        if (!sLayerCache.containsKey(layerName)) {
            layer = new Layer();
            sLayerCache.put(layerName, layer);
        } else {
            layer = sLayerCache.get(layerName);
        }

        if (layer.containsKey(assetName)) {
            return layer.get(assetName);
        } else {
            try {
                InputStream stream = sContext.getAssets().open(assetName);
                Bitmap bmp = BitmapFactory.decodeStream(stream, null, sOptions);
                layer.put(assetName, bmp);
                return bmp;
            } catch(IOException e) {
                Log.e(TAG, "error loading asset: " + assetName);
                return null;
            }
        }
    }



    public static void clear(String layerName) {
        if (sLayerCache.containsKey(layerName)) {
            sLayerCache.get(layerName).clear();
            sLayerCache.remove(layerName);
        }
    }

    public static void clear() {
        for (Layer l : sLayerCache.values()) {
            l.clear();
        }
        sLayerCache.clear();
    }

}
