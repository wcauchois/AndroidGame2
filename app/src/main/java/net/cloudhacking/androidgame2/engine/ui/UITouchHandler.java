package net.cloudhacking.androidgame2.engine.ui;

import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 2/10/2015.
 */
public class UITouchHandler extends Loggable {

    public static class WidgetController<W extends Widget> extends Entity {
        protected void onClickDown(W selected, PointF scenePt) {}
        protected void onClickCancel(W selected, PointF scenePt) {}
        protected void onClickUp(W selected, PointF scenePt) {}
        protected void onStartDrag(W selected, PointF scenePt) {}
        protected void onUpdateDrag(W selected, PointF scenePt) {}
        protected void onEndDrag(W selected, PointF scenePt) {}
    }

    private UI mUI;

    public UITouchHandler(UI ui) {
        mUI = ui;

        mDownSelect = false;
        mDragSelectionStarted = false;
        mWidgetController = new WidgetController();
    }


    //----------------------------------------------------------------------------------------------

    private boolean mDownSelect;
    private boolean mDragSelectionStarted;
    private WidgetController<Widget> mWidgetController;

    public boolean processEvent(InputManager.Event e) {
        if (e instanceof InputManager.ClickEvent) {
            return handleClickEvent((InputManager.ClickEvent) e);
        } else if (e instanceof InputManager.DragEvent) {
            return handleDragEvent((InputManager.DragEvent) e);
        }
        return false;
    }

    public boolean handleClickEvent(InputManager.ClickEvent e) {
        PointF scenePt = e.getPos();
        switch(e.getType()) {
            case DOWN:
                for (Widget w : mUI.getRoot().getEntities()) {
                    if (w.isTouchable() && w.containsPt(scenePt)) {
                        mWidgetController = mRoot.select(u);
                        mDownSelect = true;
                        mWidgetController.onClickDown(mUnitController.getSelected(), scenePt);
                        return true;
                    }
                }
                break;

            case CANCEL:
                mWidgetController.onClickCancel(mUnitController.getSelected(), scenePt);
                break;

            case UP:
                if (!mDownSelect) {
                    mUnitController.clearSelection();
                    return false;
                }
                mDownSelect = false;
                mWidgetController.onClickUp(mUnitController.getSelected(), scenePt);
                break;
        }
        return false;
    }

    public boolean handleDragEvent(InputManager.DragEvent e) {
        if (!mDownSelect) {
            return false;
        }
        PointF scenePt = e.getPos();
        switch (e.getType()) {
            case START:
                mDragSelectionStarted = true;
                mWidgetController.onStartDrag(mUnitController.getSelected(), scenePt);
                break;

            case UPDATE:
                if (!mDragSelectionStarted) return false;
                mWidgetController.onUpdateDrag(mUnitController.getSelected(), scenePt);
                break;

            case END:
                if (!mDragSelectionStarted) return false;
                mWidgetController.onEndDrag(mUnitController.getSelected(), scenePt);
                mDownSelect = false;
                break;
        }
        return true;
    }
}
