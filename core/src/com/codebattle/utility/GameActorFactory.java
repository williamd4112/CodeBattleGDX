package com.codebattle.utility;

import java.util.HashMap;
import java.util.Map;

import com.codebattle.model.GameActor;
import com.codebattle.model.GameActorProperties;
import com.codebattle.model.GameStage;

public class GameActorFactory {
	
	private class Record
	{
		public String name;
		public int count;
		public GameActorProperties prop;
		
		public Record(String name , int count)
		{
			this.name = name;
			this.count = count;
			this.prop = JSONUtil.readJSONFromFile(GameActorProperties.class, GameConstants.GAMEACTOR_PROP_DIR + name);
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
	
	public GameActor createGameActor(GameStage stage, String name, float sx , float sy) throws Exception
	{
		if(!pool.containsKey(name)) {
			this.pool.put(name, new Record(name, 0));
		}
		
		Record record = this.pool.get(name);
		
		return new GameActor(stage, record.count++, name, record.prop, sx, sy);
	}
	
}
