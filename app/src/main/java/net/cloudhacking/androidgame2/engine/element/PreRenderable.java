package net.cloudhacking.androidgame2.engine.element;

import net.cloudhacking.androidgame2.engine.gl.PreRenderedTexture;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;

import java.util.concurrent.Callable;

/**
 * Created by Andrew on 1/23/2015.
 */
public abstract class PreRenderable extends Renderable {

    /**
     * Interface for pre-renderable things
     */

    public PreRenderable(int x, int y, int w, int h) {
        super(x, y, w, h);
    }


    abstract public PreRenderedTexture preRender();

    public PreRenderedTexture getPreRendered() {

        PreRenderedTexture result = preRender();

        result.setReloader( new Callable<PreRenderedTexture>() {
            @Override
            public PreRenderedTexture call() throws Exception {
                return preRender();
            }
        });

        // add to asset cache so it can be reloaded when gl context is lost on pause
        AssetCache.getInstance().addPreRendered(result);

        return result;
    }

}
