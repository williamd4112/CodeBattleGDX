package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.codebattle.model.GameObject;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.SoundUtil;

public class OnAttackAnimation extends BaseAnimation {

    final public GameObject obj;
    final public SequenceAction action;

    public OnAttackAnimation(final GameObject obj) {
        this.obj = obj;
        this.action = Actions.sequence(
                Actions.repeat(8,
                        Actions.sequence(Actions.fadeOut(0.02f), Actions.fadeIn(0.02f))),
                Actions.color(Color.WHITE));
    }

    @Override
    public void update(final float delta) {

    }

    @Override
    public void draw(final Batch batch, final Camera camera, final float delta) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void finished() {

    }

    @Override
    public void setup() {
        this.obj.addAction(Actions.color(new Color(Color.RED.r, Color.RED.g, Color.RED.b,
                0.5f)));
        this.obj.addAction(this.action);
        SoundUtil.playSE(GameConstants.INJURE_SE);
    }

}
