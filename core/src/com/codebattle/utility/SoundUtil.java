package com.codebattle.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundUtil {

    private static int loopingID = 0;

    public static void playSE(final String name) {
        playSound(GameConstants.SE_DIR + name);
    }

    public static void playBGS(final String name) {
        playSound(GameConstants.BGS_DIR + name).setLooping(loopingID++, true);
    }

    public static void playBGM(final String name) {
        playSound(GameConstants.BGM_DIR + name).setLooping(loopingID, true);
    }

    private static Sound playSound(final String path) {
        final Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sound.play();

        return sound;
    }
}
