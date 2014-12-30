package com.codebattle.model.levelobject;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.PointLightMeta;
import com.codebattle.model.meta.Region;
import com.codebattle.model.meta.Skill;
import com.codebattle.utility.FrameTimer;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.TextureFactory;

public class GlowObject extends GameObject {

    private TextureRegion[] frames;
    private PointLight light;
    private FrameTimer timer;

    public GlowObject(GameStage stage, XmlReader.Element glowObjectContext, float x, float y)
            throws Exception {
        super(stage, Owner.GREEN, glowObjectContext.getChildByName("source")
                .getText(), glowObjectContext.getChildByName("source")
                .getText(), 0, x, y);

        Region region = new Region(glowObjectContext.getChildByName("type")
                .getChildByName("region"));

        PointLightMeta lightMeta = new PointLightMeta(glowObjectContext.getChildByName("type")
                .getChildByName("pointlight"));
        lightMeta.x += x;
        lightMeta.y += y;

        this.light = this.stage.addPointLight(lightMeta);
        this.frames = TextureFactory.getInstance()
                .loadFrameRow(source, region, ResourceType.LEVELOBJECT);
        this.timer = new FrameTimer(10, this.frames.length - 1);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.timer.act();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = batch.getColor();
        batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, parentAlpha
                * this.getColor().a);
        batch.draw(this.frames[this.timer.getFrame()], this.getX(), this.getY());
        batch.setColor(color);
    }

    @Override
    public boolean isPassiable(int x, int y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isBlock() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onAttacked(Attack attack) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSkillAttacked(int atk) {
        // TODO Auto-generated method stub

    }

    @Override
    public void attack(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onInteract(GameObject contacter) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void interact(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void skill(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSkill(Skill skill) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroyed(GameObject obj) {

    }

    @Override
    public void onSelected(Owner owner) {
        // TODO Auto-generated method stub

    }

}
