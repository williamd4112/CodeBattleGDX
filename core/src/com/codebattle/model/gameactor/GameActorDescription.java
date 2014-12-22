package com.codebattle.model.gameactor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.codebattle.model.Readable;
import com.codebattle.model.Region;

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
	 * GameActorProperies using
	 */
	public String source;
	public int hp;
	public int mp;
	public int atk;
	public int def;
	public int range;
	public int maxsteps;
	
	/**
	 * GameActorDescription Only
	 */
	final public Map<String , Region> regions;
	
	public GameActorDescription(Element context)
	{
		this();
		this.read(context);
	}
	
	public GameActorDescription()
	{
		this.regions = new HashMap<String , Region>();
	}
	
	@Override
	public void read(Element context) 
	{
		this.source = context.getChildByName("source").getText();
		this.hp = Integer.parseInt(context.getChildByName("hp").getText());
		this.mp = Integer.parseInt(context.getChildByName("mp").getText());
		this.atk = Integer.parseInt(context.getChildByName("atk").getText());
		this.def = Integer.parseInt(context.getChildByName("def").getText());
		this.range = Integer.parseInt(context.getChildByName("range").getText());
		this.maxsteps = Integer.parseInt(context.getChildByName("maxsteps").getText());
		
		for(XmlReader.Element element : context.getChildrenByNameRecursively("region")) {
			Region region = new Region();
			region.x = Integer.parseInt(element.getAttribute("x"));
			region.y = Integer.parseInt(element.getAttribute("y"));
			region.width = Integer.parseInt(element.getAttribute("width"));
			region.height = Integer.parseInt(element.getAttribute("height"));
			
			this.regions.put(element.getAttribute("name"), region);
		}
	}
	
	@Override
	public String toString()
	{	
		String basic = String.format("Source: %s\nHp: %d\nMp: %d\nAtk: %d\nDef: %d\nRange: %d\nMaxsteps: %d\n",
				source,hp,mp,atk,def,range,maxsteps);
		String sRegion = "Region:\n";
		for(String key : this.regions.keySet()) {
			sRegion += key;
			sRegion += this.regions.get(key).toString();
			sRegion += '\n';
		}
		
		return basic + sRegion;
	}
}
