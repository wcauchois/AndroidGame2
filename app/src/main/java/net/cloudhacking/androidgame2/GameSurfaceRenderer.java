package net.cloudhacking.androidgame2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.ConditionVariable;
import android.util.Log;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wcauchois on 1/3/15.
 */
public class GameSurfaceRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = GameSurfaceRenderer.class.getSimpleName();

    private GameState mGameState;
    private GameSurfaceView mSurfaceView;
    private Context mContext;
    private int mViewportWidth;
    private int mViewportHeight;
    private final float[] mProjectionMatrix = new float[16];
    private SimpleRenderService mRenderService;
    private GameLevel mLevel;
    private List<Component> mComponents = new ArrayList<Component>();

    protected <T extends Component> T addComponent(T comp) {
        mComponents.add(comp);
        return comp;
    }

    public GameSurfaceRenderer(GameSurfaceView surfaceView, Context context, GameState gameState) {
        mSurfaceView = surfaceView;
        mGameState = gameState;
        mContext = context;

        mRenderService = addComponent(new SimpleRenderService(
                new SceneInfo() {
                    public float[] getProjection() {
                        return mProjectionMatrix;
                    }

                    public int getViewportWidth() {
                        return mViewportWidth;
                    }

                    public int getViewportHeight() {
                        return mViewportHeight;
                    }
                }
        ));
        mLevel = addComponent(new GameLevel(mRenderService));
    }

    // TODO(wcauchois): Remove unused quad drawing code!
    // How to draw a quad: http://gamedev.stackexchange.com/a/10741
    private FloatBuffer mQuadVertBuffer;
    private static float[] sQuadVertices = new float[] {
            0.0f, 0.0f, 0.0f, // Bottom left
            0.0f, 20.0f, 0.0f, // Top left
            20.0f, 20.0f, 0.0f, // Top right
            20.0f, 0.0f, 0.0f // Bottom right
    };
    private ShortBuffer mQuadIndexBuffer;
    private static short[] sQuadIndices = new short[] {
            0, 1, 2,
            0, 2, 3
    };
    private FloatBuffer mQuadTexCoordBuffer;
    private static float[] sQuadTexCoords = new float[] {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    static int sProgramHandle = -1;
    static int sColorHandle = -1;
    static int sPositionHandle = -1;
    static int sMVPMatrixHandle = -1;
    static int sTextureUniformHandle = -1;
    static int sTexCoordHandle = -1;
    int mTextureHandle = -1;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // All GL state, including shader programs, must be re-generated here.

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        for (Component comp : mComponents) {
            comp.prepareResources(mContext);
        }

        /*ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(sQuadVertices.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        mQuadVertBuffer = vertexByteBuffer.asFloatBuffer();
        mQuadVertBuffer.put(sQuadVertices).position(0);

        ByteBuffer indexByteBuffer = ByteBuffer.allocateDirect(sQuadIndices.length * 4);
        indexByteBuffer.order(ByteOrder.nativeOrder());
        mQuadIndexBuffer = indexByteBuffer.asShortBuffer();
        mQuadIndexBuffer.put(sQuadIndices).position(0);

        ByteBuffer texCoordByteBuffer = ByteBuffer.allocateDirect(sQuadTexCoords.length * 4);
        texCoordByteBuffer.order(ByteOrder.nativeOrder());
        mQuadTexCoordBuffer = texCoordByteBuffer.asFloatBuffer();
        mQuadTexCoordBuffer.put(sQuadTexCoords).position(0);

        sProgramHandle = Util.createProgram(mContext, R.raw.quad_vert, R.raw.quad_frag);

        sPositionHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_position");
        sColorHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_color");
        sMVPMatrixHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_mvpMatrix");
        sTextureUniformHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_texture");
        sTexCoordHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_texCoordinate");
        Util.checkGlError("get uniform/attribute locations");*/

        mTextureHandle = Util.loadTexture(mContext, R.drawable.droid_1);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Should setup viewport here.

        GLES20.glViewport(0, 0, width, height);
        Log.d(TAG, "Viewport width=" + width + ", height=" + height);
        mViewportWidth = width;
        mViewportHeight = height;

        Matrix.orthoM(mProjectionMatrix, 0, 0, GameState.ARENA_WIDTH,
                GameState.ARENA_HEIGHT, 0, -1, 1);
        Matrix.setIdentityM(mModelView, 0);
    }

    private static float[] sTempMVP = new float[16];
    private float[] mModelView = new float[16];

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        mLevel.draw();

        /*
        // Setup vertex shaders for drawing
        GLES20.glUseProgram(sProgramHandle);
        Util.checkGlError("glUseProgram");

        GLES20.glEnableVertexAttribArray(sPositionHandle);
        GLES20.glEnableVertexAttribArray(sTexCoordHandle);

        GLES20.glVertexAttribPointer(sPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mQuadVertBuffer);
        GLES20.glVertexAttribPointer(sTexCoordHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mQuadTexCoordBuffer);

        // DO DRAWING HERE
        float[] mvp = sTempMVP;
        mModelView[12] = 50.0f; // Translation
        mModelView[13] = 50.0f;
        Matrix.multiplyMM(mvp, 0, mProjectionMatrix, 0, mModelView, 0);

        GLES20.glUniformMatrix4fv(sMVPMatrixHandle, 1, false, mvp, 0);
        float[] colorArray = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        GLES20.glUniform4fv(sColorHandle, 1, colorArray, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle);
        GLES20.glUniform1i(sTextureUniformHandle, 0); // Bind to texture unit 0

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, sQuadIndices.length, GLES20.GL_UNSIGNED_SHORT, mQuadIndexBuffer);
        Util.checkGlError("glDrawElements");

        GLES20.glDisableVertexAttribArray(sPositionHandle);
        GLES20.glDisableVertexAttribArray(sTexCoordHandle);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glUseProgram(0);
        */
    }

    public void onViewPause(ConditionVariable syncObj) {
        // Save game state here, use condition var to signal when done.
        syncObj.open();
    }
}
