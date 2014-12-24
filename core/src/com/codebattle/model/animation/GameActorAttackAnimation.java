package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.gameactor.GameActorType;
import com.codebattle.model.units.Direction;
import com.codebattle.utility.AnimationUtil;
import com.codebattle.utility.GameActorFactory;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.SoundUtil;
import com.codebattle.utility.TextureFactory;

public class GameActorAttackAnimation extends Animation{

	private Texture texture;
	private TextureRegion[] anim;
	
	final public GameStage stage;
	final public GameActor attacker;
	final public GameObject target;
	
	private int duration;
	private float x = 0;
	private int frame = 0;
	private int frameWidth;
	private int frameHeight;
	
	public GameActorAttackAnimation(GameStage stage, GameActor attacker, GameObject target) throws Exception
	{
		this.stage = stage;
		this.attacker = attacker;
		this.target = target;
		this.duration = 20;	
	}
	
	@Override
	public void setup() 
	{
		super.setup();
		try {
			GameActorType type = GameActorFactory.getInstance().getGameActorType(attacker.source, attacker.getType());
			this.texture = TextureFactory.getInstance().loadTextureFromFile(attacker.source, ResourceType.ANIMATION);
			this.anim = TextureFactory.getInstance().loadAnimationFramesFromFile(type.attackAnimationSource, type.attackAnimationRegion);
			this.frameWidth = this.anim[0].getRegionWidth();
			this.frameHeight = this.anim[0].getRegionHeight();
			SoundUtil.playSE(type.attackSoundSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.attacker.setDirection(Direction.HOLD_ATK);
	}
	
	@Override
	public void update() 
	{	
		this.stage.setCameraTarget(this.attacker);
		if(this.duration % 5 == 0)
		this.frame = AnimationUtil.frameOscillate(this.frame, 0, 3);
		this.duration--;
	}

	@Override
	public boolean isFinished()
	{
		return (this.duration <= 0);
	}

	@Override
	public void finished() 
	{
		this.attacker.setDirection(Direction.HOLD_DEF);
		if(!this.target.isAlive())
			this.stage.removeGameObject(target);
	}
	
	@Override
	public void draw(Batch batch, Camera camera, float delta) 
	{
		int step = (int) (camera.viewportWidth / this.duration);
		Vector3 screen = camera.unproject(new Vector3(x += step, camera.viewportHeight - (camera.viewportHeight - texture.getHeight()) / 2 ,0));

		batch.draw(this.anim[frame], attacker.getX() + 16 - frameWidth / 2, attacker.getY() + 16 - frameHeight / 2);
		batch.draw(texture, screen.x, screen.y);
	}

}
