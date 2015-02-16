package net.cloudhacking.androidgame2.engine.element;

import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.FrameBufferObject;
import net.cloudhacking.androidgame2.engine.gl.PreRenderTexture;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;
import net.cloudhacking.androidgame2.engine.utils.TiledImporter;

import java.util.ArrayList;

/**
 * Created by Andrew on 2/15/2015.
 */
public class LayeredTileMap extends PreRenderable {

    private ArrayList<TileMap> mLayers;

    public LayeredTileMap(SpriteAsset tiles, TiledImporter.TiledObject tileMap) {
        super(0,0,0,0);
        mLayers = new ArrayList<TileMap>();

        for (JsonMap layer : tileMap.getLayers()) {
            mLayers.add( new TileMap(tiles, layer) );
        }
    }

    public TileMap getLayer(int i) {
        return mLayers.get(i);
    }

    public float getWidth() {
        return getLayer(0).getWidth();
    }

    public float getHeight() {
        return getLayer(0).getHeight();
    }

    @Override
    protected PreRenderTexture preRender() {

        FrameBufferObject fbo = new FrameBufferObject();
        BasicGLScript gls = GameSkeleton.getGLScript();

        PreRenderTexture fboTex = new PreRenderTexture((int)getWidth(), (int)getHeight());
        fboTex.setFilter(Texture.FilterType.NEAREST, Texture.FilterType.NEAREST);
        fboTex.setWrap(Texture.WrapType.CLAMP, Texture.WrapType.CLAMP);

        if (fbo.start(fboTex)) {

            for (TileMap layer : mLayers) {
                layer.update();
                layer.draw(gls);
            }

            fbo.end();
            fbo.delete();
            return fboTex;

        } else {
            return null;
        }
    }

    public Image getImage() {
        return new Image( getPreRendered() );
    }

    @Override
    public void update() {
        for (TileMap layer : mLayers) {
            layer.update();
        }
    }

    @Override
    public void draw(BasicGLScript gls) {
        for (TileMap layer : mLayers) {
            layer.draw(gls);
        }
    }




}
