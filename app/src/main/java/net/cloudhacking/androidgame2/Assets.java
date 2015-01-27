package net.cloudhacking.androidgame2;

import android.graphics.Rect;

import net.cloudhacking.androidgame2.engine.utils.Asset;
import net.cloudhacking.androidgame2.engine.utils.NinePatchAsset;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

/**
 * Created by Andrew on 1/17/2015.
 */
public class Assets {

    public static final Asset TEST_TOWER_SPRITE     = new Asset("tower_animation_test.png");
    public static final Asset OK_BUTTON             = new Asset("ok_button.png");

    public static final SpriteAsset TEST_TILESET    = new SpriteAsset("simple_tileset.png", 32, 32);
    public static final SpriteAsset SELECTOR_8PX    = new SpriteAsset("cell_selector_8px.png", 12, 12);
    public static final SpriteAsset SELECTOR_32PX   = new SpriteAsset("cell_selector_32px.png", 36, 36);
    public static final SpriteAsset EXPLOSIONS      = new SpriteAsset("explosions.png", 32, 32);
    public static final SpriteAsset CARS            = new SpriteAsset("cars.png", 64, 64);
    public static final SpriteAsset SPACESHIPS      = new SpriteAsset("spaceships.png", 36, 36, 9, 9, 0, 0);
    public static final SpriteAsset POKEMON         = new SpriteAsset("pokemon.png", 64, 64);
    public static final SpriteAsset MICRO_TILES     = new SpriteAsset("micro_tileset.png", 8, 8);

    public static final NinePatchAsset UI_SIMPLE    = new NinePatchAsset("ui_ninepatch.png", new Rect(10, 10, 20, 20) );

}
