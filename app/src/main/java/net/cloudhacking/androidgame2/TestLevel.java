package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.element.TileMap;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.utils.TiledImporter;

import java.util.ArrayList;

/**
 * Created by Andrew on 1/26/2015.
 */
public class TestLevel extends Level {

    private Grid mGrid;
    private RectF mSize;

    @Override
    public void create() {

        // import background tile data from json map exported from Tiled
        ArrayList<TiledImporter.Map> imported
                = TiledImporter.loadMaps(Resources.JSON_MAP_MICRO);

        // create tile maps
        TileMap mTileMapL1 = new TileMap( Assets.MICRO_TILES, imported.get(0) );
        TileMap mTileMapL2 = new TileMap( Assets.MICRO_TILES, imported.get(1) );

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

        // set up game grid from tile map size
        mGrid = (Grid) add(new Grid(mTileMapL1));

        mGrid.cellSelector.connect( new Signal.Listener<Grid.Cell>() {
            @Override
            public boolean onSignal(Grid.Cell cell) {
                Grid.SELECTOR_ICON.startAnimationAt(cell.getCenter());
                addToFront(Grid.SELECTOR_ICON);
                return false;
            }
        });

    }


    @Override
    public RectF getSize() {
        return mSize;
    }

}
