package com.codebattle.model.gameactor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.Region;

public class GameActorType
{
	public GameActorProperties prop;
	public Region region;
	
	public String attackAnimationSource;
	public Region attackAnimationRegion;
	public String attackSoundSource;
	
	public Map<String , String> apis;
	
	public GameActorType(String source , XmlReader.Element type)
	{
		this.apis = new HashMap<String , String>();
		
		//Read basic info
		this.prop = new GameActorProperties(source , type);
		
		//Read actors movement animation region
		XmlReader.Element regionElement = type.getChildByName("region");
		this.region = new Region(regionElement);
		
		//Read actor attack animation
		XmlReader.Element attackAnimationRegionElement = type.getChildByName("attack").getChildByName("animation").getChildByName("region");
		this.attackAnimationSource = type.getChildByName("attack").getChildByName("animation").getAttribute("source");
		this.attackAnimationRegion = new Region(attackAnimationRegionElement);
		this.attackSoundSource = type.getChildByName("attack").getChildByName("sound").getText();
		
		//Read api element
		XmlReader.Element apiElement = type.getChildByName("api");
		for(XmlReader.Element item : apiElement.getChildrenByName("item")) {
			String name = item.getAttribute("name");
			String content = item.getText();
			this.apis.put(name, content);
		}
	}
}
