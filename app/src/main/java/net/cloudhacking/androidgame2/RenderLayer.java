package net.cloudhacking.androidgame2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 1/5/2015.
 */
public class RenderLayer {

    // Layers will be rendered from back to front in order or the list.
    //  ***Perhaps this should be like a sorted thing where the render layers are comparable and
    //     have a priority attribute.
    //  TODO: Implement render layer comparability
    public static List<RenderLayer> sRenderLayers = new ArrayList<RenderLayer>();

    private SimpleRenderService mRenderService;  // should be generalized render service
    private List<Renderable> mLayerMembers;
    //private int mLayerPriority;


    public RenderLayer(SimpleRenderService renderService) {
        mRenderService = renderService;
        mLayerMembers = new ArrayList<Renderable>();

        sRenderLayers.add(this);
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
