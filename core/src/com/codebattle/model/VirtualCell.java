package com.codebattle.model;

/**
 * Virtual Cell used to calculate action or movement in virtuality
 * @actor : actor
 * @passiable : map
 * @author williamd
 *
 */
public class VirtualCell {
    private GameObject obj = null;
    private boolean passiable = true;

    public VirtualCell(final GameObject actor, final boolean passiable) {
        this.obj = actor;
        this.passiable = passiable;
    }

    public VirtualCell(final GameObject actor) {
        this(actor, true);
    }

    public VirtualCell() {
        this(null, true);
    }

    public void setObject(final GameObject obj, final int x, final int y) {
        this.obj = obj;
        if (obj != null) {
            System.out.printf("set %s(%d , %d) at (%d , %d)\n", this.obj.getName(),
                    this.obj.getVX(), this.obj.getVY(), x, y);
        }
    }

    public void setPassiable(final boolean flag) {
        this.passiable = flag;
    }

    public void removeObject(final int x, final int y) {
        if (this.obj != null) {
            System.out.printf("remove %s(%d , %d) from (%d , %d)\n", this.obj.getName(),
                    this.obj.getVX(), this.obj.getVY(), x, y);
        }
        this.obj = null;
    }

    public GameObject getObject() {
        return this.obj;
    }

    public boolean isPassible() {
        if (this.obj != null) {
            return this.obj.isAlive() ? false : true;
        }
        return this.passiable;
    }
}
