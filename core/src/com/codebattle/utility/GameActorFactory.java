package com.codebattle.utility;

import java.util.HashMap;
import java.util.Map;

import com.codebattle.model.GameActor;
import com.codebattle.model.GameStage;

public class GameActorFactory {
	
	private class Record
	{
		public String name;
		public int count;
		
		public Record(String name , int count)
		{
			this.name = name;
			this.count = count;
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
	
	public GameActor createGameActor(GameStage stage, String name, float sx , float sy)
	{
		if(!pool.containsKey(name))
			this.pool.put(name, new Record(name, 0));
		
		return new GameActor(stage, pool.get(name).count++, name, sx, sy);
	}
	
}
