package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.element.TileMap;
import net.cloudhacking.androidgame2.engine.utils.TiledImporter;
import net.cloudhacking.androidgame2.unit.GridUnit;
import net.cloudhacking.androidgame2.unit.GridUnitController;

/**
 * Created by research on 1/29/15.
 */
public class PilotLevel extends Level {

    private GridUnitController mGridUnitController;
    private GridUnit mChar;

    @Override
    public void create() {

        InputManager inputManager = getScene().getInputManager();

        // import background tile data from json map exported from Tiled
        TiledImporter.TiledObject imported
                = TiledImporter.loadMaps(Resources.JSON_MAP_MICRO);

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

        size = mTileMapL1.getRect();

        // set up game grid from tile map size; set collision map
        grid = (Grid) add(Grid.createFromTileMap(mTileMapL1));

        // map collision map to occupied
        grid.mapToState(imported.getCollisionMap(), Grid.CellState.EMPTY, Grid.CellState.OCCUPIED);

        mGridUnitController = new GridUnitController(this);
        inputManager.click.connect(mGridUnitController);
        inputManager.drag.connect(mGridUnitController);
        add(mGridUnitController);


        //------------------------------------------------------------------------------------------
        // init level things

        mChar = new GridUnit(Assets.MINI_CHARS);
        mChar.setPermanentSpriteFrame(102);
        mChar.setToCell(grid.getCell(10, 10));
        mGridUnitController.addUnit(mChar);
        add(mChar);

    }

}
