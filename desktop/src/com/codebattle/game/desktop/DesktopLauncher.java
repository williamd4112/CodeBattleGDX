package com.codebattle.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.codebattle.game.CodeBattle;

public class DesktopLauncher {
    public static void main(final String[] arg) {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.vSyncEnabled = true;
        config.title = "CodeBattleGDX";
        
        new LwjglApplication(new CodeBattle(), config);
    }
}
	