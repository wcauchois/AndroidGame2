package net.cloudhacking.androidgame2;

/**
 * Created by wcauchois on 1/4/15.
 */
public interface QuadDrawer {
    public void beginDraw();

    /**
     * Draw a quadrilateral with a specified texture, position, dimensions, and texture coordinates.
     *
     * @param textureID OpenGL ID of the texture to use.
     * @param x X position of the quadrilateral.
     * @param y Y position of the quadrilateral.
     * @param w Width of the quadrilateral.
     * @param h Height of the quadrilateral.
     * @param tx Texture X offset (0.0 for the whole texture).
     * @param ty Texture Y offset (0.0 for the whole texture).
     * @param tw Horizontal amount of the texture to use (1.0 for the whole texture).
     * @param th Vertical amount of the texture to use (1.0 for the whole texture).
     */
    public void draw(int textureID, float x, float y, float w, float h, float tx, float ty, float tw, float th);

    public void endDraw();
}
