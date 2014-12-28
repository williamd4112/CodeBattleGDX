package com.codebattle.model.animation;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.structure.Skill;

public class SkillAnimation extends TargetBasedAnimation {

    public SkillAnimation(GameStage stage, Skill skill, GameObject target) throws Exception {
        super(stage, skill.animMeta, target);

    }

    @Override
    public void finished() {
        // TODO Auto-generated method stub

    }

}
