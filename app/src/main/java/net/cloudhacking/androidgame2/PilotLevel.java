package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.element.TileMap;
import net.cloudhacking.androidgame2.engine.utils.TiledImporter;
import net.cloudhacking.androidgame2.example.TestCreepFactory;

/**
 * Created by research on 1/29/15.
 */
public class PilotLevel extends Level {

    private Grid mGrid;
    private RectF mSize;

    private TestCreepFactory mFactory;

    @Override
    public void create() {

        // import background tile data from json map exported from Tiled
        TiledImporter.TiledObject imported
                = TiledImporter.loadMaps(Resources.JSON_MAP_MICRO);

        // create tile maps
        TileMap mTileMapL1 = new TileMap( Assets.MICRO_TILES, imported.getLayers().get(0) );
        TileMap mTileMapL2 = new TileMap( Assets.MICRO_TILES, imported.getLayers().get(1) );

        // pre-render tile maps and add to level
        Image tileMapBase = new Image( mTileMapL1.getPreRendered() );
        tileMapBase.moveToOrigin();
        tileMapBase.update();
        tileMapBase.setInactive();
        add(tileMapBase);

        Image tileMapTerrain = new Image( mTileMapL2.getPreRendered() );
        tileMapTerrain.moveToOrigin();
        tileMapTerrain.update();
        tileMapTerrain.setInactive();
        add(tileMapTerrain);

        mSize = mTileMapL1.getRect();

        // set up game grid from tile map size; set collision map
        mGrid = (Grid) add(new Grid(mTileMapL1));
        mGrid.setOccupation(imported.getCollisionMap());

        mGrid.cellSelector.connect( new Signal.Listener<Grid.Cell>() {
            @Override
            public boolean onSignal(Grid.Cell cell) {
                mGrid.SELECTOR_ICON.startAnimationAt(cell.getCenter());
                addToFront(mGrid.SELECTOR_ICON);
                return false;
            }
        });


    }


    @Override
    public RectF getSize() {
        return mSize;
    }

}
