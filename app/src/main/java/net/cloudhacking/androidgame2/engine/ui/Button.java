package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.MatrixUtils;
import net.cloudhacking.androidgame2.engine.utils.TextureCache;

import java.nio.FloatBuffer;

/**
 * Created by wcauchois on 1/21/15.
 */
public class Button extends Widget {
    private FloatBuffer mVertexBuffer;
    private Texture mTexture;
    private float[] mModelMatrix = new float[16];

    public Button(RectF bounds) {
        super(bounds);

        // TODO(wcauchois): Copypasted from Image, need to make drawing quads easier
        float halfWidth = bounds.width() / 2.0f, halfHeight = bounds.height() / 2.0f;
        mVertexBuffer = BufferUtils.makeFloatBuffer(new float[]{
                -halfWidth, -halfHeight, 0.0f, 0.0f,
                halfWidth, -halfHeight, 1.0f, 0.0f,
                halfWidth, halfHeight, 1.0f, 1.0f,
                -halfWidth, halfHeight, 0.0f, 1.0f
        });

        mTexture = TextureCache.get(Assets.OK_BUTTON);

        MatrixUtils.setIdentity(mModelMatrix);
    }

    @Override
    public void draw(BasicGLScript gls) {
        super.draw(gls);

        mTexture.bind();
        gls.setLighting(1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 0.0f);
        gls.uModel.setValueM4(mModelMatrix);
        gls.drawQuad(mVertexBuffer);
    }
}
