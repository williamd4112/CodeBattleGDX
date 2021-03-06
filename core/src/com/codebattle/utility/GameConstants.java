package com.codebattle.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.codebattle.model.meta.Animation;
import com.codebattle.model.meta.PointLightMeta;
import com.codebattle.model.meta.Region;
import com.codebattle.model.units.Interval;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameConstants {

    /**
     * Constants of the global game
     */
    public final static int FRAMERATE = 25;
    public final static int CELL_SIZE = 32;
    public final static int CHR_VSLICES = 10;
    public final static int CHR_HSLICES = 4;
    public final static int CHR_WIDTH = 32;
    public final static int CHR_HEIGHT = 32;
    public final static int ANI_CYCLE = 3;

    public final static int CAMERA_SPEED = 10;
    public final static int CAMERA_SLIDING_MARGIN = 25;
    public final static int CAMERA_UP = 0;
    public final static int CAMERA_DOWN = 1;
    public final static int CAMERA_LEFT = 2;
    public final static int CAMERA_RIGHT = 3;
    public final static int CAMERA_HOLD = -1;

    public final static String GAMEACTOR_MAP_TEXTURE_DIR = "graphics/characters/";
    public final static String LEVELOBJECT_TEXTURE_DIR = "graphics/levelobjects/";
    public final static String GAMEACTOR_PORTRAIT_TEXTURE_DIR = "graphics/portrait/";
    public final static String GAMEACTOR_ANIMATION_TEXTURE_DIR = "graphics/animation/";
    public final static String IMAGE_TEXTURE_DIR = "graphics/picture/";
    public final static String GUI_TEXTURE_DIR = "graphics/gui/";
    public final static String GAMEACTOR_PROP_DIR = "gameobjects/";
    public final static String LEVEL_PROP_DIR = "levelobjects/";
    public final static String API_DIR = "apis/";
    public final static String SCENE_DIR = "scene/";
    public final static String SHADER_DIR = "shaders/";
    public final static String BGM_DIR = "audio/BGM/";
    public final static String BGS_DIR = "audio/BGS/";
    public final static String SE_DIR = "audio/SE/";
    public final static String DEFAULT_META_DIR = "default/";
    public final static String EDITOR_KEYWORD_REF = "editor/keywords.xml";
    public final static String EDITOR_COLOR_REF = "editor/color.xml";

    public final static String DEFAULT_TEXTURE_EXTENSION = ".png";
    public final static String DEFAULT_GAMEACTORDESC_EXTENSION = ".xml";

    public final static int OWNER_COUNT = 3;

    public final static int OBJECT_CURSOR_OFFSET = 16;
    public final static String ONSELECT_CURSOR = "001-Blue01";
    public final static Region ONSELECT_CURSOR_REGION = new Region(160, 64, 32, 32, 16, 16);
    public final static Interval ONSELECT_CURSOR_INTERVAL = Interval.VERYHIGH;
    public final static String ONSELECT_SE = "002-System02.ogg";

    public final static int INIT_HP = 1500;
    public final static int INIT_RES = 300;
    public final static int WRITE_COST = 40;
    public final static int VIRTUAL_SYSTEM_RESOURCE_ADDING = 20;
    public final static int WRITE_LEVEL = 2;
    public final static int HEAL_LEVEL = 2;

    public final static String SKILL_EFFECT_ANIM = "effect";
    public final static int SKILL_EFFECT_COUNT = 6;

    public final static Skin DEFAULT_SKIN = new Skin(
            Gdx.files.internal("skin/demo/uiskin.json"));

    public static float SUMMON_TARGETBASEANIMATION_SCALE = 0.3f;
    public static Animation SUMMON_ANIMMETA;
    public static PointLightMeta SUMMON_LIGHTMETA;
    public static String SUMMON_SE = "summon.ogg";

    public static Animation HEAL_ANIMMETA;
    public static String HEAL_SE = "105-Heal01.ogg";

    public static Animation WRITE_ANIMMETA;
    public static String WRITE_SE = "154-Support12.ogg";

    public static String INJURE_SE = "injure.mp3";

    public static Map<String, String> API_GAMEACTOR = new HashMap<String, String>();
    public static Map<String, String> AVAILABLE_GAMEACTORS = new HashMap<String, String>();

    public static void init() {
        try {
            API_GAMEACTOR = XMLUtil.readAPIFromFile(API_DIR + "GameActor.xml");
            AVAILABLE_GAMEACTORS = XMLUtil.readAPIFromFile(API_DIR + "AvailableActor.xml");
            SUMMON_ANIMMETA = new Animation(XMLUtil.readXMLFromFile(
                    DEFAULT_META_DIR + "summon_animation.xml").getChildByName("animation"));
            SUMMON_LIGHTMETA = new PointLightMeta(XMLUtil.readXMLFromFile(
                    DEFAULT_META_DIR + "summon_pointlight.xml").getChildByName("pointlight"));
            HEAL_ANIMMETA = new Animation(XMLUtil.readXMLFromFile(
                    DEFAULT_META_DIR + "heal_animation.xml").getChildByName("animation"));
            WRITE_ANIMMETA = new Animation(XMLUtil.readXMLFromFile(
                    DEFAULT_META_DIR + "write_animation.xml").getChildByName("animation"));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
