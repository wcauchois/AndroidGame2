package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.Scene;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    private TestLevel mLevel;
    private TestUI mUI;


    @Override
    public TestScene create() {

        mLevel = new TestLevel();
        mUI = new TestUI();

        add(mLevel);
        add(mUI);

        mLevel.create();
        mUI.create();

        // vvv keep for reference vvv

        // lol i sure got u
        /*String[] strings = new String[] { "such game", "very wow", "much defence", "amaze", "many towers" };
        Integer[] colors = new Integer[] { Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN, Color.LTGRAY };
        Random rand = CommonUtils.getRandom();
        for (int i = 0; i < rand.nextInt(3) + 5; i++) {
            rootWidget.addToFront(Label.create(
                    new PointF(rand.nextFloat(), rand.nextFloat()),
                    CommonUtils.randomChoice(strings),
                    TextRenderer.newProps().textColor(CommonUtils.randomChoice(colors))
            ));
        }*/

        getActiveCamera().setBoundaryRect(mLevel.getSize());
        return this;
    }

    @Override
    public void destroy() {

    }


    @Override
    public void update() {

        super.update();
    }

    @Override
    public void draw(BasicGLScript gls) {

        super.draw(gls);
    }

}
