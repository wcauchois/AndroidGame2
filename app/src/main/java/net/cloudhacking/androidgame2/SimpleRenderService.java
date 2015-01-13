package net.cloudhacking.androidgame2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by wcauchois on 1/4/15.
 */
public class SimpleRenderService extends Component {
    private static final String TAG = SimpleRenderService.class.getSimpleName();

    private SceneInfo mSceneInfo;
    private QuadDrawerImpl mQuadDrawer;

    public SimpleRenderService(SceneInfo sceneInfo) {
        mSceneInfo = sceneInfo;
        mQuadDrawer = new QuadDrawerImpl();
    }

    public void prepareResources(Context context) {
        mQuadDrawer.prepareResources(context);
        mResourcesPrepared = true;
    }

    public QuadDrawer getQuadDrawer() {
        return mQuadDrawer;
    }


    // temp storage
    static float[] sTempMVP = new float[16];

    private class QuadDrawerImpl implements QuadDrawer {
        // How to draw a quad: http://gamedev.stackexchange.com/a/10741

        FloatBuffer mVertBuffer;
        ShortBuffer mIndexBuffer;

        // Resource handles
        int mProgramHandle = -1;
        int mPositionHandle = -1;
        int mMVPMatrixHandle = -1;
        int mTextureUniformHandle = -1;
        int mTexCoordHandle = -1;

        void prepareResources(Context context) {
            mVertBuffer = RenderUtils.makeFloatBuffer(new float[]{
                    -0.5f, -0.5f, 0.0f, // Bottom left
                    -0.5f, +0.5f, 0.0f, // Top left
                    +0.5f, +0.5f, 0.0f, // Top right
                    +0.5f, -0.5f, 0.0f // Bottom right
            });

            // Note for whatever weird reason index buffers appear to only work as shorts, not ints.
            mIndexBuffer = RenderUtils.makeShortBuffer(new short[]{
                    0, 1, 2,
                    0, 2, 3
            });

            mProgramHandle = RenderUtils.createProgram(context, R.raw.quad_vert, R.raw.quad_frag);

            mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_position");
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_mvpMatrix");
            mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_texture");
            mTexCoordHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_texCoordinate");
            RenderUtils.checkGlError("get uniform/attribute locations");
        }

        public void beginDraw() {
            checkResourcesPrepared(TAG);

            GLES20.glUseProgram(mProgramHandle);
            RenderUtils.checkGlError("glUseProgram");

            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glEnableVertexAttribArray(mTexCoordHandle);

            GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertBuffer);
        }

        public void prepareTexture(int glTextureUnit) {
            // activate texture by textureID and pass to shader program
            GLES20.glUniform1i(mTextureUniformHandle, glTextureUnit);
        }

        public void draw(float x, float y, float rot, float w, float h, float tx, float ty, float tw, float th) {

            // setup texture coordinates buffer
            FloatBuffer texCoordBuffer = RenderUtils.makeFloatBuffer(new float[]{
                    tx, ty,
                    tx, ty + th,
                    tx + tw, ty + th,
                    tx + tw, ty
            });
            GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, texCoordBuffer);

            // setup MVP matrix
            float[] mvp = sTempMVP;
            Matrix.setIdentityM(mvp, 0);
            Matrix.translateM(mvp, 0, x, y, 0.0f);
            Matrix.scaleM(mvp, 0, w, h, 0.0f);
            Matrix.rotateM(mvp, 0, rot, 0.0f, 0.0f, 1.0f);
            Matrix.multiplyMM(mvp, 0, mSceneInfo.getProjectionCamera(), 0, mvp, 0);

            // pass MVP matrix to shader program
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvp, 0);

            // draw quad
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6 /* number of indices */, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
            RenderUtils.checkGlError("glDrawElements");
        }


        public void endDraw() {
            GLES20.glDisableVertexAttribArray(mPositionHandle);
            GLES20.glDisableVertexAttribArray(mTexCoordHandle);
            //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            //GLES20.glUseProgram(0);
        }
    }
}
