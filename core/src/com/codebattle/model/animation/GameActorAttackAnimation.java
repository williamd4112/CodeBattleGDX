package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.units.Direction;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.TextureFactory;

public class GameActorAttackAnimation extends AttackAnimation {

    private final GameActor attacker;

    private Texture texture;
    private float x = 0, time = 0;

    public GameActorAttackAnimation(final GameStage stage, final Attack attack,
            final GameActor attacker,
            final GameObject target) throws Exception {
        super(stage, attack, target);
        this.attacker = attacker;

    }

    @Override
    public void setup() {
        super.setup();
        try {
            this.texture =
                    TextureFactory.getInstance()
                            .loadTextureFromFile(this.attacker.source + "_attack",
                                    ResourceType.ANIMATION);

        } catch (final Exception e) {
            e.printStackTrace();
        }
        this.attacker.setDirection(Direction.HOLD_ATK);
    }

    @Override
    public void update(final float delta) {
        super.update(delta);
    }

    @Override
    public void finished() {
        super.finished();
        this.attacker.setDirection(Direction.HOLD_DEF);
    }

    @Override
    public void draw(final Batch batch, final Camera camera, final float delta) {
        super.draw(batch, camera, delta);
        this.x += Functions.exp6(this.time) * (camera.viewportWidth / this.duration);
        this.time += 0.01f;

        final Vector3 screen = camera.unproject(new Vector3(this.x, camera.viewportHeight / 2
                + this.texture.getHeight() * 0.5f / 2, 0));
        batch.draw(this.texture, screen.x, screen.y, this.texture.getWidth() * 0.5f,
                this.texture.getHeight() * 0.5f);
    }

}
