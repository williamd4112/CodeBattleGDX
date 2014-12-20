package com.codebattle.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ShaderLoader {
	
	public static FileHandle loadFragShader(String name)
	{
		return Gdx.files.internal(GameConstants.SHADER_DIR + name + ".fsh");
	}
	
	public static FileHandle loadVertexShader(String name)
	{
		return Gdx.files.internal(GameConstants.SHADER_DIR + name + ".vsh");
	}
}
