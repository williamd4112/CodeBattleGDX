package com.codebattle.model;

import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.Skill;

public interface Affectable {
    abstract public void onAttacked(Attack attack);

    abstract public void onSkill(Skill skill, GameObject emitter);
}
