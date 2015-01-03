package net.cloudhacking.androidgame2;

import android.view.MotionEvent;

/**
 * Created by wcauchois on 12/31/14.
 */
public class TurretCreator extends GameObject {
    public TurretCreator(GameServices services) {
        super(services);
    }

    private Vector2D touchPosition = new Vector2D(0, 0);
    private boolean pressed = false;
    private double lastCreate = 0.0;
    private double currentTime = 0.0;
    private static final double CREATE_INTERVAL = 5.0;

    @Override
    public void simulate(double timeDelta) {
        currentTime += timeDelta;
        if (pressed && (currentTime - lastCreate) > CREATE_INTERVAL) {
            createTurret();
            lastCreate = currentTime;
        }
    }

    private void createTurret() {
        Vector2D newPosition = new Vector2D(touchPosition.getX(), touchPosition.getY());
        getServices().getManager().addObject(
                new Turret(getServices(), newPosition)
        );
    }

    @Override
    public void handleTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pressed = true;
            lastCreate = 0.0;
            touchPosition = new Vector2D(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            pressed = false;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            touchPosition = new Vector2D(event.getX(), event.getY());
        }
    }
}
