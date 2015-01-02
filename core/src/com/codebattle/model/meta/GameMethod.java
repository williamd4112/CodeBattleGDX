package com.codebattle.model.meta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.utility.GameMethods;

/**
 * Define a executeable method in GameMethods from xml element
 * @author williamd
 *
 */
public class GameMethod {
    private Method method;
    private Bundle args;

    public GameMethod(XmlReader.Element methodElement) throws NoSuchMethodException,
            SecurityException {
        // Parsing method name
        String methodName = methodElement.getAttribute("name");
        this.method = GameMethods.class.getMethod(methodName, Bundle.class);

        // Parsing args
        this.args = new Bundle();
        for (XmlReader.Element argElement : methodElement.getChildrenByName("arg")) {
            String key = argElement.get("key");
            String value = (argElement.getText() == null) ? "" : argElement.getText();
            for (int i = 0; i < argElement.getChildCount(); i++)
                value += argElement.getChild(i)
                        .toString();
            // System.out.printf("key: %s\n value:%s \n", key, value);
            args.bind(key, value);
        }
    }

    /**
     * Only execute
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public void execute() throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        this.method.invoke(this, args);
    }

    /**
     * Return the validation value
     * @return validation value
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public boolean validate() throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        return (Boolean) this.method.invoke(this, args);
    }

    public void bind(String key, Object value) {
        this.args.bind(key, value);
    }
}
