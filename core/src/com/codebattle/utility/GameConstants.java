package com.codebattle.utility;

public class GameConstants {
	
	/**
	 * Constants of the global game
	 * (be able to modified in config file)
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
	public final static String GAMEACTOR_PORTRAIT_TEXTURE_DIR = "graphics/portrait/";
	public final static String GAMEACTOR_ANIMATION_TEXTURE_DIR = "graphics/animation/";
	public final static String IMAGE_TEXTURE_DIR = "graphics/texture/";
	public final static String GAMEACTOR_PROP_DIR = "actors/";
	public final static String SCENE_DIR = "scene/";
	public final static String SHADER_DIR = "shaders/";
	public final static String BGM_DIR = "audio/BGM/";
	public final static String BGS_DIR = "audio/BGS/";
	public final static String SE_DIR = "audio/SE/";
	
	public final static String DEFAULT_TEXTURE_EXTENSION = ".png";
	public final static String DEFAULT_GAMEACTORDESC_EXTENSION = ".xml";
	
	public final static int OWNER_COUNT = 3;
	
}
