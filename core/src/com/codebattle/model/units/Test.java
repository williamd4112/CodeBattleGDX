package com.codebattle.model.units;

import java.lang.reflect.Method;

import com.codebattle.model.Skills;
import com.codebattle.model.gameactor.GameActor;

public class Test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            Class[] arg = new Class[2];
            arg[0] = GameActor.class;
            arg[1] = int.class;
            Method m = Skills.class.getMethod("areaAttack", arg);
            System.out.println(m.getName());
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
