package com.codebattle.model.gameactor;

import com.badlogic.gdx.utils.XmlReader;

public class GameActorProperties{
	final public String name;
	public int hp;
	public int mp;
	public int atk;
	public int def;
	public int range;
	public int maxsteps;
			
	public GameActorProperties(String type, int hp, int mp, int atk, int def, int range, int maxsteps)
	{
		this.name = type;
		this.hp = hp;
		this.mp = mp;
		this.atk = atk;
		this.def = def;
		this.range = range;
		this.maxsteps = maxsteps;
	}
	
	public GameActorProperties(XmlReader.Element type)
	{
		this.name = type.getAttribute("name");
		this.hp = Integer.parseInt(type.getChildByName("hp").getText());
		this.mp = Integer.parseInt(type.getChildByName("mp").getText());
		this.atk = Integer.parseInt(type.getChildByName("atk").getText());
		this.def = Integer.parseInt(type.getChildByName("def").getText());
		this.range = Integer.parseInt(type.getChildByName("range").getText());
		this.maxsteps = Integer.parseInt(type.getChildByName("maxsteps").getText());
	}
	
	public GameActorProperties(GameActorProperties prop)
	{
		this(prop.name, prop.hp, prop.mp, prop.atk, prop.def, prop.range, prop.maxsteps);
	}
	
}
