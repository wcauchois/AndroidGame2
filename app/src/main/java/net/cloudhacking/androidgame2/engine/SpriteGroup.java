package net.cloudhacking.androidgame2.engine;

import java.util.ArrayList;

/**
 * Created by Andrew on 1/7/2015.
 */
public class SpriteGroup implements Renderable {
    private static final String TAG = SpriteGroup.class.getSimpleName();

    private TileSet mTileSet;
    private ArrayList<LevelGrid.GridItem> mGridItemList;

    public SpriteGroup(TileSet tileSet) {
        mTileSet = tileSet;
        mGridItemList = new ArrayList<LevelGrid.GridItem>();
    }

    public void setTileSet(TileSet tileSet) {
        mTileSet = tileSet;
    }

    public void addGridItem(LevelGrid.GridItem gridItem) {
        mGridItemList.add(gridItem);
    }

    public ArrayList<LevelGrid.GridItem> getGridItemList() {
        return mGridItemList;
    }


    public void draw(QuadDrawer quadDrawer) {
        mTileSet.prepareTexture(quadDrawer);
        for (LevelGrid.GridItem gridItem : mGridItemList) {
            gridItem.draw(quadDrawer, mTileSet);
        }
    }

}
