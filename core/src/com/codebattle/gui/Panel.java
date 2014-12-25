package com.codebattle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.codebattle.model.gameactor.GameActorProperties;
import com.codebattle.utility.GameActorFactory;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.TextureFactory;

public class Panel extends BasePanel
{
	private Texture portrait;
	private Image image;
	private StateTable stateTable;
	
	private APIList apiList;
	
	public Panel(Skin skin)
	{
		super(skin);
		this.image = new Image();
		this.setImage("default");
		this.stateTable = new StateTable(skin);
		this.apiList = new APIList(skin);
	}
	
	public void resize(int width , int height)
	{
		this.reset();
		this.setDebug(true);
		this.stateTable.resize(width, height);
		this.apiList.resize(width, height);
		this.add(image).prefSize(width * 0.1f).pad(5).left().fill();
		this.add(stateTable).padLeft(width * 0.01f).padRight(width * 0.01f).left().center();
		this.add(apiList).left();
		this.pad(5);
	}
	
	public void setImage(String source)
	{
		try {
			this.portrait = TextureFactory.getInstance().loadTextureFromFile(source + "_portrait", ResourceType.PORTRAIT);
			Drawable drawable = new TextureRegionDrawable(new TextureRegion(this.portrait));
			this.image.setDrawable(drawable);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void setProperties(GameActorProperties prop)
	{
		this.stateTable.setProperties(prop);
		this.apiList.setAPIList(GameActorFactory.getInstance().getGameActorType(prop.source, prop.name).apis);
	}
	
	public void resetProperties()
	{
		this.stateTable.resetProperties();
	}
	
	public void resetImage()
	{
		this.setImage("default");
	}
	
}
