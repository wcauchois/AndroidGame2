package net.cloudhacking.androidgame2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wcauchois on 12/31/14.
 */
public class ResourceManager {
    private Map<Integer, Bitmap> cachedBitmaps;
    private Resources resources;
    private boolean initialized = false;

    public ResourceManager(Resources resources) {
        this.cachedBitmaps = new HashMap<Integer, Bitmap>();
        this.resources = resources;
    }

    public void initialize() {
        if (initialized) return;

        for (int i = 0; i < CACHED_BITMAP_IDS.length; i++) {
            this.cachedBitmaps.put(CACHED_BITMAP_IDS[i],
                    BitmapFactory.decodeResource(resources, CACHED_BITMAP_IDS[i]));
        }

        initialized = true;
    }

    public Bitmap getBitmapResource(int id) {
        return cachedBitmaps.get(id);
    }

    public static int[] CACHED_BITMAP_IDS = new int[] {
            R.drawable.droid_1
    };
}
