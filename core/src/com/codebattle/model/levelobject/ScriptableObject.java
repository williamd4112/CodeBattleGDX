package com.codebattle.model.levelobject;

import java.util.HashMap;
import java.util.Map;

import com.codebattle.model.GameObjectState;
import com.codebattle.model.GameStage;
import com.codebattle.model.MoveableGameObject;
import com.codebattle.model.Owner;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.Skill;
import com.codebattle.model.scriptprocessor.ObjectScriptProcessor;

abstract class ScriptableObject extends MoveableGameObject {

    private Map<String, String> scripts;

    public ScriptableObject(GameStage stage, Owner owner, String source, String name, int id,
            float sx, float sy, int maxsteps) {
        super(stage, owner, source, name, id, sx, sy, maxsteps);
        this.scripts = new HashMap<String, String>();

    }

    @Override
    public GameObjectState onAttacked(Attack attack) {
        this.execute("onAttacked");
        return null;
    }

    @Override
    public void onSkillAttacked(int atk) {
        this.execute("onSkillAttacked");

    }

    @Override
    public void attack(int x, int y) {
        this.execute("attack");

    }

    @Override
    public void skill(int x, int y) {
        this.execute("skill");

    }

    @Override
    public void onSkill(Skill skill) {
        this.execute("onSkill");

    }

    @Override
    public void onDestroyed() {
        this.execute("onDestroyed");

    }

    public void setScript(String key, String value) {
        this.scripts.put(key, value);
    }

    public void execute(String key) {
        if (this.scripts.containsKey(key)) {
            ObjectScriptProcessor processor = new ObjectScriptProcessor(this);
            processor.setScript(this.scripts.get(key));
            processor.run();
        }
    }

}
