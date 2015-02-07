package net.cloudhacking.androidgame2.engine.utils;

import net.cloudhacking.androidgame2.engine.element.TileMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads a map using a simple JSON format.
 *
 * Created by wcauchois on 1/4/15.
 */
public class JsonMap extends Loggable implements TileMap.Map {

    private int[] tiles;
    private int width;
    private int height;

    public JsonMap(Resource resource) {
        loadFromResource(resource);
    }


    public int getTile(int ix, int iy) {
        return tiles[iy * width + ix];
    }

    public int getTile(int index) {
        return tiles[index];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public void loadFromResource(Resource resource) {
        String resourceString = ResourceUtils.readTextFileFromRawResource(resource);
        try {
            JSONObject object = new JSONObject(resourceString);
            JSONArray tilesArray = object.getJSONArray("tiles");
            List<List<Integer>> rows = new ArrayList<List<Integer>>();
            int maxCols = -1, row, col;

            for (row = 0; row < tilesArray.length(); row++) {
                List<Integer> rowArray = new ArrayList<Integer>();
                JSONArray rowJArray = tilesArray.getJSONArray(row);

                for (col = 0; col < rowJArray.length(); col++) {
                    rowArray.add(rowJArray.getInt(col));
                }

                if (maxCols >= 0 && rowArray.size() != maxCols) {
                    w("tile row of abnormal length (expected: " +
                            maxCols + ", actual: " + rowArray.size() + ")");
                }

                rows.add(rowArray);
                maxCols = Math.max(maxCols, rowArray.size());
            }

            this.width = maxCols;
            this.height = rows.size();
            this.tiles = new int[width * height];

            for (row = 0; row < this.height; row++) {
                for (col = 0; col < this.width; col++) {
                    List<Integer> rowArray = rows.get(row);
                    if (rowArray == null || rowArray.get(col) == null) {
                        w("couldn't get value at (" + col + ", " + row + ")");
                    } else {
                        this.tiles[col + row * this.width] = rowArray.get(col);
                    }
                }
            }

            i("loaded JSON map: width=" + this.width + ", height=" + this.height);
        } catch (JSONException e) {
            e("failed to load JSON map: " + e);
            throw new RuntimeException(e);
        }
    }
}
