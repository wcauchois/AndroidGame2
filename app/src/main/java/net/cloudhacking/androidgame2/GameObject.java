package net.cloudhacking.androidgame2;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by wcauchois on 12/31/14.
 */
public abstract class GameObject {
    private GameServices services;

    protected GameServices getServices() {
        return services;
    }

    public GameObject(GameServices services) {
        this.services = services;
    }

    public void draw(Canvas canvas) {}
    public void simulate(double timeDelta) {}
    public void handleTouchEvent(MotionEvent event) {}
}
