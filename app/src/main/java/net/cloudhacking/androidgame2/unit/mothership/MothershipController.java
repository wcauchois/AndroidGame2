package net.cloudhacking.androidgame2.unit.mothership;

import net.cloudhacking.androidgame2.CDLevel;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.element.shape.Circle;
import net.cloudhacking.androidgame2.engine.element.shape.ColorBlock;
import net.cloudhacking.androidgame2.engine.element.shape.Line;
import net.cloudhacking.androidgame2.engine.unit.SelectionHandler;
import net.cloudhacking.androidgame2.engine.gl.GLColor;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 2/6/2015.
 */
public class MothershipController extends SelectionHandler.SelectionController<Mothership> {

    private CDLevel mLevel;
    private Grid mGrid;

    private Mothership mMothership;
    private Circle mSelectionAnim;

    private Grid.GridOverlay mGridOverlay;
    private Line mDragLine;
    private ColorBlock mDragReticle;

    public MothershipController(CDLevel level) {
        mLevel = level;
        mGrid = level.getGrid();
        mMothership = new Mothership();

        GLColor grey = new GLColor(1,1,1,0.5f);
        mSelectionAnim = new Circle(mMothership.getClickRadius(), 2f, grey);
        mSelectionAnim.hide();
        mLevel.add(mMothership);
        mLevel.add(mSelectionAnim);

        mDragLine = new Line(new PointF(), new PointF(), 1, grey);
        mDragReticle = new ColorBlock(8, 8, grey);
        mGridOverlay = new Grid.GridOverlay(mGrid, grey);
        mDragLine.hide();
        mDragReticle.hide();
        mGridOverlay.hide();
        mLevel.add(mDragLine);
        mLevel.add(mDragReticle);
        mLevel.add(mGridOverlay);
    }

    public Mothership getMothership() {
        return mMothership;
    }

    public void setPos(Grid.Cell target) {
        mMothership.moveTo(target);
    }


    @Override
    protected void onStartDrag(Mothership unused, PointF scenePt) {
        mDragLine.show();
        mDragReticle.show();
        mGridOverlay.show();
        mLevel.bringToFront(mDragLine);
        mLevel.bringToFront(mDragReticle);

        PointF cellPt = mGrid.nearestCell(scenePt).getCenter();
        mDragReticle.setPos(cellPt);
        mDragLine.setEndPoints(mMothership.getPos(), cellPt);
    }

    @Override
    protected void onUpdateDrag(Mothership unused, PointF scenePt) {
        PointF cellPt = mGrid.nearestCell(scenePt).getCenter();
        mDragReticle.setPos(cellPt);
        mDragLine.setEndPoints(mMothership.getPos(), cellPt);
    }

    @Override
    protected void onEndDrag(Mothership unused, PointF scenePt) {
        mMothership.moveTo(mGrid.nearestCell(scenePt));
        mGridOverlay.hide();
        mDragLine.hide();
        mDragReticle.hide();
    }


    @Override
    public void update() {
        if (mMothership.isSelected()) {
            mSelectionAnim.setPos(mMothership.getPos());
            mSelectionAnim.show();
            mLevel.queueBringToFront(mSelectionAnim);
        } else {
            mSelectionAnim.hide();
        }
        mLevel.queueBringToFront(mMothership);
    }
}
