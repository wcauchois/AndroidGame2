package net.cloudhacking.androidgame2.engine;

import android.content.Context;

import net.cloudhacking.androidgame2.engine.gl.*;

import java.nio.FloatBuffer;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TDGameScript extends GLScript {

    public Attribute aXY;  // use to point to coordinates in game space
    public Attribute aUV;  // use to point to coordinates in texture space
    public Uniform uCamera;
    public Uniform uModel;
    public Uniform uTex;
    public Uniform uColorM;
    public Uniform uColorA;


    public TDGameScript() {

        compileGLAssets();

        aXY		= mProgram.getAttribute("aXYZW");
        aUV		= mProgram.getAttribute("aUV");
        uCamera	= mProgram.getUniform("uCamera");
        uModel	= mProgram.getUniform("uModel");
        uTex	= mProgram.getUniform("uTex");
        uColorM	= mProgram.getUniform("uColorM");
        uColorA	= mProgram.getUniform("uColorA");

    }


    public ShaderSrc getShaderSrc() {
        String[] srcString = SHADER.split("//\n");
        ShaderSrc src = new ShaderSrc();
        src.putSource(ShaderType.VERTEX, srcString[0]);
        src.putSource(ShaderType.FRAGMENT, srcString[1]);
        return src;
    }


    private static final String SHADER =
        "uniform mat4 uCamera;" +
        "uniform mat4 uModel;" +
        "attribute vec4 aXYZW;" +
        "attribute vec2 aUV;" +
        "varying vec2 vUV;" +
        "void main() {" +
        "  gl_Position = uCamera * uModel * aXYZW;" +
        "  vUV = aUV;" +
        "}" +

        "//\n" +

        "precision mediump float;" +
        "varying vec2 vUV;" +
        "uniform sampler2D uTex;" +
        "uniform vec4 uColorM;" +
        "uniform vec4 uColorA;" +
        "void main() {" +
        "  gl_FragColor = texture2D( uTex, vUV ) * uColorM + uColorA;" +
        "}";



    public void use() {
        super.use();

        aXY.enable();
        aUV.enable();
    }


    /**
     * Set the vertex attribute pointers for the XY coordinates and UV coordinates attributes
     *
     * @param vertices FloatBuffer containing XY coordinates of vertices and UV coordinates of
     *                 texture vertices.  Each coordinate takes up two positions in the buffer.
     *                 The XY and UV coordinates alternate such that the XY coordinates start
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

}
