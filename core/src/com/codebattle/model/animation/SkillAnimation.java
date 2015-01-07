package com.codebattle.model.animation;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.Skill;
import com.codebattle.utility.SoundUtil;

public class SkillAnimation extends TargetBasedAnimation {

    private Skill skill;

    public SkillAnimation(GameStage stage, Skill skill, GameObject target) throws Exception {
        super(stage, skill.animMeta, target);
        this.skill = skill;

    }

    @Override
    public void finished() {

    }

    @Override
    public void setup() {
        super.setup();
        SoundUtil.playSES(skill.getSoundNames());
    }

}
