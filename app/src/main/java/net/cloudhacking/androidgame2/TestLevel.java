package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.element.TileMap;
import net.cloudhacking.androidgame2.engine.utils.Signal;
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


        ArrayList<TiledImporter.TiledMap> imported
                = TiledImporter.loadMaps(Resources.JSON_MAP_MICRO);

        //mTileMap = new TileMap( Assets.TEST_TILESET, new JsonMap(Resources.JSON_MAP_SIMPLE) );
        TileMap mTileMapL1 = new TileMap( Assets.MICRO_TILES, imported.get(0) );
        TileMap mTileMapL2 = new TileMap( Assets.MICRO_TILES, imported.get(1) );

        Image tileMapBase = new Image( mTileMapL1.getPreRendered() );
        Image tileMapTerrain = new Image( mTileMapL2.getPreRendered() );
        tileMapBase.moveToOrigin();
        tileMapTerrain.moveToOrigin();
        add(tileMapBase);
        add(tileMapTerrain);

        mSize = mTileMapL1.getRect();

        // set up game grid from tile map
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
