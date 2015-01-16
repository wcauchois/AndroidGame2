package net.cloudhacking.androidgame2.engine;

import net.cloudhacking.androidgame2.engine.utils.Vec2;

import java.util.ArrayList;

/**
 * Created by Andrew on 1/7/2015.
 *
 * This class can handle anything having to do with grid coordinates, including converting to
 * pixel coordinates, and TODO: creep pathing.
 *
 * The GridItem class provides functionality for things that reside on and interact with the
 * level grid.
 */
public class LevelGrid {

    /*
     * Can access this collection of all grid items in GameLevel instance in update() method.
     */
    private static ArrayList<GridItem> sGridItems = new ArrayList<GridItem>();

    public static ArrayList<GridItem> getGridItems() {
        return sGridItems;
    }

    public static void clearGridItems() {
        sGridItems.clear();
    }


    public static abstract class GridItem extends Entity {
        private int mPosX, mPosY;
        private float mRotation;
        private float mScaleX, mScaleY;
        private int mTileIndex;

        public GridItem() {
            sGridItems.add(this);
            mPosX = 0;
            mPosY = 0;
            mRotation = 0.0f;
            mScaleX = 1.0f;
            mScaleY = 1.0f;
            mTileIndex = 0;
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
            //levelGrid.setOccupation(true, gx, gy);
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

        // called in: GameSurfaceRenderer.onDrawFrame() -> RenderLayer.draw() -> SpriteGroup.draw()
        public void draw(QuadDrawerOld quadDrawer, TileSet tileSet) {
            tileSet.drawTile(quadDrawer, mTileIndex,
                    mPosX, mPosY, mRotation, mScaleX, mScaleY);
        }

    }


    private int mGridWidth, mGridHeight;
    private int mNodePixelWidth, mNodePixelHeight;
    private boolean[][] mGridOccupation;

    public LevelGrid(int gridWidth, int gridHeight, int nodePixelWidth, int nodePixelHeight) {
        mGridWidth = gridWidth;
        mGridHeight = gridHeight;
        mNodePixelWidth = nodePixelWidth;
        mNodePixelHeight = nodePixelHeight;

        mGridOccupation = new boolean[mGridWidth][mGridHeight];
        for (int i=0; i<mGridWidth; i++) {
            for (int j=0; j<mGridHeight; j++) {
                mGridOccupation[i][j] = false;
            }
        }
    }

    public Vec2 getSize() {
        return new Vec2(mGridWidth*mNodePixelWidth, mGridHeight*mNodePixelHeight);
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

    public boolean checkOccupation(int gx, int gy) {
        return mGridOccupation[gx][gy];
    }

    public void setOccupation(boolean bool, int gx, int gy) {
        mGridOccupation[gx][gy] = bool;
    }
}
