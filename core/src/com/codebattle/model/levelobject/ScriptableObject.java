package com.codebattle.model.levelobject;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.MoveableGameObject;
import com.codebattle.model.Owner;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.GameObjectType;
import com.codebattle.model.meta.Skill;
import com.codebattle.model.scriptprocessor.ObjectScriptProcessor;
import com.codebattle.model.units.Direction;

import java.util.HashMap;
import java.util.Map;

abstract public class ScriptableObject extends MoveableGameObject {

    private final Map<String, String> scripts;

    final private String readonlyScript;

    final private boolean isFixed;

    public ScriptableObject(final GameStage stage, final Owner owner, final String source,
            final String name, final int id,
            final GameObjectType type, final float sx, final float sy, final int maxsteps,
            final String readonlyScript,
            final boolean isFixed) {
        super(stage, owner, source, name, id, type, sx, sy, maxsteps);
        this.scripts = new HashMap<String, String>();
        this.readonlyScript = readonlyScript == null ? null : readonlyScript;
        this.isFixed = isFixed;
    }

    @Override
    public void move(final Direction direction, final int pace) {
        if (this.isFixed) {
            return;
        }
        super.move(direction, pace);
    }

    @Override
    public boolean isBlock() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void attack(final int x, final int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onInteract(final GameObject contacter) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void interact(final int x, final int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void skill(final int x, final int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSelected(final Owner owner) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAttacked(final Attack attack) {
        this.execute("onAttacked");
    }

    @Override
    public void onSkill(final Skill skill, final GameObject emitter) {
        this.execute("onSkill");

    }

    @Override
    public void onDestroyed() {
        this.execute("onDestroyed");

    }

    public void onUpdate() {
        this.loadScriptProcessor(this.readonlyScript);
        this.execute("onUpdate");
    }

    public void setScript(final String key, final String value) {
        if (!this.scripts.containsKey(key)) {
            this.scripts.put(key, value);
        } else {
            this.scripts.remove(key);
            this.scripts.put(key, value);
        }
    }

    public void execute(final String key) {
        if (this.scripts.containsKey(key)) {
            this.loadScriptProcessor(this.scripts.get(key));
        }
    }

    private void loadScriptProcessor(final String script) {
        if (script == null) {
            return;
        }
        final ObjectScriptProcessor processor = new ObjectScriptProcessor(this);
        processor.setScript(script);
        processor.run();
    }

}
