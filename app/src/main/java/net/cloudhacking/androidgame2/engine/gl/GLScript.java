package net.cloudhacking.androidgame2.engine.gl;

import android.opengl.GLES20;

import net.cloudhacking.androidgame2.engine.utils.Loggable;

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Created by Andrew on 1/15/2015.
 */
public abstract class GLScript extends Loggable {

    /**
     * Alias for two different openGL shader types
     */
    public enum ShaderType {
        VERTEX      (GLES20.GL_VERTEX_SHADER),
        FRAGMENT    (GLES20.GL_FRAGMENT_SHADER);

        private int mValue;

        private ShaderType(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    /**
     * Contains one vertex shader source and one fragment shader source (could be expanded)
     */
    public static class ShaderSrc {
        private HashMap<ShaderType, String> mStorage;

        public ShaderSrc() {
            mStorage = new HashMap<ShaderType, String>();
        }

        public void putSource(ShaderType type, String src) {
            mStorage.put(type, src);
        }

        public String getSource(ShaderType type) {
            return mStorage.get(type);
        }
    }

    /**
     * Wrapper of openGL shader
     */
    public static class Shader extends Loggable {
        private int mHandle;

        public Shader(ShaderType type) {
            mHandle = GLES20.glCreateShader(type.getValue());
        }

        public int getHandle() {
            return mHandle;
        }

        public void source(String src) {
            GLES20.glShaderSource(mHandle, src);
        }

        public void compile() {
            GLES20.glCompileShader(mHandle);

            int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(mHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] != GLES20.GL_TRUE) {
                e("glCompileShader: " + GLES20.glGetShaderInfoLog(mHandle));
                throw new RuntimeException("glCompileShader failed");
            }
        }

        public void delete() {
            GLES20.glDeleteShader(mHandle);
        }

        public static Shader createCompiled(ShaderType type, String src) {
            Shader shader = new Shader(type);
            shader.source(src);
            shader.compile();
            return shader;
        }
    }

    /**
     * Wrapper of generic shader variable
     */
    private static class ShaderVar {
        protected int mLocation;

        public ShaderVar(int location) {
            mLocation = location;
        }

        public int getLocation() {
            return mLocation;
        }

        public void enable() {
            GLES20.glEnableVertexAttribArray(mLocation);
        }

        public void disable() {
            GLES20.glDisableVertexAttribArray(mLocation);
        }
    }

    /**
     * Wrapper of shader uniform
     */
    public static class Uniform extends ShaderVar {
        public Uniform(int location) {
            super(location);
        }

        public void setValue(int value) {
            GLES20.glUniform1i(mLocation, value);
        }

        public void setValue1f(float value) {
            GLES20.glUniform1f(mLocation, value);
        }

        public void setValue2f(float v1, float v2) {
            GLES20.glUniform2f(mLocation, v1, v2);
        }

        public void setValue4f(float v1, float v2, float v3, float v4) {
            GLES20.glUniform4f(mLocation, v1, v2, v3, v4);
        }

        public void setValueM4(float[] value) {
            GLES20.glUniformMatrix4fv(mLocation, 1, false, value, 0);
        }
    }

    /**
     * Wrapper of shader attribute
     */
    public static class Attribute extends ShaderVar {
        public Attribute(int location) {
            super(location);
        }

        public void vertexAttribPointer(int size, int stride, FloatBuffer fb) {
            GLES20.glVertexAttribPointer(mLocation, size, GLES20.GL_FLOAT, false,
                    stride*(Float.SIZE/8), fb);
        }
    }

    /**
     * Wrapper of openGL program
     */
    public static class Program extends Loggable {
        private int mHandle;

        public Program() {
            mHandle = GLES20.glCreateProgram();
        }

        public int getHandle() {
            return mHandle;
        }

        public void attachShader(Shader shader) {
            GLES20.glAttachShader(mHandle, shader.getHandle());
        }

        public void linkProgram() {
            GLES20.glLinkProgram(mHandle);

            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(mHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] != GLES20.GL_TRUE) {
                e("glLinkProgram: " + GLES20.glGetProgramInfoLog(mHandle));
                throw new RuntimeException("glLinkProgram failed");
            }
        }

        public Attribute getAttribute(String name) {
            return new Attribute(GLES20.glGetAttribLocation(mHandle, name));
        }

        public Uniform getUniform(String name) {
            return new Uniform(GLES20.glGetUniformLocation(mHandle, name));
        }

        public void useProgram() {
            GLES20.glUseProgram(mHandle);
        }

        public void deleteProgram() {
            GLES20.glDeleteProgram(mHandle);
        }
    }


    /**********************************************************************************************/
    protected Program mProgram;


    abstract public ShaderSrc getShaderSrc();

    public GLScript() {
        mProgram = new Program();
    }

    public void compileGLAssets() {
        ShaderSrc src = getShaderSrc();
        mProgram.attachShader(Shader.createCompiled(
                ShaderType.VERTEX, src.getSource(ShaderType.VERTEX)
        ));
        mProgram.attachShader(Shader.createCompiled(
                ShaderType.FRAGMENT, src.getSource(ShaderType.FRAGMENT)
        ));
        mProgram.linkProgram();
    }

    public void use() {
        mProgram.useProgram();
    }

}
