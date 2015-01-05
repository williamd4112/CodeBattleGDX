package com.codebattle.model.levelobject;

import java.util.HashMap;
import java.util.Map;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.MoveableGameObject;
import com.codebattle.model.Owner;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.GameObjectType;
import com.codebattle.model.meta.Skill;
import com.codebattle.model.scriptprocessor.ObjectScriptProcessor;

abstract public class ScriptableObject extends MoveableGameObject {

    private Map<String, String> scripts;

    public ScriptableObject(GameStage stage, Owner owner, String source, String name, int id,
            GameObjectType type, float sx, float sy, int maxsteps) {
        super(stage, owner, source, name, id, type, sx, sy, maxsteps);
        this.scripts = new HashMap<String, String>();

    }

    @Override
    public void onAttacked(Attack attack) {
        this.execute("onAttacked");
    }

    @Override
    public void onSkill(Skill skill, GameObject emitter) {
        this.execute("onSkill");

    }

    @Override
    public void onDestroyed() {
        this.execute("onDestroyed");

    }

    public void onUpdate() {
        this.execute("onUpdate");
    }

    public void setScript(String key, String value) {
        if (!this.scripts.containsKey(key)) {
            this.scripts.put(key, value);
        } else {
            this.scripts.remove(key);
            this.scripts.put(key, value);
        }
    }

    public void execute(String key) {
        if (this.scripts.containsKey(key)) {
            System.out.println(this.scripts.get(key));
            ObjectScriptProcessor processor = new ObjectScriptProcessor(this);
            processor.setScript(this.scripts.get(key));
            processor.run();
        }
    }

}
