package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.Signal;

/**
 * Created by wcauchois on 1/21/15.
 */
public class RootWidget extends Widget {

    private int mRootWidth;
    private int mRootHeight;

    public void setRootSize(int sw, int sh) {
        mRootWidth = sw;
        mRootHeight = sh;
        setBounds( new RectF(0, 0, sw, sh) );
    }

    public int getRootWidth() {
        return mRootWidth;
    }

    public int getRootHeight() {
        return mRootHeight;
    }


    public RootWidget(int width, int height) {
        super( new RectF(0, 0, width, height), null );
    }

    public boolean processEvent(InputManager.ClickEvent event) {
        switch(event.getType()) {
            case UP:
                for (Widget w : mEntities) {
                    if (w.handleClick(event)) return true;
                }
                break;
        }
        return false;
    }

}
