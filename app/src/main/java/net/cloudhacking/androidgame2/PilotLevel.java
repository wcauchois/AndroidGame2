package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.element.Animated;
import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.element.TileMap;
import net.cloudhacking.androidgame2.engine.element.shape.Circle;
import net.cloudhacking.androidgame2.engine.element.shape.Line;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.TiledImporter;
import net.cloudhacking.androidgame2.engine.utils.Vec2;
import net.cloudhacking.androidgame2.unit.ControllableUnit;
import net.cloudhacking.androidgame2.unit.UnitController;

/**
 * Created by research on 1/29/15.
 */
public class PilotLevel extends Level {

    private UnitController mUnitController;
    private ControllableUnit mMothership, mChar;

    private PointF cam2scene(PointF camPt) {
        return getScene().getActiveCamera().cameraToScene(camPt);
    }

    @Override
    public void create() {

        InputManager inputManager = getScene().getInputManager();

        // import background tile data from json map exported from Tiled
        TiledImporter.TiledObject imported
                = TiledImporter.loadMaps(Resources.BIG_MAP);

        // create tile maps
        TileMap mTileMapL1 = new TileMap( Assets.MICRO_TILES, imported.getLayers().get(0) );
        TileMap mTileMapL2 = new TileMap( Assets.MICRO_TILES, imported.getLayers().get(1) );

        // pre-render tile maps and add to level
        Image tileMapWater = new Image( mTileMapL1.getPreRendered() );
        tileMapWater.moveToOrigin();
        tileMapWater.update();
        tileMapWater.setInactive();
        add(tileMapWater);

        Image tileMapTerrain = new Image( mTileMapL2.getPreRendered() );
        tileMapTerrain.moveToOrigin();
        tileMapTerrain.update();
        tileMapTerrain.setInactive();
        add(tileMapTerrain);

        size = mTileMapL1.getRect();


        // set up game grid from tile map size; set collision map
        grid = (Grid) add(Grid.createFromTileMap(mTileMapL1));

        // map collision map to occupied
        grid.mapToState(imported.getCollisionMap(), Grid.CellState.EMPTY, Grid.CellState.OCCUPIED);


        mUnitController = new UnitController(this);
        inputManager.click.connect(mUnitController);
        inputManager.drag.connect(mUnitController);
        add(mUnitController);

        mMothership = new ControllableUnit(Assets.MOTHERSHIP);
        mMothership.moveToCell( grid.getCell(50,50) );
        mMothership.setAlpha(0.5f);
        mMothership.setVelocity(new Vec2(0, 10));
        mUnitController.add(mMothership);

        mChar = new ControllableUnit(Assets.MINI_CHARS);
        mChar.queueAnimation(new Animated.Animation() {
            public void start() {}
            public void reset() {}
            public void update() {}

            @Override
            public boolean isAnimating() {
                return true;
            }

            @Override
            public int getCurrentFrameIndex() {
                return 102;
            }

        }, true, false);
        mChar.moveToCell( grid.getCell(50,30) );
        mUnitController.add(mChar);


        /*float[] red = new float[] {1,0,0,1};

        final Circle circle = new Circle(40, 5, red);
        circle.setVisibility(false);
        addToFront(circle);

        final Line line = new Line(new PointF(), new PointF(), 5, red);
        line.setVisibility(false);
        addToFront(line);

        inputManager.drag.connect(0, new Signal.Listener<InputManager.DragEvent>() {
            @Override
            public boolean onSignal(InputManager.DragEvent dragEvent) {
                switch (dragEvent.getType()) {
                    case UPDATE:
                        line.setVisibility(true);
                        circle.setVisibility(true);
                        line.setEndPoints(cam2scene(dragEvent.getOrigin()),
                                          cam2scene(dragEvent.getPos())
                        );
                        circle.setPos(cam2scene(dragEvent.getPos()));
                        break;
                    case END:
                        line.setVisibility(false);
                        circle.setVisibility(false);
                        break;
                }
                return false;
            }
        });*/



    }

}
