package com.codebattle.model.meta;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.utility.XMLUtil;

public class Skill {
    public Animation animMeta;
    public String soundName;
    private int range;

    private List<MethodMeta> methods;

    public Skill(XmlReader.Element skillElement) {
        this.methods = new LinkedList<MethodMeta>();

        // Read animation element
        this.animMeta = new Animation(XMLUtil.childByName(skillElement, "animation"));

        // Read sound element
        this.soundName = XMLUtil.childByName(skillElement, "sound")
                .getText();

        // Read range element
        this.range = Integer.parseInt(skillElement.getAttribute("range"));

        // Read method element
        for (XmlReader.Element methodElement : skillElement.getChildrenByName("method")) {
            this.methods.add(new MethodMeta(methodElement));
        }

    }

    public void execute(GameStage stage, GameObject emitter, int x, int y) {
        try {
            for (MethodMeta m : methods) {
                m.args.bind("Emitter", emitter);
                m.args.bind("Skill", this);
                m.args.bind("Stage", stage);
                m.args.bind("x", x);
                m.args.bind("y", y);
                m.method.invoke(this, m.args);
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
