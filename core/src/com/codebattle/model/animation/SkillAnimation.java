package com.codebattle.model.animation;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.meta.Skill;
import com.codebattle.model.units.Direction;
import com.codebattle.utility.SoundUtil;

public class SkillAnimation extends TargetBasedAnimation {

    private Skill skill;
    private GameObject emitter;

    public SkillAnimation(GameStage stage, Skill skill, GameObject emitter, GameObject target)
            throws Exception {
        super(stage, skill.animMeta, target);
        this.skill = skill;
        this.emitter = emitter;

    }

    @Override
    public void finished() {
        if (this.emitter instanceof GameActor) {
            ((GameActor) this.emitter).setDirection(Direction.HOLD_DEF);
        }

    }

    @Override
    public void setup() {
        super.setup();
        if (this.emitter instanceof GameActor) {
            ((GameActor) this.emitter).setDirection(Direction.HOLD_ATK);
        }
        SoundUtil.playSE(skill.soundName);
    }

}
