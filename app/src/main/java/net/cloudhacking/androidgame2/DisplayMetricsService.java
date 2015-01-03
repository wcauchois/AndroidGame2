package net.cloudhacking.androidgame2;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by wcauchois on 12/31/14.
 */
public class DisplayMetricsService {
    private DisplayMetrics metrics;

    public DisplayMetricsService(Context context) {
        metrics = context.getResources().getDisplayMetrics();
    }

    public int getWidthPixels() {
        return metrics.widthPixels;
    }

    public int getHeightPixels() {
        return metrics.heightPixels;
    }

    public boolean pointInScreen(Vector2D vector) {
        return vector.getX() >= 0 && vector.getY() >= 0 &&
                vector.getX() <= getWidthPixels() &&
                vector.getY() <= getHeightPixels();
    }
}
