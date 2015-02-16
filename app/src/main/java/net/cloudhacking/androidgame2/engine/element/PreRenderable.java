package net.cloudhacking.androidgame2.engine.element;

import net.cloudhacking.androidgame2.engine.gl.PreRenderTexture;
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


    abstract protected PreRenderTexture preRender();

    public PreRenderTexture getPreRendered() {

        PreRenderTexture result = preRender();

        result.setReloader( new Callable<PreRenderTexture>() {
            @Override
            public PreRenderTexture call() throws Exception {
                return preRender();
            }
        });

        // add to asset cache so it can be reloaded when gl context is lost on pause
        AssetCache.getInstance().addPreRendered(result);

        return result;
    }

}
