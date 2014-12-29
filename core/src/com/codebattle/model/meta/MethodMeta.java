package com.codebattle.model.meta;

import java.lang.reflect.Method;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.utility.Skills;

public class MethodMeta {
    public Method method;
    public Bundle args;

    public MethodMeta(XmlReader.Element methodElement) {
        try {
            String methodName = methodElement.getAttribute("name");
            this.method = Skills.class.getMethod(methodName, Bundle.class);

            this.args = new Bundle();
            for (XmlReader.Element argElement : methodElement.getChildrenByName("arg")) {
                String key = argElement.get("key");
                String value = argElement.get("value");
                args.bind(key, value);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
