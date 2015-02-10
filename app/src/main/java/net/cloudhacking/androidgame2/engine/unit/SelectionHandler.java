package net.cloudhacking.androidgame2.engine.unit;

import net.cloudhacking.androidgame2.engine.CameraController;
import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 2/9/2015.
 */
public class SelectionHandler extends Loggable {

    public static class SelectionController<U extends Unit> extends Entity {
        protected void onClickDown(U selected, PointF scenePt) {}
        protected void onClickCancel(U selected, PointF scenePt) {}
        protected void onClickUp(U selected, PointF scenePt) {}
        protected void onStartDrag(U selected, PointF scenePt) {}
        protected void onUpdateDrag(U selected, PointF scenePt) {}
        protected void onEndDrag(U selected, PointF scenePt) {}
    }

    private UnitController mUnitController;
    private SelectionController<Unit> mSelectionController;
    private CameraController mCameraController;

    public SelectionHandler(UnitController unitController) {
        mUnitController = unitController;
        mSelectionController = new SelectionController<Unit>();
        mCameraController = GameSkeleton.getCameraController();

        mDownSelect = false;
        mDragSelectionStarted = false;
    }


    //----------------------------------------------------------------------------------------------

    private boolean mDownSelect;
    private boolean mDragSelectionStarted;

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
                for (Unit u : mUnitController.getControlledUnits()) {
                    if (u.isSelectable() && u.containsPt(scenePt)) {
                        mSelectionController = mUnitController.select(u);
                        mDownSelect = true;
                        mSelectionController.onClickDown(mUnitController.getSelected(), scenePt);
                        mCameraController.setDisabled(true);
                        return true;
                    }
                }
                break;

            case CANCEL:
                mSelectionController.onClickCancel(mUnitController.getSelected(), scenePt);
                break;

            case UP:
                if (!mDownSelect) {
                    mUnitController.clearSelection();
                    return false;
                }
                mDownSelect = false;
                mSelectionController.onClickUp(mUnitController.getSelected(), scenePt);
                mCameraController.setDisabled(false);
                break;
        }
        return false;
    }

    public boolean handleDragEvent(InputManager.DragEvent e) {
        if (!mDownSelect) {
            mCameraController.setDisabled(false);
            return false;
        }
        PointF scenePt = e.getPos();
        switch (e.getType()) {
            case START:
                mDragSelectionStarted = true;
                mSelectionController.onStartDrag(mUnitController.getSelected(), scenePt);
                break;

            case UPDATE:
                if (!mDragSelectionStarted) return false;
                mSelectionController.onUpdateDrag(mUnitController.getSelected(), scenePt);
                break;

            case END:
                if (!mDragSelectionStarted) return false;
                mSelectionController.onEndDrag(mUnitController.getSelected(), scenePt);
                mDownSelect = false;
                mCameraController.setDisabled(false);
                break;
        }
        return true;
    }
}
