package net.cloudhacking.androidgame2.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Andrew on 1/5/2015.
 */
public class RenderLayer implements Comparable<RenderLayer> {
    private static final String TAG = RenderLayer.class.getSimpleName();

    /* This is a static set which contains all the render layers.  Layers will be rendered in
     * increasing order of layer priority.
     */
    public static TreeSet<RenderLayer> sRenderLayers = new TreeSet<RenderLayer>();

    public static TreeSet<RenderLayer> getsRenderLayers() {
        return sRenderLayers;
    }

    public static void clearRenderLayers() {
        sRenderLayers.clear();
    }


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

    public List<Renderable> getLayerMembers() {
        return mLayerMembers;
    }


    public void draw() {
        QuadDrawerOld quadDrawer = mRenderService.getQuadDrawer();
        quadDrawer.beginDraw();
        for (Renderable member : mLayerMembers) {
            member.draw(quadDrawer);
        }
        quadDrawer.endDraw();
    }

}
