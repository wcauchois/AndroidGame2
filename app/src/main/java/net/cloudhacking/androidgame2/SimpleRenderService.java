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
    private SceneInfo mSceneInfo;
    private QuadDrawerImpl mQuadDrawer;

    public SimpleRenderService(SceneInfo sceneInfo) {
        mSceneInfo = sceneInfo;
        mQuadDrawer = new QuadDrawerImpl();

    }

    public void prepareResources(Context context) {
        mQuadDrawer.prepareResources(context);
    }

    public QuadDrawer getQuadDrawer() {
        return mQuadDrawer;
    }

    static float[] sTempMVP = new float[16];

    private class QuadDrawerImpl implements QuadDrawer {
        // How to draw a quad: http://gamedev.stackexchange.com/a/10741

        FloatBuffer mVertBuffer;
        ShortBuffer mIndexBuffer;

        // Resource handles
        int mProgramHandle = -1;
        int mColorHandle = -1;
        int mPositionHandle = -1;
        int mMVPMatrixHandle = -1;
        int mTextureUniformHandle = -1;
        int mTexCoordHandle = -1;

        void prepareResources(Context context) {
            mVertBuffer = Util.makeFloatBuffer(new float[] {
                    0.0f, 0.0f, 0.0f, // Bottom left
                    0.0f, 1.0f, 0.0f, // Top left
                    1.0f, 1.0f, 0.0f, // Top right
                    1.0f, 0.0f, 0.0f // Bottom right
            });

            // Note for whatever weird reason index buffers appear to only work as shorts, not ints.
            mIndexBuffer = Util.makeShortBuffer(new short[]{
                    0, 1, 2,
                    0, 2, 3
            });

            mProgramHandle = Util.createProgram(context, R.raw.quad_vert, R.raw.quad_frag);

            mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_position");
            mColorHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_color");
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_mvpMatrix");
            mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_texture");
            mTexCoordHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_texCoordinate");
            Util.checkGlError("get uniform/attribute locations");
        }

        public void beginDraw() {
            GLES20.glUseProgram(mProgramHandle);
            Util.checkGlError("glUseProgram");

            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glEnableVertexAttribArray(mTexCoordHandle);

            GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertBuffer);
        }

        public void draw(int textureID, float x, float y, float w, float h, float tx, float ty, float tw, float th) {
            // FIXME(wcauchois): The texture coordinates don't work! Either that or TileSet is broken.
            FloatBuffer texCoordBuffer = Util.makeFloatBuffer(new float[] {
                    tx,      ty,
                    tx,      ty + th,
                    tx + tw, ty + th,
                    tx + tw, ty
            });

            GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, texCoordBuffer);

            float[] mvp = sTempMVP;
            Matrix.setIdentityM(mvp, 0);

            MatrixUtil.setTranslation(mvp, x, y);
            MatrixUtil.setScale(mvp, w, h);

            Matrix.multiplyMM(mvp, 0, mSceneInfo.getProjection(), 0, mvp, 0);

            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvp, 0);
            float[] colorArray = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
            GLES20.glUniform4fv(mColorHandle, 1, colorArray, 0);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
            GLES20.glUniform1i(mTextureUniformHandle, 0); // Bind to texture unit 0

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6 /* number of indices */, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
            Util.checkGlError("glDrawElements");
        }

        public void endDraw() {
            GLES20.glDisableVertexAttribArray(mPositionHandle);
            GLES20.glDisableVertexAttribArray(mTexCoordHandle);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glUseProgram(0);
        }
    }
}
