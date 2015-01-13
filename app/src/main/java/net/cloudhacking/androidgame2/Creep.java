package net.cloudhacking.androidgame2;

import android.opengl.Matrix;

import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Created by Andrew on 1/7/2015.
 */
public /*abstract*/ class Creep extends AnimatedGridItem {
    private static final String TAG = Creep.class.getSimpleName();

    // TODO: see comment in Tower class about this hashmap.
    private static HashMap<String, int[]> sAnimationCache = new HashMap<String, int[]>();

    public static void addAnimationSeq(String handle, int[] animationSeq) {
        sAnimationCache.put(handle, animationSeq);
    }

    public void queueAnimation(String handle, long frameTime, boolean loop) {
        queueAnimationSequence(sAnimationCache.get(handle), frameTime, loop);
    }


    private float mVelocity;
    private ArrayDeque<int[]> mWaypoints;
    private int[] mCurrentWaypoint;
    private static final int WAYPOINT_THRESHOLD = 5 /*pixels*/;  // distance from waypoint where we
                                                                 // can consider it reached

    public Creep() {
        mWaypoints = new ArrayDeque<int[]>();
    }

    public float getVelocity() {
        return mVelocity;
    }

    public void setVelocity(float velocity) {
        mVelocity = velocity;
    }

    public ArrayDeque<int[]> getWaypoints() {
        return mWaypoints;
    }

    public void setWaypoints(ArrayDeque<int[]> waypoints) {
        mWaypoints = waypoints;
        updateCurrentWaypoint();
    }

    public void queueWaypoint(int[] waypoint) {
        mWaypoints.addLast(waypoint);
    }

    public int[] popWaypoint() {
        return mWaypoints.removeFirst();
    }

    public int[] getDestination() {
        return mWaypoints.getLast();
    }

    public int[] getCurrentWaypoint() {
        return mWaypoints.getFirst();
    }

    public void updateCurrentWaypoint() {
        if (mWaypoints.isEmpty()) {
            mCurrentWaypoint = null;
            return;
        }
        mCurrentWaypoint = getCurrentWaypoint();
    }


    public void update() {
        updateAnimation();

        // if current waypoint is null, don't do anything
        if (mCurrentWaypoint==null) {
            return;
        }

        // move towards current waypoint
        int[] pos, dir;
        float distToWaypoint;

        pos = getPos();
        dir = new int[] {mCurrentWaypoint[0]-pos[0], mCurrentWaypoint[1]-pos[1]};  // vector towards next waypoint
        distToWaypoint = Matrix.length(dir[0], dir[1], 0.0f);

        // if distToWaypoint falls within target threshold, pop waypoint and go to next waypoint
        if (distToWaypoint < WAYPOINT_THRESHOLD) {
            popWaypoint();
            updateCurrentWaypoint();
            return;
        }

        setPosX(pos[0] + (int) (dir[0]*(mVelocity/distToWaypoint)));
        setPosY(pos[1] + (int) (dir[1]*(mVelocity/distToWaypoint)));
    }


}
