package net.cloudhacking.androidgame2;

import android.graphics.Color;
import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.CameraGroup;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.Scene;
import net.cloudhacking.androidgame2.engine.foundation.TileMap;
import net.cloudhacking.androidgame2.engine.gl.TextRenderer;
import net.cloudhacking.androidgame2.engine.ui.Button;
import net.cloudhacking.androidgame2.engine.ui.Label;
import net.cloudhacking.androidgame2.engine.ui.RootWidget;
import net.cloudhacking.androidgame2.engine.utils.CommonUtils;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Signal;

import java.util.Random;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    // essentials
    private CameraGroup mActiveCameraGroup;
    private CameraGroup mUICameraGroup;
    private Grid mGrid;
    private TileMap mTileMap;

    public Grid getGrid() {
        return mGrid;
    }


    // for testing
    private ExplosionFactory mFXFactory;
    private PokemonFactory mPokeFactory;



    @Override
    public TestScene create() {

        mActiveCameraGroup = new CameraGroup( getActiveCamera() );
        mUICameraGroup = new CameraGroup( getUICamera() );

        add(mActiveCameraGroup);
        add(mUICameraGroup);


        mTileMap = new TileMap( Assets.TEST_TILESET, new JsonMap(Resources.JSON_MAP_SIMPLE) );
        mActiveCameraGroup.add(mTileMap);


        // set up game grid from tile map
        mGrid = new Grid(mTileMap);
        mActiveCameraGroup.add(mGrid);

        mGrid.cellSelector.connect( new Signal.Listener<Grid.Cell>() {
            @Override
            public boolean onSignal(Grid.Cell cell) {
                Grid.SELECTOR_ICON.startAnimationAt(cell.getCenter());
                mActiveCameraGroup.addToFront(Grid.SELECTOR_ICON);
                return false;
            }
        });



        // test explosion factory...
        // spawn an explosion animation on mouse click
        mFXFactory = new ExplosionFactory();
        mActiveCameraGroup.add(mFXFactory);

        getInputManager().clickUp.connect( new Signal.Listener<InputManager.ClickEvent>() {
            @Override
            public boolean onSignal(InputManager.ClickEvent clickEvent) {
                mFXFactory.spawnAt(getActiveCamera().cameraToScene(clickEvent.getPos()));
                return false;
            }
        });



        // this is my favorite... POKEMON FACTORY!!!
        mPokeFactory = new PokemonFactory(mGrid);
        mPokeFactory.start();
        mActiveCameraGroup.add(mPokeFactory);


        // UI

        RootWidget rootWidget = new RootWidget( getInputManager() );
        // example of stretching with rect
        rootWidget.addToFront(new Button(new RectF(0.0f, 0.0f, 300.0f, 100.0f), Assets.OK_BUTTON));

        // lol i sure got u
        String[] strings = new String[] { "such game", "very wow", "much defence", "amaze", "many towers" };
        Integer[] colors = new Integer[] { Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN, Color.LTGRAY };
        Random rand = CommonUtils.getRandom();
        for (int i = 0; i < rand.nextInt(3) + 5; i++) {
            rootWidget.addToFront(Label.create(
                    new PointF(rand.nextFloat() * 400.0f, rand.nextFloat() * 500.0f),
                    CommonUtils.randomChoice(strings),
                    TextRenderer.newProps().textColor(CommonUtils.randomChoice(colors))
            ));
        }

        mUICameraGroup.add(rootWidget);



        //getActiveCamera().setBoundaryRect(mTileMap.getRect());
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
