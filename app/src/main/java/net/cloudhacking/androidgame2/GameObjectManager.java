package net.cloudhacking.androidgame2;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcauchois on 12/31/14.
 */
public class GameObjectManager {
    private List<GameObject> gameObjects;

    private interface Callback {
        public void call(GameObject obj);
    }

    private void forEach(Callback callback) {
        List<GameObject> staticObjects = new ArrayList<GameObject>(gameObjects);
        for (GameObject obj : staticObjects) {
            callback.call(obj);
        }
    }

    public GameObjectManager() {
        gameObjects = new ArrayList<GameObject>();
    }

    public void addObject(GameObject obj) {
        gameObjects.add(obj);
    }

    public void removeObject(GameObject obj) {
        gameObjects.remove(obj);
    }

    public void draw(final Canvas canvas) {
        forEach(new Callback() {
            public void call(GameObject obj) {
                obj.draw(canvas);
            }
        });
    }

    public void simulate(final double timeDelta) {
        forEach(new Callback() {
            public void call(GameObject obj) {
                obj.simulate(timeDelta);
            }
        });
    }

    public void handleTouchEvent(final MotionEvent event) {
        forEach(new Callback() {
            public void call(GameObject obj) {
                obj.handleTouchEvent(event);
            }
        });
    }
}
