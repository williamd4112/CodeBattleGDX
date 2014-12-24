package com.codebattle.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderLoader {

    public static FileHandle loadFragShader(final String name) {
        return Gdx.files.internal(GameConstants.SHADER_DIR + name + ".fsh");
    }

    public static FileHandle loadVertexShader(final String name) {
        return Gdx.files.internal(GameConstants.SHADER_DIR + name + ".vsh");
    }

    public static ShaderProgram loadShader(final String name) {
        return new ShaderProgram(ShaderLoader.loadVertexShader(name),
                ShaderLoader.loadFragShader(name));
    }
}
