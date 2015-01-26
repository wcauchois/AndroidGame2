package net.cloudhacking.androidgame2.engine.gl;

import android.graphics.Rect;
import android.opengl.GLES20;

import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.MatrixUtils;

import java.nio.FloatBuffer;

/**
 * Created by Andrew on 1/22/2015.
 */
public class FrameBufferObject extends Loggable {

    /**
     * This is a wrapper for the GL frame buffer object.  However right now this class is
     * really just meant to pre-render tile maps as opposed to being an off-screen
     * renderer.
     */

    private int mHandle;

    public FrameBufferObject() {
        mHandle = genNewHandle();
    }

    public static int genNewHandle() {
        int[] handles = new int[1];
        GLES20.glGenFramebuffers(1, handles, 0);
        return handles[0];
    }

    public void bind() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mHandle);
    }

    public void unbind() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public boolean checkStatus() {
        bind();
        return GLES20.glCheckFramebufferStatus( GLES20.GL_FRAMEBUFFER )
                == GLES20.GL_FRAMEBUFFER_COMPLETE;
    }


    /**
     * Caution: this is an expensive function and should probably not be called
     * during the game loop.
     *
     * @param gls           -instance of GLScript for drawing quads
     * @param w             -width of new texture corresponding to vertices
     * @param h             -height of new texture corresponding to vertices
     * @param centered      -whether or not the vertices are centered around the origin,
     *                       as opposed to if the top-left vertex is on the origin
     * @param tex           -texture to be used for rendering
     * @param vbo           -vertex buffer containing screen vertices and uv vertices
     * @param quadCount     -number of quads in vertex buffer
     * @return              -returns instance of Texture.
     */
    public PreRenderedTexture renderToTexture(BasicGLScript gls, int w, int h, boolean centered,
                                              Texture tex, FloatBuffer vbo, int quadCount)
    {

        // initialize empty texture
        PreRenderedTexture fboTexture = new PreRenderedTexture(w, h);
        fboTexture.setFilter(Texture.FilterType.NEAREST, Texture.FilterType.NEAREST);
        fboTexture.setWrap(Texture.WrapType.CLAMP, Texture.WrapType.CLAMP);

        // let GL know the size of the texture and the data types
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
                w, h, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        // bind this framebuffer and then bind the newly created texture handle to it
        bind();
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,
                fboTexture.getHandle(),
                0
        );

        // if something went wrong just return null
        if (!checkStatus()) return null;


        // bind the texture to be drawn to the frame buffer
        tex.bind();

        // set clear color to be completely transparent
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT );

        // store the touch screen's viewport and temporarily set the gl viewport
        // to the width and height of the new texture
        Rect oldViewport = GameSkeleton.getInstance().getViewport();
        GLES20.glViewport(0, 0, w, h);

        // set camera matrix to scale vertices to the [-1,1]X[-1,1] range.
        // (This matrix actually looks transposed because GL matrices are column major.)
        float[] cam;

        if (centered) {
            cam = new float[]{2f/w,    0, 0, 0,
                                 0, 2f/h, 0, 0,
                                 0,    0, 1, 0,
                                -1,   -1, 0, 1};
        } else {
            cam = new float[]{2f/w,    0, 0, 0,
                                 0, 2f/h, 0, 0,
                                 0,    0, 1, 0,
                                 0,    0, 0, 1};
        }

        gls.uCamera.setValueM4(cam);

        // model matrix isn't needed; set to identity
        gls.uModel.setValueM4(MatrixUtils.IDENTITY);

        gls.setLighting(new float[]{1, 1, 1, 1}, new float[]{0, 0, 0, 0});

        // draw quads using the given vertex buffer
        gls.drawQuadSet(vbo, quadCount);

        // unbind this framebuffer and reset the viewport and clear color
        unbind();
        if (oldViewport != null) {
            GLES20.glViewport(0, 0, oldViewport.width(), oldViewport.height());
        }
        GLES20.glClearColor(0, 0, 0, 1);

        return fboTexture;
    }

}
