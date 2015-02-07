package net.cloudhacking.androidgame2.engine.utils;

/**
 * Created by Andrew on 2/6/2015.
 */
public class Color {

    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static final Color WHITE = new Color(1,1,1,1);
    public static final Color RED   = new Color(1,0,0,1);
    public static final Color GREEN = new Color(0,1,0,1);
    public static final Color BLUE  = new Color(0,0,1,1);
    public static final Color BLACK = new Color(0,0,0,1);
    public static final Color CLEAR = new Color(0,0,0,0);

    public static Color setAlpha(Color c, float a) {
        return new Color(c.r, c.g, c.b, a);
    }

}
