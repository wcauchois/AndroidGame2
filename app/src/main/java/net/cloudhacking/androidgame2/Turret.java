package net.cloudhacking.androidgame2;

import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by wcauchois on 12/31/14.
 */
public class Turret extends GameObject {
    private static final String TAG = Turret.class.getSimpleName();

    private Vector2D position;
    private Vector2D velocity;
    private static double SPEED = 0.2;

    public Turret(GameServices services, Vector2D position) {
        super(services);
        this.position = position;
        this.velocity = Vector2D.randomUnit().scale(SPEED);
        Log.d(TAG, "Turret created at " + this.position + " with velocity " + this.velocity);
    }

    @Override
    public void simulate(double timeDelta) {
        this.position.addInPlace(this.velocity.scale(timeDelta));
        if (!getServices().getDisplayMetricsService().pointInScreen(this.position)) {
            Log.d(TAG, "Exited screen, dying");
            getServices().getManager().removeObject(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(
                this.getServices().getResourceManager().getBitmapResource(R.drawable.droid_1),
                (int) this.position.getX(),
                (int) this.position.getY(),
                null
        );
    }
}
