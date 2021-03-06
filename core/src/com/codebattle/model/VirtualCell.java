package com.codebattle.model;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.codebattle.gui.StateShowable;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.Skill;
import com.codebattle.model.scriptprocessor.CellScriptProcessor;
import com.codebattle.utility.GameConstants;

/**
 * Virtual Cell used to calculate action or movement in virtuality
 * @actor : actor
 * @passiable : map
 * @author williamd
 *
 */
public class VirtualCell implements StateShowable, Affectable {

    final int x, y;

    private GameObject obj = null;
    private boolean passiable = true;

    private String script_enter = null, script_exit = null, script_update = null;

    public VirtualCell(final GameObject actor, final int x, final int y,
            final boolean passiable) {
        this.obj = actor;
        this.passiable = passiable;
        this.x = x;
        this.y = y;
    }

    public VirtualCell(final GameObject actor, final int x, final int y) {
        this(actor, x, y, true);
    }

    public VirtualCell(final int x, final int y) {
        this(null, x, y, true);
    }

    public void setObject(final GameObject obj, final int x, final int y) {
        this.obj = obj;
        if (obj != null) {
            this.onEnter(obj);
            // System.out.printf("set %s(%d , %d) at (%d , %d)\n", this.obj.getName(),
            // this.obj.getVX(), this.obj.getVY(), x, y);
        }
    }

    public void setPassiable(final boolean flag) {
        // System.out.printf("setPassiable@cell(%d , %d): %b\n", x, y, flag);
        this.passiable = flag;
    }

    public void removeObject(final int x, final int y) {
        if (this.obj != null) {
            this.onExit(this.obj);
            // System.out.printf("remove %s(%d , %d) from (%d , %d)\n", this.obj.getName(),
            // this.obj.getVX(), this.obj.getVY(), x, y);
        }
        this.obj = null;
    }

    public GameObject getObject() {
        return this.obj;
    }

    public GameActor getGameActor() {
        if (this.obj instanceof GameActor) {
            return (GameActor) this.obj;
        } else {
            return null;
        }
    }

    public float getX() {
        return this.x * GameConstants.CELL_SIZE;
    }

    public float getY() {
        return this.y * GameConstants.CELL_SIZE;
    }

    public int getVX() {
        return this.x;
    }

    public int getVY() {
        return this.y;
    }

    public boolean isPassible() {
        if (this.obj != null) {
            return this.obj.isBlock() ? false : true;
        }
        return this.passiable;
    }

    public void setScript(final String type, final String script) {
        if (type.equals("Enter")) {
            this.script_enter = script;
        } else if (type.equals("Exit")) {
            this.script_exit = script;
        } else if (type.equals("Update")) {
            this.script_update = script;
        }
    }

    @Override
    public void onSkill(final Skill skill, final GameObject emitter) {
        System.out.println(String.format("cell(%d ,% d)@ onSkill:\n", this.x, this.y));
        skill.execute(this.getObject(), emitter, this.x, this.y);
    }

    @Override
    public void onAttacked(final Attack attack) {

    }

    public void onUpdate() {
        // System.out.printf("onUpdate@cell(%d , %d):\n ", x, y);
        if (this.script_update != null) {
            final CellScriptProcessor processor = new CellScriptProcessor(this);
            processor.setScript(this.script_update);
            processor.run();
            this.script_update = "";
        }
    }

    public void onEnter(final GameObject obj) {
        // System.out.printf("onEnter@cell(%d , %d):\n ", x, y);
        if (this.script_enter != null) {
            final CellScriptProcessor processor = new CellScriptProcessor(this);
            processor.setScript(this.script_enter);
            processor.run();
            this.script_enter = "";
        }
    }

    public void onExit(final GameObject obj) {
        // System.out.printf("onExit@cell(%d , %d):\n ", x, y);
        if (this.script_exit != null) {
            final CellScriptProcessor processor = new CellScriptProcessor(this);
            processor.setScript(this.script_exit);
            processor.run();
            this.script_exit = "";
        }
    }

    @Override
    public String[] getKeys() {

        return new String[] { "Pass", "--", "--", "--", "--" };
    }

    @Override
    public String[] getValues() {
        return new String[] { String.valueOf(this.passiable), "--", "--", "--", "--" };
    }

    @Override
    public Drawable getPortrait() {
        return null;
    }

    @Override
    public String getNameInfo() {
        return String.format("%s", "Cell");
    }

    @Override
    public String getPositionInfo() {
        return String.format("(%d , %d)", this.x, this.y);
    }
}
