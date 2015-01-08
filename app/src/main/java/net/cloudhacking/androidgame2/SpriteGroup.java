package net.cloudhacking.androidgame2;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Andrew on 1/7/2015.
 */
public class SpriteGroup<GridItem extends LevelGrid.GridItem> implements Renderable {
    private static final String TAG = SpriteGroup.class.getSimpleName();

    private TileSet mTileSet;
    private ArrayList<GridItem> mGridItemList;

    public SpriteGroup(TileSet tileSet) {
        mTileSet = tileSet;
        mGridItemList = new ArrayList<GridItem>();
    }

    public void setTileSet(TileSet tileSet) {
        mTileSet = tileSet;
    }

    public void addGridItem(GridItem gridItem) {
        mGridItemList.add(gridItem);
    }

    public ArrayList<GridItem> getGridItemList() {
        return mGridItemList;
    }


    public void draw(QuadDrawer quadDrawer) {
        mTileSet.prepareTexture(quadDrawer);
        for (GridItem gridItem : mGridItemList) {
            gridItem.draw(quadDrawer, mTileSet);
        }
    }

}
