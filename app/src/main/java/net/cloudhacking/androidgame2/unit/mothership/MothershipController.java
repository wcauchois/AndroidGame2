package net.cloudhacking.androidgame2.unit.mothership;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.element.shape.Circle;
import net.cloudhacking.androidgame2.engine.element.shape.Line;
import net.cloudhacking.androidgame2.engine.utils.Color;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 2/6/2015.
 */
public class MothershipController extends Entity implements Signal.Listener {

    private Level mLevel;
    private Grid mGrid;
    private Mothership mMothership;
    private Circle mSelectionAnim;

    public MothershipController(Level level) {
        mLevel = level;
        mGrid = level.grid;
        mMothership = new Mothership();

        mSelectionAnim =
                new Circle(mMothership.getClickRadius(), 2f, Color.WHITE);
        mSelectionAnim.hide();
        mLevel.add(mSelectionAnim);
        mLevel.add(mMothership);
        initSelector();
    }

    public void setPos(Grid.Cell target) {
        mMothership.moveTo(target);
    }

    @Override
    public void update() {
        if (mMothership.isSelected()) {
            mSelectionAnim.setPos(mMothership.getPos());
            mSelectionAnim.show();
        } else {
            mSelectionAnim.hide();
        }
    }

    //----------------------------------------------------------------------------------------------
    // selection and movement handling

    // TODO: Should probably make some kind of drag and dropper thing

    private boolean mDownSelect;
    private Grid.GridOverlay mGridOverlay;
    private Line mDragLine;
    private Circle mDragReticle;

    private void initSelector() {
        mDownSelect = false;
        mDragLine = new Line(new PointF(), new PointF(), 1, Color.RED);
        mDragReticle = new Circle(8, 1, Color.BLUE);
        mDragLine.hide();
        mDragReticle.hide();
        mLevel.add(mDragLine);
        mLevel.add(mDragReticle);
        mGridOverlay = new Grid.GridOverlay(mGrid, new Color(1,1,1,0.5f));
        mLevel.add(mGridOverlay);
        mGridOverlay.hide();
    }

    public void startDrag() {
        mDragLine.show();
        mDragReticle.show();
        mGridOverlay.show();
        mLevel.bringToFront(mDragLine);
        mLevel.bringToFront(mDragReticle);
        mLevel.bringToFront(mSelectionAnim);
        mLevel.bringToFront(mMothership);
    }

    public void updateDrag(PointF p) {
        mDragReticle.setPos(p);
        mDragLine.setEndPoints(mMothership.getPos(), p);
    }

    public void endDrag() {
        mGridOverlay.hide();
        mDragLine.hide();
        mDragReticle.hide();
    }

    @Override
    public boolean onSignal(Object o) {
        if (o instanceof InputManager.ClickEvent) {
            return handleClickEvent((InputManager.ClickEvent) o);

        } else if (o instanceof InputManager.DragEvent) {
            return handleDragEvent((InputManager.DragEvent) o);
        }

        return false;
    }

    public boolean handleClickEvent(InputManager.ClickEvent e) {
        switch(e.getType()) {
            case DOWN:
                if (mMothership.containsPt(mLevel.cam2scene(e.getPos()))) {
                    mDownSelect = true;
                    mMothership.select();
                    mLevel.bringToFront(mMothership);

                    mLevel.getScene().getCameraController().setDisabled(true);
                    return true;
                }
                break;

            case UP:
                if (!mDownSelect) mMothership.unSelect();
                mDownSelect = false;

                mLevel.getScene().getCameraController().setDisabled(false);
                break;
        }
        return false;
    }

    public boolean handleDragEvent(InputManager.DragEvent e) {
        if (!mMothership.isSelected()) {
            mLevel.getScene().getCameraController().setDisabled(false);
            return false;
        }

        switch (e.getType()) {
            case START:
                startDrag();
                break;

            case UPDATE:
                updateDrag(mLevel.cam2scene(e.getPos()));
                break;

            case END:
                endDrag();
                mDownSelect = false;
                mMothership.moveTo(mGrid.nearestCell(mLevel.cam2scene(e.getPos())));

                mLevel.getScene().getCameraController().setDisabled(false);
                break;
        }
        return true;
    }
}
