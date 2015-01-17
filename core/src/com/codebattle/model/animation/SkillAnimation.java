package com.codebattle.model.animation;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.Skill;
import com.codebattle.utility.SoundUtil;

public class SkillAnimation extends TargetBasedAnimation {

    private final Skill skill;

    public SkillAnimation(final GameStage stage, final Skill skill, final GameObject target)
            throws Exception {
        super(stage, skill.animMeta, target);
        this.skill = skill;

    }

    @Override
    public void finished() {

    }

    @Override
    public void setup() {
        super.setup();
        SoundUtil.playSES(this.skill.getSoundNames());
    }

}
