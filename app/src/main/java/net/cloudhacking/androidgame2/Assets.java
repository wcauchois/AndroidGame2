package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.utils.NinePatchAsset;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

/**
 * Created by Andrew on 1/17/2015.
 */
public class Assets {

    public static final SpriteAsset TEST_TOWER      = new SpriteAsset("tower_animation_test.png", 32, 32);
    public static final SpriteAsset MOTHERSHIP      = new SpriteAsset("mothership.png");
    public static final SpriteAsset TEST_TILESET    = new SpriteAsset("simple_tileset.png", 32, 32);
    public static final SpriteAsset SELECTOR_8PX    = new SpriteAsset("cell_selector_8px.png", 12, 12);
    public static final SpriteAsset SELECTOR_32PX   = new SpriteAsset("cell_selector_32px.png", 36, 36);
    public static final SpriteAsset HIGHLIGHTER_8PX = new SpriteAsset("cell_highlighter_8px.png", 8, 8);
    public static final SpriteAsset EXPLOSIONS      = new SpriteAsset("explosions.png", 32, 32);
    public static final SpriteAsset POKEMON2        = new SpriteAsset("pokemon2.png", 50, 40);
    public static final SpriteAsset MICRO_TILES     = new SpriteAsset("micro_tileset.png", 8, 8);
    public static final SpriteAsset MINI_CHARS      = new SpriteAsset("mini_chars.png", 8, 8);

    public static final NinePatchAsset UI_SIMPLE    = new NinePatchAsset("ui_simple.png", 10, 10, 20, 20);
    public static final NinePatchAsset UI_PURPLE    = new NinePatchAsset("ui_purple.png", 10, 10, 20, 20);
    public static final NinePatchAsset UI_WHITE     = new NinePatchAsset("ui_white.png", 10, 10, 20, 20);

}
