package com.codebattle.model.structure;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.codebattle.model.Readable;

/**
 * Store actor's data including
 * GameActorDescription is each GameActorProperties' base
 * (but only copy some info , excluding source image meta data)
 * @source: source sprite(.png file path
 * @hp: health point
 * @mp: magic power
 * @atk: attack point
 * @def: def point
 * @range: attack range
 * @maxsteps: max movement steps
 * @author williamd
 *
 */
public class GameActorDescription implements Readable
{	
	/**
	 * Source Image
	 */
	public String source;
	
	/**
	 * Map<GameActorName , Map<TypeName , GameActorType>>
	 */
	final public Map<String , GameActorType> types;
	
	public GameActorDescription(Element context)
	{
		this();
		this.read(context);
	}
	
	public GameActorDescription()
	{
		this.types = new HashMap<String , GameActorType>();
	}
	
	@Override
	public void read(Element context) 
	{
		this.source = context.getChildByName("source").getText();
		for(XmlReader.Element type : context.getChildrenByNameRecursively("type")) {
			GameActorType gameActorType = new GameActorType(this.source , type);
			this.types.put(gameActorType.prop.name, gameActorType);
		}
	}
	
	@Override
	public String toString()
	{	
		String basic = "";
		for(String key : this.types.keySet()) {
			GameActorType type = this.types.get(key);
			basic += String.format("Type: %s\nHp: %d\nMp: %d\nAtk: %d\nDef: %d\nRange: %d\nMaxsteps: %d\n",
					key,type.prop.hp,type.prop.mp,type.prop.atk,type.prop.def,type.prop.range,type.prop.maxsteps);
			basic += type.region.toString();
		}
		
		return basic;
	}
}
