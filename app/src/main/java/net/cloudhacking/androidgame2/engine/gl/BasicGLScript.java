package net.cloudhacking.androidgame2.engine.gl;

import java.nio.FloatBuffer;

/**
 * Created by Andrew on 1/15/2015.
 */
public class BasicGLScript extends GLScript {

    /**
     * This is the basic openGL program that we will use, shamelessly stolen from Pixel Dungeon.
     * Since its only meant for 2D applications its vertex shader is simple with just a camera
     * matrix and a model matrix to transform the vertices.  The fragment shader is versatile with
     * a multiplicative color and an additive color.  For example if you just wanted to set the
     * whole texture to a certain color, you would assign that color to uColorA and then set
     * uColorM to {0, 0, 0, 0}.
     *
     * Note: This class assumes that the scene coordinates and texture coordinates are packaged
     *       into a single vertex buffer (see the Image class for how this is set up).
     *
     * Call drawQuad(vertexBuffer) or drawQuadSet(vertexBuffer, quadCount) in order to actually
     * draw quads on the screen, but make sure all the proper uniforms are set first.
     *
     */

    private Camera mLastCamera;

    public Attribute aXY;  // use to point to coordinates in game space
    public Attribute aUV;  // use to point to coordinates in texture space
    public Uniform uCamera;
    public Uniform uModel;
    public Uniform uTex;
    public Uniform uColorM;
    public Uniform uColorA;


    public BasicGLScript() {
        compileGLAssets();

        aXY	    = mProgram.getAttribute("aXYZW");
        aUV     = mProgram.getAttribute("aUV");
        uCamera	= mProgram.getUniform("uCamera");
        uModel	= mProgram.getUniform("uModel");
        uTex	= mProgram.getUniform("uTex");
        uColorM	= mProgram.getUniform("uColorM");
        uColorA	= mProgram.getUniform("uColorA");

        d("OpenGL program successfully compiled");

        mLastCamera = null;
    }


    public void use() {
        super.use();
        aXY.enable();
        aUV.enable();
    }


    public ShaderSrc getShaderSrc() {
        ShaderSrc src = new ShaderSrc();
        src.putSource(ShaderType.VERTEX, VERTEX_SHADER);
        src.putSource(ShaderType.FRAGMENT, FRAGMENT_SHADER);
        return src;
    }


    private static final String VERTEX_SHADER =
        "uniform mat4 uCamera;" +
        "uniform mat4 uModel;" +
        "attribute vec4 aXYZW;" +
        "attribute vec2 aUV;" +
        "varying vec2 vUV;" +
        "void main() {" +
        "  gl_Position = uCamera * uModel * aXYZW;" +
        "  vUV = aUV;" +
        "}";

    private static final String FRAGMENT_SHADER =
        "precision mediump float;" +
        "varying vec2 vUV;" +
        "uniform sampler2D uTex;" +
        "uniform vec4 uColorM;" +
        "uniform vec4 uColorA;" +
        "void main() {" +
        "  gl_FragColor = texture2D( uTex, vUV ) * uColorM + uColorA;" +
        "}";



    /**
     * Set camera
     */
    public void useCamera(Camera camera) {
        if (camera == null) {
            return;
        } else if (camera != mLastCamera) {
            mLastCamera = camera;
            uCamera.setValueM4(camera.getMatrix());
        }
    }

    public void clearLastCamera() {
        mLastCamera = null;
    }


    /**
     * Set lighting uniforms
     */
    public void setLighting(float[] vm, float[] va) {
        setLighting(vm[0], vm[1], vm[2], vm[3], va[0], va[1], va[2], va[3]);
    }

    public void setLighting(float rm, float gm, float bm, float am,
                            float ra, float ga, float ba, float aa)
    {
        uColorM.setValue4f(rm, gm, bm, am);
        uColorA.setValue4f(ra, ga, ba, aa);
    }


    //----------------------------------------------------------------------------------------------
    // Quad Drawing


    /**
     * Set the vertex attribute pointers for the XY coordinates and UV coordinates attributes
     *
     * @param vertices FloatBuffer containing XY coordinates of vertices and UV coordinates of
     *                 texture vertices.  Each coordinate takes up two positions in the buffer.
     *                 The XY and UV coordinates interleave such that the XY coordinates start
     *                 at the zero-th position and occur every 4 positions, and the UV coordinates
     *                 start at the second position and also occur every 4 positions.
     */
    public void setVertexAttribPointers(FloatBuffer vertices) {
        vertices.position(0);
        aXY.vertexAttribPointer(2, 4, vertices);

        vertices.position(2);
        aUV.vertexAttribPointer(2, 4, vertices);
    }


    /**
     * Draw a single quad
     */
    public void drawQuad(FloatBuffer vertices) {
        setVertexAttribPointers(vertices);
        QuadDrawer.drawQuad();
    }


    /**
     * Draw multiple quads
     *
     * @param quadCount number of quads for which there are vertices in the vertex buffer
     */
    public void drawQuadSet(FloatBuffer vertices, int quadCount) {
        setVertexAttribPointers(vertices);
        QuadDrawer.drawQuadSet(quadCount);
    }


    //----------------------------------------------------------------------------------------------
    // Array Drawing


    private void setArrayPointers(FloatBuffer vertices) {
        vertices.position(0);
        aXY.vertexAttribPointer(2, 2, vertices);
        aUV.vertexAttribPointer(2, 2, vertices);
    }

    /**
     * Draw lines
     */
    public void drawLines(FloatBuffer vertices, int offset, int vertexCount) {
        setArrayPointers(vertices);
        ArrayDrawer.drawLines(offset, vertexCount);
    }

    /**
     * Draw line loop
     */
    public void drawLineLoop(FloatBuffer vertices, int offset, int vertexCount) {
        setArrayPointers(vertices);
        ArrayDrawer.drawLineLoop(offset, vertexCount);
    }

    /**
     * Draw a filled convex shape
     */
    public void drawConvexFilled(FloatBuffer vertices, int offset, int vertexCount) {
        setArrayPointers(vertices);
        ArrayDrawer.drawConvexFilled(offset, vertexCount);
    }

    /**
     * Draw triangle strip
     */
    public void drawTriangleStrip(FloatBuffer vertices, int offset, int vertexCount) {
        setArrayPointers(vertices);
        ArrayDrawer.drawTriangleStrip(offset, vertexCount);
    }
}
