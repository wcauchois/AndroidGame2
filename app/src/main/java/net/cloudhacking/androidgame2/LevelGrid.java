package net.cloudhacking.androidgame2;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Andrew on 1/7/2015.
 */
public class LevelGrid {

    private static ArrayList<GridItem> sGridItems = new ArrayList<GridItem>();

    public static ArrayList<GridItem> getGridItems() {
        return sGridItems;
    }

    public static void clearGridItems() {
        sGridItems.clear();
    }


    public static abstract class GridItem {
        private static final String TAG = GridItem.class.getSimpleName();

        protected int mPosX, mPosY;
        protected float mRotation;
        protected float mScaleX, mScaleY;
        protected int mTileIndex;

        public GridItem() {
            sGridItems.add(this);
        }

        public int[] getPos() {
            return new int[] {mPosX, mPosY};
        }

        public int getPosX() {
            return mPosX;
        }

        public int getPosY() {
            return mPosY;
        }

        public int[] getNearestGridNode(LevelGrid levelGrid) {
            return levelGrid.nearestGridNode(mPosX, mPosY);
        }

        public float getRotation() {
            return mRotation;
        }

        public float getScaleX() {
            return mScaleX;
        }

        public float getScaleY() {
            return mScaleY;
        }

        public int getTileIndex() {
            return mTileIndex;
        }

        public void setPos(int[] pos) {
            mPosX = pos[0];
            mPosY = pos[1];
        }

        public void setPosX(int posX) {
            mPosX = posX;
        }

        public void setPosY(int posY) {
            mPosY = posY;
        }

        public void setToGridNode(LevelGrid levelGrid, int gx, int gy) {
            setPos(levelGrid.toPixelCoord(gx, gy));
        }

        public void setRotation(float rotation) {
            mRotation = rotation;
        }

        public void setScaleX(float scaleX) {
            mScaleX = scaleX;
        }

        public void setScaleY(float scaleY) {
            mScaleY = scaleY;
        }

        public void setTileIndex(int tileIndex) {
            mTileIndex = tileIndex;
        }

        // override this; called in GameLevel.update()
        protected void update() {};

        // called in SpriteGroup.draw()
        public void draw(QuadDrawer quadDrawer, TileSet tileSet) {
            //Log.d(TAG, "drawing grid item: mTileIndex="+mTileIndex+", x="+mPosX+", y="+mPosY+", r="+mRotation+", sx="+mScaleX+", sy="+mScaleY);
            tileSet.drawTile(quadDrawer, mTileIndex,
                    mPosX, mPosY, mRotation, mScaleX, mScaleY);
        }

    }


    private int mGridWidth, mGridHeight;
    private int mNodePixelWidth, mNodePixelHeight;

    public LevelGrid(int gridWidth, int gridHeight, int nodePixelWidth, int nodePixelHeight) {
        mGridWidth = gridWidth;
        mGridHeight = gridHeight;
        mNodePixelWidth = nodePixelWidth;
        mNodePixelHeight = nodePixelHeight;
    }

    public int[] getSize() {
        return new int[] {mGridWidth*mNodePixelWidth, mGridHeight*mNodePixelHeight};
    }

    public int[] toPixelCoord(int gx, int gy) {
        int[] pixelCoord = new int[2];
        pixelCoord[0] = mNodePixelWidth/2 + gx*mNodePixelWidth;
        pixelCoord[1] = mNodePixelHeight/2 + gy*mNodePixelHeight;
        return pixelCoord;
    }

    public int[] nearestGridNode(int px, int py) {
        int[] gridCoord = new int[2];
        gridCoord[0] = px/mNodePixelWidth;
        gridCoord[1] = py/mNodePixelHeight;
        return gridCoord;
    }
}
