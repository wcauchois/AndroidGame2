package net.cloudhacking.androidgame2.engine.gl;

/**
 * Created by Andrew on 2/6/2015.
 */
public class GLColor {

    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public GLColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static final GLColor WHITE = new GLColor(1,1,1,1);
    public static final GLColor RED   = new GLColor(1,0,0,1);
    public static final GLColor GREEN = new GLColor(0,1,0,1);
    public static final GLColor BLUE  = new GLColor(0,0,1,1);
    public static final GLColor BLACK = new GLColor(0,0,0,1);
    public static final GLColor TRANSPARENT = new GLColor(0,0,0,0);

    public static GLColor setAlpha(GLColor c, float a) {
        return new GLColor(c.r, c.g, c.b, a);
    }

}
