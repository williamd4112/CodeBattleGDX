package com.codebattle.utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.levelobject.LevelObject;
import com.codebattle.model.meta.GameObjectDescription;
import com.codebattle.model.meta.GameObjectType;
import com.codebattle.model.meta.PointLightMeta;
import com.codebattle.model.meta.Region;

public class GameObjectFactory {

    private class Record {
        public String name;
        public int[] count;
        public GameObjectDescription desc;

        public Record(final String name) throws IOException, NoSuchMethodException,
                SecurityException {
            this.name = name;
            this.count = new int[GameConstants.OWNER_COUNT];
            Arrays.fill(this.count, 0);

            this.desc = XMLUtil.readGameActorDescFromFile(GameConstants.GAMEACTOR_PROP_DIR
                    + name + GameConstants.DEFAULT_GAMEACTORDESC_EXTENSION);
        }

        public void addCount(final Owner owner) {
            this.count[owner.index]++;
        }

        public void resetCount() {
            Arrays.fill(this.count, 0);
        }
    }

    private static GameObjectFactory instance;
    private final Map<String, Record> pool;

    private GameObjectFactory() {
        this.pool = new HashMap<String, Record>();
    }

    public static GameObjectFactory getInstance() {
        if (instance == null) {
            instance = new GameObjectFactory();
        }

        return instance;
    }

    public GameActor createGameActor(final GameStage stage, final Owner owner,
            final String name, final String type, final float sx, final float sy)
            throws Exception {
        if (!this.pool.containsKey(name)) {
            this.pool.put(name, new Record(name));
        }

        // Get region data and then load texture frames
        final Record record = this.pool.get(name);
        final int id = record.count[owner.index];
        record.addCount(owner);

        final String source = record.desc.source;
        final Region region = record.desc.types.get(type).region;
        final GameObjectType actorType = record.desc.types.get(type);
        final TextureRegion[][] frames = TextureFactory.getInstance()
                .loadCharacterFramesFromFile(source, region);

        return new GameActor(stage, owner, id, source, name, actorType, frames, sx, sy);
    }

    public LevelObject createLevelObject(GameStage stage, String name, String type,
            PointLightMeta lightMeta, float sx, float sy) throws Exception {
        if (!this.pool.containsKey(name)) {
            this.pool.put(name, new Record(name));
        }

        // Get region data and then load texture frames
        final Record record = this.pool.get(name);
        final int id = record.count[Owner.GREEN.index];
        record.addCount(Owner.GREEN);

        final String source = record.desc.source;
        final Region region = record.desc.types.get(type).region;
        final GameObjectType objType = record.desc.types.get(type);
        final TextureRegion[] frames = TextureFactory.getInstance().loadFrameRow(source,
                region, ResourceType.LEVELOBJECT);

        return new LevelObject(stage, Owner.GREEN, source, name, id, objType, frames,
                lightMeta, sx, sy, objType.prop.maxsteps);
    }

    public GameObjectType getGameObjectType(final String source, final String type) {
        return this.pool.get(source).desc.types.get(type);
    }

    public GameObjectType getGameObjectType(GameActor actor) {
        return getGameObjectType(actor.source, actor.getProp().typeName);
    }

    public void resetCount() {
        for (String key : this.pool.keySet())
            this.pool.get(key).resetCount();
    }
}
