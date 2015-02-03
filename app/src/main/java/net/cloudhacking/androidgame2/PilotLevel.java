package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.element.TileMap;
import net.cloudhacking.androidgame2.engine.utils.TiledImporter;
import net.cloudhacking.androidgame2.unit.ControllableUnit;
import net.cloudhacking.androidgame2.unit.UnitController;

/**
 * Created by research on 1/29/15.
 */
public class PilotLevel extends Level {

    private UnitController mUnitController;
    private ControllableUnit mMothership;

    @Override
    public void create() {

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

        size = mTileMapL1.getRect();


        // set up game grid from tile map size; set collision map
        grid = (Grid) add(Grid.createFromTileMap(mTileMapL1));

        // map collision map to occupied
        grid.mapToState(imported.getCollisionMap(), Grid.CellState.EMPTY, Grid.CellState.OCCUPIED);

        grid.cellSelector.connect( new Signal.Listener<Grid.Cell>() {
            @Override
            public boolean onSignal(Grid.Cell cell) {
                grid.SELECTOR_ICON.startAnimationAt(cell.getCenter());
                addToFront(grid.SELECTOR_ICON);
                return false;
            }
        });


        mUnitController = new UnitController(grid);

        mMothership = new ControllableUnit(Assets.MOTHERSHIP);
        mMothership.moveToCell( grid.getCell(50,50) );
        mUnitController.add(mMothership);

        grid.cellSelector.connect(mUnitController);

        add(mUnitController);


    }

}
