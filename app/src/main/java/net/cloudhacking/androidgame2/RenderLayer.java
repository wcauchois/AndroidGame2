package net.cloudhacking.androidgame2;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Andrew on 1/5/2015.
 */
public class RenderLayer implements Comparable<RenderLayer> {

    /* This is a static set which contains all the render layers.  Layers will be rendered in
     * increasing order of layer priority.
     */
    public static TreeSet<RenderLayer> sRenderLayers = new TreeSet<RenderLayer>();


    private SimpleRenderService mRenderService;  // should be generalized render service
    private List<Renderable> mLayerMembers;
    private int mLayerPriority;


    public RenderLayer(SimpleRenderService renderService, int layerPriority) {
        mRenderService = renderService;
        mLayerPriority = layerPriority;
        mLayerMembers = new ArrayList<Renderable>();

        sRenderLayers.add(this);
    }

    // implement Comparable interface...
    public int getLayerPriority() {return mLayerPriority;}

    @Override
    public int compareTo(RenderLayer other) {
        // A lower render layer priority corresponds to being drawn further in the background.
        return (mLayerPriority - other.getLayerPriority());
    }


    public <T extends Renderable> T addMember(T member) {
        mLayerMembers.add(member);
        return member;
    }


    public void draw() {
        QuadDrawer quadDrawer = mRenderService.getQuadDrawer();
        quadDrawer.beginDraw();
        for (Renderable member : mLayerMembers) {
            member.draw(quadDrawer);
        }
        quadDrawer.endDraw();
    }

}
