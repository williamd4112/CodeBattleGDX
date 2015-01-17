package com.codebattle.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.codebattle.model.gameactor.GameObjectProperties;
import com.codebattle.model.meta.GameObjectType;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.GameObjectFactory;

/**
 * Define a general object over this game
 * Everything which is functional in the map is derived from this class
 * @author williamd
 *
 */
abstract public class GameObject extends Actor implements Affectable {

    // Where is this object
    // Repeated object avoidance
    final public GameStage stage;
    final public int id;

    final public String source;
    final public GameObjectType type;
    final protected GameObjectProperties properties;

    // Virtual Coordinate , used to calculate in actorsmap
    protected int vx;
    protected int vy;

    // Store actor's state, different state may cause some effect
    // Note: Death state used in virtual map (temporary removing actor
    protected GameObjectState state;

    // Controller
    protected Owner owner;

    // Operation times
    protected int operation = 0;

    public GameObject(final GameStage stage, final Owner owner, final String source,
            final String name, final int id,
            final GameObjectType type, final float sx, final float sy) {
        super();
        this.source = source;
        this.type = type;
        this.properties = new GameObjectProperties(type.prop);
        this.stage = stage;
        this.owner = owner;
        this.setName(name);
        this.id = id;
        this.state = GameObjectState.ALIVE;
        this.setX(sx);
        this.setY(sy);
        this.resetVirtualCoordinate();
    }

    protected void updateVirtualMap(final GameObject obj, final int newX, final int newY) {
        if (this.stage.isOutBoundInVirtualMap(newX, newY)) {
            return;
        }
        System.out.printf("update object (%d , %d) to (%d , %d)\n", obj.vx, obj.vy, newX,
                newY);

        this.stage.getVirtualMap().updateVirtualMap(this, newX, newY);
    }

    protected void resetVirtualCoordinate() {
        this.vx = (int) (this.getX() / GameConstants.CELL_SIZE);
        this.vy = (int) (this.getY() / GameConstants.CELL_SIZE);
        System.out.printf("Restore %s to (%d , %d)\n", this.getName(), this.vx, this.vy);
    }

    public int increaseHP(final int diff) {
        final int newValue = this.properties.hp + diff;
        final int max = GameObjectFactory.getInstance().getGameObjectType(this).prop.hp;
        this.properties.hp = newValue >= max ? max : newValue;
        return this.properties.hp;
    }

    public int decreaseHP(final int diff) {
        final int newValue = this.properties.hp - diff;
        this.properties.hp = newValue >= 0 ? newValue : 0;
        if (this.properties.hp == 0) {
            this.state = GameObjectState.DEATH;
        }

        return this.properties.hp;
    }

    public int increaseMP(final int diff) {
        final int newValue = this.properties.mp + diff;
        final int max = GameObjectFactory.getInstance().getGameObjectType(this).prop.mp;
        this.properties.mp = newValue >= max ? max : newValue;
        return this.properties.mp;
    }

    public int decreaseMP(final int diff) {
        final int newValue = this.properties.mp - diff;
        this.properties.mp = newValue >= 0 ? newValue : 0;

        return this.properties.mp;
    }

    public void addOperation() {
        this.operation++;
    }

    public void resetOperation() {
        this.operation = 0;
    }

    public boolean checkOperation() {
        return this.operation < this.type.prop.maxoperation;
    }

    public boolean isInbounding(final int x, final int y) {
        return x >= 0 && x < this.stage.getMapWidth() && y >= 0
                && y < this.stage.getMapHeight() ? true
                : false;
    }

    public boolean isAlive() {
        return this.state == GameObjectState.ALIVE;
    }

    @Override
    public String getName() {
        return this.id == 0 ? super.getName() : super.getName() + String.valueOf(this.id);
    }

    public GameObjectProperties getProp() {
        return this.properties;
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

    public GameStage getGameStage() {
        return this.stage;
    }

    public void setVirtualCoordinate(final int x, final int y) {
        this.vx = x;
        this.vy = y;
    }

    public void setState(final GameObjectState state) {
        this.state = state;
    }

    // Interaction with game objects in the game stage
    abstract public boolean isPassiable(int x, int y);

    abstract public boolean isBlock();

    abstract public void attack(int x, int y);

    abstract public boolean onInteract(GameObject contacter);

    abstract public void interact(int x, int y);

    abstract public void skill(int x, int y);

    abstract public void onDestroyed();

    // Interaction with user
    abstract public void onSelected(Owner owner);

}
