package com.codebattle.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.codebattle.model.gameactor.GameActor;

public class VirtualMap {

    final GameStage stage;
    final public TiledMap map;
    final public MapProperties mapProperties;

    private final VirtualCell[][] virtualCells;

    public VirtualMap(final GameStage stage, final TiledMap map) {
        this.stage = stage;
        this.map = map;
        this.mapProperties = map.getProperties();
        this.virtualCells = new VirtualCell[this.stage.getMapHeight()][this.stage.getMapWidth()];
        this.initVirtualMap();
    }

    /**
     * Initialize virtual map
     */
    public void initVirtualMap() {
        final MapLayers layers = this.map.getLayers();
        final int mapWidth = this.stage.getMapWidth();
        final int mapHeight = this.stage.getMapHeight();

        for (final MapLayer layer : layers) {
            final TiledMapTileLayer tLayer = (TiledMapTileLayer) layer;
            for (int row = 0; row < mapHeight; row++) {
                for (int col = 0; col < mapWidth; col++) {
                    boolean passiable = true;
                    try {
                        final Cell cell = tLayer.getCell(col, row);
                        final TiledMapTile tile = cell.getTile();
                        final String prop = tile.getProperties()
                                .get("passiable", String.class);
                        if (prop.equals("0")) {
                            passiable = false;
                        }
                    } catch (final Exception e) {
                        // e.printStackTrace();
                    }

                    if (this.virtualCells[row][col] == null) {
                        this.virtualCells[row][col] = new VirtualCell(null, passiable);
                    } else if (this.virtualCells[row][col].isPassible()) {
                        this.virtualCells[row][col] = new VirtualCell(null, passiable);
                    }
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
        for (final GameActor actor : this.stage.getGroupByType(GameActor.class)) {
            final int cellRow = ((GameObject) actor).getVY();
            final int cellCol = ((GameObject) actor).getVX();
            this.virtualCells[cellRow][cellCol].setObject(actor, cellCol, cellRow);

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
        for (final GameActor actor : this.stage.getGroupByType(GameActor.class)) {
            System.out.println("Reset steps on " + actor.getName());
            actor.resetCulmuSteps();
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
