package net.cloudhacking.androidgame2.engine.unit;

import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.element.Group;

import java.util.LinkedHashSet;

/**
 * Created by Andrew on 2/9/2015.
 */
public abstract class UnitController
        extends Group<SelectionHandler.SelectionController>
        implements Signal.Listener
{

    private Level mLevel;
    private SelectionHandler mHandler;
    private LinkedHashSet<Unit> mUnits;
    private Unit mSelected;

    public UnitController(Level level) {
        mLevel = level;
        mUnits = new LinkedHashSet<Unit>();
        mHandler = new SelectionHandler(this);
        mSelected = null;
    }


    @Override
    public boolean onSignal(Object o) {
        if (o instanceof InputManager.Event) {
            return mHandler.processEvent((InputManager.Event) o);
        }
        return false;
    }

    public SelectionHandler.SelectionController select(Unit u) {
        if (mSelected != null) mSelected.unSelect();
        mSelected = u;
        mSelected.select();
        return getControllerFor(u);
    }

    abstract protected SelectionHandler.SelectionController getControllerFor(Unit unit);


    public Unit addUnit(Unit u) {
        mLevel.add(u);
        mUnits.add(u);
        return u;
    }

    public Unit removeUnit(Unit u) {
        mLevel.fastRemove(u);
        mUnits.remove(u);
        return u;
    }

    public Unit getSelected() {
        return mSelected;
    }

    public void clearSelection() {
        if (mSelected != null) mSelected.unSelect();
        mSelected = null;
    }

    public boolean isControlling(Unit u) {
        return mUnits.contains(u);
    }

    public LinkedHashSet<Unit> getControlledUnits() {
        return mUnits;
    }




}
