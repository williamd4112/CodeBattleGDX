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
import com.codebattle.model.units.Direction;

abstract public class ScriptableObject extends MoveableGameObject {

    private Map<String, String> scripts;

    final private String readonlyScript;

    final private boolean isFixed;

    public ScriptableObject(GameStage stage, Owner owner, String source, String name, int id,
            GameObjectType type, float sx, float sy, int maxsteps, String readonlyScript,
            boolean isFixed) {
        super(stage, owner, source, name, id, type, sx, sy, maxsteps);
        this.scripts = new HashMap<String, String>();
        this.readonlyScript = (readonlyScript == null) ? null : readonlyScript;
        this.isFixed = isFixed;
    }

    @Override
    public void move(Direction direction, int pace) {
        if (this.isFixed)
            return;
        super.move(direction, pace);
    }

    @Override
    public boolean isBlock() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void attack(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onInteract(GameObject contacter) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void interact(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void skill(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSelected(Owner owner) {
        // TODO Auto-generated method stub

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
        this.loadScriptProcessor(readonlyScript);
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
            loadScriptProcessor(this.scripts.get(key));
        }
    }

    private void loadScriptProcessor(String script) {
        if (script == null)
            return;
        ObjectScriptProcessor processor = new ObjectScriptProcessor(this);
        processor.setScript(script);
        processor.run();
    }

}
