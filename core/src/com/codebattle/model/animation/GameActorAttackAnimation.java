package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.GameActorType;
import com.codebattle.model.units.Direction;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.TextureFactory;

public class GameActorAttackAnimation extends AttackAnimation {

    private GameActor attacker;

    // GameActor Portrait
    private Texture texture;

    public GameActorAttackAnimation(GameStage stage, Attack attack, GameActor attacker,
            GameObject target) throws Exception {
        super(stage, attack, target);
        this.attacker = attacker;
    }

    @Override
    public void setup() {
        super.setup();
        try {
            GameActorType type = attacker.type;
            this.texture = TextureFactory.getInstance()
                    .loadTextureFromFile(attacker.source, ResourceType.ANIMATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.attacker.setDirection(Direction.HOLD_ATK);
    }

    @Override
    public void finished() {
        super.finished();
        this.attacker.setDirection(Direction.HOLD_DEF);
    }

    @Override
    public void draw(Batch batch, Camera camera, float delta) {
        super.draw(batch, camera, delta);
        int step = (int) (camera.viewportWidth / this.duration);
        Vector3 screen = camera.unproject(new Vector3(x += step, camera.viewportHeight
                - (camera.viewportHeight - texture.getHeight()) / 2, 0));
        batch.draw(texture, screen.x, screen.y);
    }

}
