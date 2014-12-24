package com.codebattle.model.gameactor;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.Region;

public class GameActorType
{
	public GameActorProperties prop;
	public Region region;
	
	public String attackAnimationSource;
	public Region attackAnimationRegion;
	public String attackSoundSource;
	
	public GameActorType(XmlReader.Element type)
	{
		//Read basic info
		this.prop = new GameActorProperties(type);
		
		//Read actors movement animation region
		XmlReader.Element regionElement = type.getChildByName("region");
		this.region = new Region(regionElement);
		
		//Read actor attack animation
		XmlReader.Element attackAnimationRegionElement = type.getChildByName("attack").getChildByName("animation").getChildByName("region");
		this.attackAnimationSource = type.getChildByName("attack").getChildByName("animation").getAttribute("source");
		this.attackAnimationRegion = new Region(attackAnimationRegionElement);
		this.attackSoundSource = type.getChildByName("attack").getChildByName("sound").getText();
		
	}
}
