package com.codebattle.utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.meta.GameActorDescription;
import com.codebattle.model.meta.GameActorType;
import com.codebattle.model.meta.Region;

public class GameActorFactory {

    private class Record {
        public String name;
        public int[] count;
        public GameActorDescription desc;

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
    }

    private static GameActorFactory instance;
    private final Map<String, Record> pool;

    private GameActorFactory() {
        this.pool = new HashMap<String, Record>();
    }

    public static GameActorFactory getInstance() {
        if (instance == null) {
            instance = new GameActorFactory();
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
        final GameActorType actorType = record.desc.types.get(type);
        final TextureRegion[][] frames = TextureFactory.getInstance()
                .loadCharacterFramesFromFile(source, region);

        return new GameActor(stage, owner, id, source, name, actorType, frames, sx, sy);
    }

    public GameActorType getGameActorType(final String source, final String type) {
        return this.pool.get(source).desc.types.get(type);
    }

    public GameActorType getGameActorType(GameActor actor) {
        return getGameActorType(actor.source, actor.getProp().typeName);
    }
}
