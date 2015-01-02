package com.codebattle.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.Skill;
import com.codebattle.utility.GameConstants;

/**
 * Define a general object over this game
 * Everything which is functional in the map is derived from this class
 * @author williamd
 *
 */
abstract public class GameObject extends Actor {

    // Where is this object
    // Repeated object avoidance
    final public GameStage stage;
    final public int id;

    final public String source;

    // Virtual Coordinate , used to calculate in actorsmap
    protected int vx;
    protected int vy;

    // Store actor's state, different state may cause some effect
    // Note: Death state used in virtual map (temporary removing actor
    protected GameObjectState state;

    // Controller
    final protected Owner owner;

    public GameObject(GameStage stage, Owner owner, String source, String name, int id,
            float sx, float sy) {
        super();
        this.source = source;
        this.stage = stage;
        this.owner = owner;
        this.setName(name);
        this.id = id;
        this.state = GameObjectState.ALIVE;
        this.setX(sx);
        this.setY(sy);
        this.resetVirtualCoordinate();
    }

    protected void updateVirtualMap(GameObject obj, int newX, int newY) {
        System.out.printf("update object (%d , %d) to (%d , %d)\n", obj.vx, obj.vy, newX, newY);
        this.stage.getVirtualMap()
                .updateVirtualMap(this, newX, newY);
    }

    protected void resetVirtualCoordinate() {
        this.vx = (int) (this.getX() / GameConstants.CELL_SIZE);
        this.vy = (int) (this.getY() / GameConstants.CELL_SIZE);
        System.out.printf("Restore %s to (%d , %d)\n", this.getName(), this.vx, this.vy);
    }

    public boolean isInbounding(int x, int y) {
        return (x >= 0 && x < this.stage.getMapWidth() && y >= 0 && y < this.stage.getMapHeight()) ? true
                : false;
    }

    public boolean isAlive() {
        return (this.state == GameObjectState.ALIVE);
    }

    @Override
    public String getName() {
        return (this.id == 0) ? super.getName() : super.getName() + String.valueOf(id);
    }

    public int getVX() {
        return this.vx;
    }

    public int getVY() {
        return this.vy;
    }

    public GameObjectState getState() {
        return this.state;
    }

    @Override
    public float getCenterX() {
        return this.getX() + GameConstants.CELL_SIZE / 2;
    }

    @Override
    public float getCenterY() {
        return this.getY() + GameConstants.CELL_SIZE / 2;
    }

    public float getCursorX() {
        return this.getCenterX() - GameConstants.ONSELECT_CURSOR_REGION.hTile / 2;
    }

    public float getCursorY() {
        return this.getCenterY() - GameConstants.ONSELECT_CURSOR_REGION.vTile / 2
                + GameConstants.OBJECT_CURSOR_OFFSET;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setVirtualCoordinate(int x, int y) {
        this.vx = x;
        this.vy = y;
    }

    public void setState(GameObjectState state) {
        this.state = state;
    }

    // Interaction with game objects in the game stage
    abstract public boolean isPassiable(int x, int y);

    abstract public boolean isBlock();

    abstract public GameObjectState onAttacked(Attack attack);

    abstract public void onSkillAttacked(int atk);

    abstract public void attack(int x, int y);

    abstract public boolean onInteract(GameObject contacter);

    abstract public void interact(int x, int y);

    abstract public void skill(int x, int y);

    abstract public void onSkill(Skill skill);

    abstract public void onDestroyed();

    // Interaction with user
    abstract public void onSelected(Owner owner);

}
