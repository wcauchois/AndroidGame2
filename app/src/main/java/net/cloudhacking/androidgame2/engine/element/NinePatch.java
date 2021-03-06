package net.cloudhacking.androidgame2.engine.element;

import android.graphics.Rect;
import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.QuadDrawer;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.ui.widget.WidgetBackground;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.NinePatchAsset;

import java.nio.FloatBuffer;

/**
 * Created by Andrew on 1/24/2015.
 */
public class NinePatch extends Renderable implements WidgetBackground {

    private Texture mTexture;
    private Rect mCenterPatch;

    private int mMinWidth;
    private int mMinHeight;

    private FloatBuffer mVertexBuffer;

    public NinePatch(NinePatchAsset asset) {
        this(asset, 0, 0);
    }

    public NinePatch(NinePatchAsset asset, float width, float height) {
        super(0, 0, width, height);

        mTexture = AssetCache.getInstance().getTexture(asset);
        mCenterPatch = asset.getCenterPatch();

        mMinWidth = mTexture.getWidth() - mCenterPatch.width();
        mMinHeight = mTexture.getHeight() - mCenterPatch.height();

        mVertexBuffer = BufferUtils.makeQuadFloatBuffer(9);
        setSize(width, height);
    }


    public void setSize(float w, float h) {
        w = Math.max(w, mMinWidth);
        h = Math.max(h, mMinHeight);

        setActualWidth(w);
        setActualHeight(h);

        int tw = mTexture.getWidth();
        int th = mTexture.getHeight();

        float cl = (float)mCenterPatch.left;
        float ct = (float)mCenterPatch.top;
        float cr = (float)mCenterPatch.right;
        float cb = (float)mCenterPatch.bottom;

        float crs = cl + w - mMinWidth;
        float cbs = ct + h - mMinHeight;

        float cluv = cl/tw;
        float ctuv = ct/th;
        float cruv = cr/tw;
        float cbuv = cb/th;

        float[] V = new float[16];
        mVertexBuffer.position(0);

        // top-left
        QuadDrawer.fillUVCoords(V, 0, 0, cluv, ctuv);
        QuadDrawer.fillVertices(V, 0, 0, cl, ct);
        mVertexBuffer.put(V);

        // center-top
        QuadDrawer.fillUVCoords(V, cluv, 0, cruv, ctuv);
        QuadDrawer.fillVertices(V, cl, 0, crs, ct);
        mVertexBuffer.put(V);

        // top-right
        QuadDrawer.fillUVCoords(V, cruv, 0, 1, ctuv);
        QuadDrawer.fillVertices(V, crs, 0, w, ct);
        mVertexBuffer.put(V);

        // center-left
        QuadDrawer.fillUVCoords(V, 0, ctuv, cluv, cbuv);
        QuadDrawer.fillVertices(V, 0, ct, cl, cbs);
        mVertexBuffer.put(V);

        // center
        QuadDrawer.fillUVCoords(V, cluv, ctuv, cruv, cbuv);
        QuadDrawer.fillVertices(V, cl, ct, crs, cbs);
        mVertexBuffer.put(V);

        // center-right
        QuadDrawer.fillUVCoords(V, cruv, ctuv, 1, cbuv);
        QuadDrawer.fillVertices(V, crs, ct, w, cbs);
        mVertexBuffer.put(V);

        // bottom-left
        QuadDrawer.fillUVCoords(V, 0, cbuv, cluv, 1);
        QuadDrawer.fillVertices(V, 0, cbs, cl, h);
        mVertexBuffer.put(V);

        // center-bottom
        QuadDrawer.fillUVCoords(V, cluv, cbuv, cruv, 1);
        QuadDrawer.fillVertices(V, cl, cbs, crs, h);
        mVertexBuffer.put(V);

        // bottom-right
        QuadDrawer.fillUVCoords(V, cruv, cbuv, 1, 1);
        QuadDrawer.fillVertices(V, crs, cbs, w, h);
        mVertexBuffer.put(V);

    }

    @Override
    public void setToRect(RectF rect) {
        setPos(rect.left, rect.top);
        setSize(rect.width(), rect.height());
    }


    @Override
    public void draw(BasicGLScript gls) {
        super.draw(gls);
        mTexture.bind();
        gls.drawQuadSet(mVertexBuffer, 9);
    }


}
