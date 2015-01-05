package com.codebattle.model.meta;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.GameObject;
import com.codebattle.utility.XMLUtil;

public class Skill {
    public Animation animMeta;
    public String soundName;
    private int range;

    private List<GameMethod> methods;

    public Skill(XmlReader.Element skillElement) throws NoSuchMethodException,
            SecurityException {
        this.methods = new LinkedList<GameMethod>();

        // Read animation element
        this.animMeta = new Animation(XMLUtil.childByName(skillElement, "animation"));

        // Read sound element
        this.soundName = XMLUtil.childByName(skillElement, "sound").getText();

        // Read range element
        this.range = Integer.parseInt(skillElement.getAttribute("range"));

        // Read method element
        for (XmlReader.Element methodElement : skillElement.getChildrenByName("method")) {
            this.methods.add(new GameMethod(methodElement));
        }

    }

    public void execute(GameObject target, GameObject emitter) {
        try {
            for (GameMethod m : methods) {
                m.bind("Target", target);
                m.bind("Emitter", emitter);
                m.bind("Skill", this);
                m.bind("Stage", target.getGameStage());
                m.bind("Source", emitter.getOwner());
                m.execute();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getRange() {
        return this.range;
    }
}
