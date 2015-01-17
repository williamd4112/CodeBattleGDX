package com.codebattle.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.levelobject.ScriptableObject;

public class VirtualMap {

    final GameStage stage;
    final public TiledMap map;
    final public MapProperties mapProperties;

    private final VirtualCell[][] virtualCells;

    public VirtualMap(final GameStage stage, final TiledMap map) {
        this.stage = stage;
        this.map = map;
        this.mapProperties = map.getProperties();
        this.virtualCells =
                new VirtualCell[this.stage.getMapHeight()][this.stage.getMapWidth()];
        this.initVirtualMap();
    }

    /**
     * Initialize virtual map
     */
    public void initVirtualMap() {
        final MapLayers layers = this.map.getLayers();
        for (final MapLayer layer : layers) {
            final TiledMapTileLayer tLayer = (TiledMapTileLayer) layer;
            if (tLayer.getName().equals("dye")) {
                continue;
            }
            this.scanLayer(tLayer);
        }
    }

    private void scanLayer(final TiledMapTileLayer tLayer) {
        for (int row = 0; row < this.stage.getMapHeight(); row++) {
            for (int col = 0; col < this.stage.getMapWidth(); col++) {
                if (this.virtualCells[row][col] == null) {
                    this.virtualCells[row][col] = new VirtualCell(null, col, row, true);
                }

                final Cell cell = tLayer.getCell(col, row);
                if (cell == null) {
                    continue;
                }

                final TiledMapTile tile = cell.getTile();
                final String prop = tile.getProperties().get("passiable", String.class);
                if (prop == null) {
                    this.virtualCells[row][col].setPassiable(true);
                } else if (prop.equals("0")) {
                    this.virtualCells[row][col].setPassiable(false);
                }
            }
        }
    }

    /**
     * ResetVirtualMap:
     * reset all virtual cell's object with actors' virtual coordinate
     * (NOTE: when used in error recovering, must check the virtual coordinate of actor has been restore to last state)
     */
    public void resetVirtualMap() {
        this.clearVirtualMap();
        // Reset the object on the cell
        for (final GameObject actor : this.stage.getGroupByType(GameObject.class)) {
            final int cellRow = actor.getVY();
            final int cellCol = actor.getVX();

            // Clear the dead objects
            if (actor.isAlive()) {
                this.virtualCells[cellRow][cellCol].setObject(actor, cellCol, cellRow);
            } else {
                this.stage.emitDestroyedEvent(actor);
            }

        }
        System.out.println("------Reset completed------");
    }

    /**
     * ResetActorsVirtualCoordinate
     * set actors virtual coordinate according its real coordinate
     */
    public void resetActorsVirtualCoordinate() {
        for (final GameActor actor : this.stage.getGroupByType(GameActor.class)) {
            actor.resetVirtualCoordinate();
        }
    }

    public void resetActorsCulmuSteps() {
        for (final MoveableGameObject actor : this.stage.getGroupByType(MoveableGameObject.class)) {
            // System.out.println("Reset steps on " + actor.getName());
            actor.resetCulmuSteps();
        }
    }

    public void resetObjectOperation() {
        for (final GameObject obj : this.stage.getGroupByType(GameObject.class)) {
            obj.resetOperation();
        }
    }

    /**
     * ClearVirtualMap
     * remove all object on the cell
     */
    public void clearVirtualMap() {
        for (int row = 0; row < this.stage.getMapHeight(); row++) {
            for (int col = 0; col < this.stage.getMapWidth(); col++) {
                this.virtualCells[row][col].removeObject(col, row);
            }
        }
    }

    /**
     * Update actors map when each 1-cell movement
     * @param lastX
     * @param lastY
     * @param newX
     * @param newY
     */
    public void updateVirtualMap(final GameObject obj, final int newX, final int newY) {
        this.virtualCells[obj.vy][obj.vx].removeObject(obj.vx, obj.vy);
        this.virtualCells[newY][newX].setObject(obj, newX, newY);
        obj.setVirtualCoordinate(newX, newY);
    }

    /**
     * Pre-update will process the cell script before all actor's action
     */
    public void preUpdate() {
        System.out.println("Pre-Update");
        // Update cell
        for (final VirtualCell[] row : this.virtualCells) {
            for (final VirtualCell cell : row) {
                cell.onUpdate();
                if (cell.getObject() != null) {
                    cell.getObject().increaseMP(2);
                }
            }
        }

        for (final ScriptableObject obj : this.stage.getGroupByType(ScriptableObject.class)) {
            obj.onUpdate();
        }
    }

    public boolean isPassiable(final int x, final int y) {
        return this.virtualCells[y][x].isPassible();
    }

    public VirtualCell getCell(final int x, final int y) {
        return this.virtualCells[y][x];
    }

    public VirtualCell[][] getVirtualCells() {
        return this.virtualCells;
    }

}
