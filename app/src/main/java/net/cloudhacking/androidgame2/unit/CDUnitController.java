package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.CDLevel;
import net.cloudhacking.androidgame2.engine.unit.SelectionHandler;
import net.cloudhacking.androidgame2.engine.unit.Unit;
import net.cloudhacking.androidgame2.engine.unit.UnitController;
import net.cloudhacking.androidgame2.unit.gridUnit.GridUnit;
import net.cloudhacking.androidgame2.unit.gridUnit.GridUnitController;
import net.cloudhacking.androidgame2.unit.mothership.Mothership;
import net.cloudhacking.androidgame2.unit.mothership.MothershipController;

/**
 * Created by Andrew on 2/9/2015.
 */
public class CDUnitController extends UnitController {

    private MothershipController mMothershipController;
    private GridUnitController mGridUnitController;

    public CDUnitController(CDLevel level) {
        super(level);
        mMothershipController = new MothershipController(level);
        add(mMothershipController);
        addUnit(mMothershipController.getMothership());

        mGridUnitController = new GridUnitController(level);
        add(mGridUnitController);
    }

    @Override
    protected SelectionHandler.SelectionController getControllerFor(Unit unit) {
        if (unit instanceof Mothership) return mMothershipController;
        if (unit instanceof GridUnit)   return mGridUnitController;
        return null;
    }


    public GridUnitController getGridUnitController() {
        return mGridUnitController;
    }

    public MothershipController getMothershipController() {
        return mMothershipController;
    }


}
