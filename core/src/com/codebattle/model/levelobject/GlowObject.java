package com.codebattle.model.levelobject;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.gui.StateShowable;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.animation.FrameTimer;
import com.codebattle.model.animation.Oscillator;
import com.codebattle.model.meta.PointLightMeta;
import com.codebattle.model.meta.Region;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.TextureFactory;

public class GlowObject extends ScriptableObject implements StateShowable {

    private TextureRegion[] frames;
    private PointLight light;
    private PointLightMeta lightMeta;

    private FrameTimer timer;
    private Oscillator oscillator;

    public GlowObject(GameStage stage, XmlReader.Element glowObjectContext, float x, float y)
            throws Exception {
        super(stage, Owner.GREEN, glowObjectContext.getChildByName("source").getText(),
                glowObjectContext.getChildByName("source").getText(), 0, x, y, 2);

        Region region = new Region(glowObjectContext.getChildByName("type").getChildByName(
                "region"));

        this.lightMeta = new PointLightMeta(glowObjectContext.getChildByName("type")
                .getChildByName("pointlight"));
        lightMeta.x += x;
        lightMeta.y += y;

        this.light = this.stage.addPointLight(lightMeta);
        this.frames = TextureFactory.getInstance().loadFrameRow(source, region,
                ResourceType.LEVELOBJECT);

        this.timer = new FrameTimer(10, this.frames.length - 1);
        this.oscillator = new Oscillator(10, 0.08f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.timer.act();
        this.light.setDistance(this.lightMeta.radius + oscillator.getValue());
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
    public void attack(int x, int y) {
        super.attack(x, y);
    }

    @Override
    public String[] getKeys() {

        return new String[] { "" };
    }

    @Override
    public String[] getValues() {

        return new String[] { "" };
    }

    @Override
    public Drawable getPortrait() {
        return null;
    }

    @Override
    public String getNameInfo() {
        return this.getName();
    }

    @Override
    public String getPositionInfo() {
        // TODO Auto-generated method stub
        return String.format("(%d , %d)", vx, vy);
    }

    @Override
    public boolean isBlock() {
        // TODO Auto-generated method stub
        return false;
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
    public void onSelected(Owner owner) {
        // TODO Auto-generated method stub

    }

}
