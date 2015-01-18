package net.cloudhacking.androidgame2.engine;

import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 1/17/2015.
 */
public class TileMap extends Renderable {

    private Texture texture;
    private TextureFrameSet frameSet;
    private JsonMap map;

    public TileMap() {
        super(0, 0, 1, 1);
    }

}
