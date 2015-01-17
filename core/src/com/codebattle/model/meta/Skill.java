package com.codebattle.model.meta;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.GameObject;
import com.codebattle.utility.XMLUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class Skill {
    public Animation animMeta;
    private final List<String> soundNames;
    private final int range;
    private final int cost;

    private final List<GameMethod> methods;

    public Skill(final XmlReader.Element skillElement) throws NoSuchMethodException,
            SecurityException {
        this.methods = new LinkedList<GameMethod>();

        // Read cost
        this.cost = Integer.parseInt(skillElement.getAttribute("cost"));

        // Read animation element
        this.animMeta = new Animation(XMLUtil.childByName(skillElement, "animation"));

        // Read sound element
        this.soundNames = new LinkedList<String>();
        for (final XmlReader.Element soundElement : skillElement.getChildrenByName("sound")) {
            this.soundNames.add(soundElement.getText());
        }

        // Read range element
        this.range = Integer.parseInt(skillElement.getAttribute("range"));

        // Read method element
        for (final XmlReader.Element methodElement : skillElement.getChildrenByName("method")) {
            this.methods.add(new GameMethod(methodElement));
        }

    }

    public void execute(final GameObject target, final GameObject emitter, final int x,
            final int y) {
        try {
            for (final GameMethod m : this.methods) {
                m.bind("Target", target);
                m.bind("tx", x);
                m.bind("ty", y);
                m.bind("Emitter", emitter);
                m.bind("Skill", this);
                m.bind("Stage", emitter.getGameStage());
                m.bind("Source", emitter.getOwner());
                m.execute();
            }
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getRange() {
        return this.range;
    }

    public int getCost() {
        return this.cost;
    }

    public List<String> getSoundNames() {
        return this.soundNames;
    }
}
