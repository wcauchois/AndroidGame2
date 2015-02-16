package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.element.LayeredTileMap;
import net.cloudhacking.androidgame2.engine.element.TileMap;
import net.cloudhacking.androidgame2.engine.utils.TiledImporter;
import net.cloudhacking.androidgame2.unit.CDUnitController;
import net.cloudhacking.androidgame2.unit.gridUnit.GridUnit;

/**
 * Created by research on 1/29/15.
 */
public class CDLevel extends Level {

    private Grid mGrid;
    private RectF mSize;

    private CDUnitController mController;
    private GridUnit mChar;

    @Override
    public void create() {

        InputManager inputManager = ColonyDrop.getInputManager();

        // import background tile data from json map exported from Tiled
        TiledImporter.TiledObject imported
                = TiledImporter.loadMaps(Resources.MINIMAL_TEST);

        LayeredTileMap tileMap = new LayeredTileMap(Assets.MINIMAL, imported);

        Image bg = new Image( tileMap.getPreRendered() );
        bg.moveToOrigin();
        bg.update();
        bg.setInactive();
        add(bg);

        mSize = tileMap.getLayer(0).getRect();
        mGrid = (Grid) add(Grid.createFromTileMap(tileMap.getLayer(0)));


        // map collision map to occupied
        mGrid.mapToState(imported.getCollisionMap(), Grid.CellState.EMPTY, Grid.CellState.OCCUPIED);

        mController = new CDUnitController(this);
        inputManager.click.connect(mController);
        inputManager.drag.connect(mController);
        add(mController);

        //------------------------------------------------------------------------------------------
        // init level things

        mChar = new GridUnit(Assets.MINIMAL);
        mChar.setPermanentSpriteFrame(17);
        mChar.setToCell(mGrid.getCell(17, 17));
        mController.addUnit(mChar);

        mController.getMothershipController().setPos(mGrid.getCell(17, 17));

    }

    public Grid getGrid() {
        return mGrid;
    }

    @Override
    public RectF getSize() {
        return mSize;
    }

    public CDUnitController getUnitController() {
        return mController;
    }

}
