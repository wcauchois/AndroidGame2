package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.element.Image;
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
                = TiledImporter.loadMaps(Resources.BIG_MAP);

        // create tile maps
        TileMap mTileMapL1 = new TileMap( Assets.MICRO_TILES, imported.getLayers().get(0) );
        TileMap mTileMapL2 = new TileMap( Assets.MICRO_TILES, imported.getLayers().get(1) );

        // pre-render tile maps and add to level
        Image tileMapWater = new Image( mTileMapL1.getPreRendered() );
        tileMapWater.moveToOrigin();
        tileMapWater.update();
        tileMapWater.setInactive();
        add(tileMapWater);

        Image tileMapTerrain = new Image( mTileMapL2.getPreRendered() );
        tileMapTerrain.moveToOrigin();
        tileMapTerrain.update();
        tileMapTerrain.setInactive();
        add(tileMapTerrain);

        mSize = mTileMapL1.getRect();

        // set up game grid from tile map size; set collision map
        mGrid = (Grid) add(Grid.createFromTileMap(mTileMapL1));

        // map collision map to occupied
        mGrid.mapToState(imported.getCollisionMap(), Grid.CellState.EMPTY, Grid.CellState.OCCUPIED);

        mController = new CDUnitController(this);
        inputManager.click.connect(mController);
        inputManager.drag.connect(mController);
        add(mController);

        //------------------------------------------------------------------------------------------
        // init level things

        mChar = new GridUnit(Assets.MINI_CHARS);
        mChar.setPermanentSpriteFrame(102);
        mChar.setToCell(mGrid.getCell(50, 50));
        mController.addUnit(mChar);

        mController.getMothershipController().setPos(mGrid.getCell(50, 50));

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
