package net.cloudhacking.androidgame2.engine.old;

/**
 * Created by wcauchois on 1/4/15.
 */
public interface QuadDrawerOld {
    public void beginDraw();

    /**
     * Set shader uniform to desired texture unit;
     * @param glTextureUnit gl texture unit (i.e. 0 for GL_TEXTURE0)
     */
    public void prepareTexture(int glTextureUnit);

    /**
     * Draw a quadrilateral with a specified texture, position, dimensions, and texture coordinates.
     *
     * @param x X position of the quadrilateral.
     * @param y Y position of the quadrilateral.
     * @param w Width of the quadrilateral.
     * @param h Height of the quadrilateral.
     * @param tx Texture X offset (0.0 for the whole texture).
     * @param ty Texture Y offset (0.0 for the whole texture).
     * @param tw Horizontal amount of the texture to use (1.0 for the whole texture).
     * @param th Vertical amount of the texture to use (1.0 for the whole texture).
     */
    public void draw(float x, float y, float rot, float w, float h, float tx, float ty, float tw, float th);

    public void endDraw();
}
