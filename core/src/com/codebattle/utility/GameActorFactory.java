package com.codebattle.utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.Region;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.gameactor.GameActorDescription;
import com.codebattle.model.gameactor.GameActorProperties;
import com.codebattle.model.gameactor.GameActorType;

public class GameActorFactory {
	
	private class Record
	{
		public String name;
		public int[] count;
		public GameActorDescription desc;
		
		public Record(String name) throws IOException
		{
			this.name = name;
			this.count = new int[GameConstants.OWNER_COUNT];
			Arrays.fill(count, 0);
			
			this.desc = XMLUtil.readGameActorDescFromFile(GameConstants.GAMEACTOR_PROP_DIR + name + GameConstants.DEFAULT_GAMEACTORDESC_EXTENSION);
		}
		
		public void addCount(Owner owner)
		{
			this.count[owner.index]++;
		}
	}
	
	private static GameActorFactory instance;
	private Map<String , Record> pool;
	
	private GameActorFactory()
	{
		this.pool = new HashMap<String , Record>();
	}
	
	public static GameActorFactory getInstance()
	{
		if(instance == null) {
			instance = new GameActorFactory();
		}
		
		return instance;
	}
	
	public GameActor createGameActor(GameStage stage, Owner owner, String name, String type, float sx , float sy) throws Exception
	{
		if(!pool.containsKey(name)) {
			this.pool.put(name, new Record(name));
		}
		
		//Get region data and then load texture frames
		Record record = this.pool.get(name);
		int id = record.count[owner.index];
		record.addCount(owner);
		
		String source = record.desc.source;
		Region region = record.desc.types.get(type).region;
		GameActorProperties prop = new GameActorProperties(record.desc.types.get(type).prop);
		TextureRegion[][] frames = TextureFactory.getInstance().loadCharacterFramesFromFile(source, region);
		
		return new GameActor(stage, owner, id, source, name, prop, frames, sx, sy);
	}
	
	public GameActorType getGameActorType(String source, String type)
	{
		return this.pool.get(source).desc.types.get(type);
	}
}
