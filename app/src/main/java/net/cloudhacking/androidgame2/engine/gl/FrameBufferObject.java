package net.cloudhacking.androidgame2.engine.gl;

import android.graphics.Rect;
import android.opengl.GLES20;

import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.utils.Loggable;

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
    private float[] mCameraMatrix;
    private boolean mActive;

    public FrameBufferObject() {
        mHandle = genNewHandle();
        mCameraMatrix = null;
        mActive = false;
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

    public void delete() {
        GLES20.glDeleteFramebuffers(1, new int[] {mHandle}, 0);
    }

    public boolean checkStatus() {
        bind();
        return GLES20.glCheckFramebufferStatus( GLES20.GL_FRAMEBUFFER )
                == GLES20.GL_FRAMEBUFFER_COMPLETE;
    }

    public boolean isActive() {
        return mActive;
    }


    public void setClearColor(GLColor c) {
        GLES20.glClearColor(c.r, c.g, c.b, c.a);
    }

    public void clear() {
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT );
    }

    public void clear(GLColor c) {
        setClearColor(c);
        clear();
    }


    public boolean start(PreRenderTexture fboTex) {
        // returns true for a successful start, otherwise false.

        // bind this framebuffer and then bind the newly created texture handle to it
        bind();
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,
                fboTex.getHandle(),
                0
        );

        // if something went wrong just return false
        if (!checkStatus()) return false;

        int w = fboTex.getWidth();
        int h = fboTex.getHeight();

        // set the gl viewport to the width and height of the new texture
        GLES20.glViewport(0, 0, w, h);

        // set camera matrix to scale vertices to the [-1,1]X[-1,1] range.
        mCameraMatrix = new float[]{2f/w,    0, 0, 0,
                                       0, 2f/h, 0, 0,
                                       0,    0, 1, 0,
                                      -1,   -1, 0, 1};

        GameSkeleton.getGLScript().uCamera.setValueM4(mCameraMatrix);

        clear(GLColor.TRANSPARENT);
        mActive = true;
        return true;
    }


    public void end() {
        // unbind this framebuffer and reset the viewport and clear color
        unbind();
        mActive = false;

        Rect deviceViewport = GameSkeleton.getViewport();
        if (deviceViewport != null) {
            GLES20.glViewport(0, 0, deviceViewport.width(), deviceViewport.height());
        }
        GLES20.glClearColor(0, 0, 0, 1);  // reset to black
    }


}
