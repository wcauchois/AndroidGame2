package net.cloudhacking.androidgame2.engine.ui;

import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 2/10/2015.
 */
public class UITouchHandler extends Loggable {

    public static class WidgetController extends Entity {
        protected void onClickDown(PointF touchPt) {}
        protected void onClickCancel(PointF touchPt) {}
        protected void onClickUp(PointF touchPt) {}
        protected void onStartDrag(PointF touchPt) {}
        protected void onUpdateDrag(PointF touchPt) {}
        protected void onEndDrag(PointF touchPt) {}
    }


    //----------------------------------------------------------------------------------------------

    private UI mUI;

    private boolean mDownSelect;
    private boolean mDragSelectionStarted;
    private WidgetController mWidgetController;


    public UITouchHandler(UI ui) {
        mUI = ui;

        mDownSelect = false;
        mDragSelectionStarted = false;
        mWidgetController = new WidgetController();
    }

    public boolean processEvent(InputManager.Event e) {
        if (e instanceof InputManager.ClickEvent) {
            return handleClickEvent((InputManager.ClickEvent) e);
        } else if (e instanceof InputManager.DragEvent) {
            return handleDragEvent((InputManager.DragEvent) e);
        }
        return false;
    }

    public boolean handleClickEvent(InputManager.ClickEvent e) {
        PointF touchPt = e.getPos();
        switch(e.getType()) {
            case DOWN:
                mWidgetController = mUI.selectWidgetOnTouch(touchPt);
                if (mWidgetController == null) return false;
                mDownSelect = true;
                mWidgetController.onClickDown(touchPt);
                break;

            case CANCEL:
                if (!mDownSelect) return false;
                mWidgetController.onClickCancel(touchPt);
                break;

            case UP:
                if (!mDownSelect) {
                    mUI.clearSelection();
                    return false;
                }
                mDownSelect = false;
                mWidgetController.onClickUp(touchPt);
                break;
        }
        return true;
    }

    public boolean handleDragEvent(InputManager.DragEvent e) {
        if (!mDownSelect) {
            return false;
        }
        PointF touchPt = e.getPos();
        switch (e.getType()) {
            case START:
                mDragSelectionStarted = true;
                mWidgetController.onStartDrag(touchPt);
                break;

            case UPDATE:
                if (!mDragSelectionStarted) return false;
                mWidgetController.onUpdateDrag(touchPt);
                break;

            case END:
                if (!mDragSelectionStarted) return false;
                mWidgetController.onEndDrag(touchPt);
                mDownSelect = false;
                break;
        }
        return true;
    }
}
