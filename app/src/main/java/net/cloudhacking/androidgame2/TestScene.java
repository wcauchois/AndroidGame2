package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.CameraGroup;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.Scene;
import net.cloudhacking.androidgame2.engine.foundation.TileMap;
import net.cloudhacking.androidgame2.engine.fx.ExplosionFactory;
import net.cloudhacking.androidgame2.engine.fx.PokemonFactory;
import net.cloudhacking.androidgame2.engine.ui.Button;
import net.cloudhacking.androidgame2.engine.ui.RootWidget;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;
import net.cloudhacking.androidgame2.engine.utils.Signal;

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


        mTileMap = new TileMap(
                Assets.TEST_TILESET, new JsonMap(Resources.JSON_MAP_SIMPLE), 32, 32
        );
        mActiveCameraGroup.add(mTileMap);


        mGrid = new Grid(mTileMap);

        mGrid.addSelectorListener(new Grid.CellSelectorListener() {
            @Override
            public void onCellSelect(Grid.Cell selected) {
                Grid.SELECTOR_ICON.startAnimationAt(selected.getCenter());
                mActiveCameraGroup.addToFront(Grid.SELECTOR_ICON);
            }
        });

        mActiveCameraGroup.add(mGrid);


        // test explosion factory
        mFXFactory = new ExplosionFactory();

        getInputManager().clickUp.connect( new Signal.Listener<InputManager.ClickEvent>() {
            @Override
            public boolean onSignal(InputManager.ClickEvent clickEvent) {
                mFXFactory.spawnAt( getActiveCamera().cameraToScene(clickEvent.getPos()) );
                return false;
            }
        });
        mActiveCameraGroup.add(mFXFactory);


        // this is my favorite... POKEMON FACTORY!!!
        mPokeFactory = new PokemonFactory(mGrid);
        mPokeFactory.start();
        mActiveCameraGroup.add(mPokeFactory);


        // UI

        RootWidget rootWidget = new RootWidget( getInputManager() );
        // example of stretching with rect
        rootWidget.addToFront(new Button(new RectF(0.0f, 0.0f, 500.0f, 200.0f), Assets.OK_BUTTON));
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
