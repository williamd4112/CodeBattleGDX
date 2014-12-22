package com.codebattle.utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.Region;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.gameactor.GameActorDescription;
import com.codebattle.model.gameactor.GameActorProperties;

public class GameActorFactory {
	
	private class Record
	{
		public String name;
		public int count;
		public GameActorDescription desc;
		
		public Record(String name) throws IOException
		{
			this.name = name;
			this.count = 0;
			this.desc = XMLUtil.readGameActorDescFromFile(GameConstants.GAMEACTOR_PROP_DIR + name + GameConstants.DEFAULT_GAMEACTORDESC_EXTENSION);
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
		String source = record.desc.source;
		Region region = record.desc.regions.get(type);
		TextureRegion[][] frames = TextureFactory.getInstance().loadCharacterFramesFromFile(source, region);
		
		return new GameActor(stage, owner, record.count++, name, record.desc, frames, sx, sy);
	}
	
}
