package com.codebattle.model.gameactor;

public class GameActorProperties{
	public String source;
	public int hp;
	public int mp;
	public int atk;
	public int def;
	public int range;
	public int maxsteps;
		
	public GameActorProperties(GameActorDescription desc)
	{
		this(desc.source, desc.hp, desc.mp, desc.atk, desc.def, desc.range, desc.maxsteps);
	}
	
	public GameActorProperties(String source, int hp, int mp, int atk, int def, int range, int maxsteps)
	{
		this.source = source;
		this.hp = hp;
		this.mp = mp;
		this.atk = atk;
		this.def = def;
		this.range = range;
		this.maxsteps = maxsteps;
	}
	
	public GameActorProperties(GameActorProperties prop)
	{
		this(prop.source, prop.hp, prop.mp, prop.atk, prop.def, prop.range, prop.maxsteps);
	}
	
}
