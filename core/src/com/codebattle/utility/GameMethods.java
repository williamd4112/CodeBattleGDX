package com.codebattle.utility;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.gui.GameDialog;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameObjectState;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.animation.AttackAnimation;
import com.codebattle.model.animation.MovementAnimation;
import com.codebattle.model.animation.TransferAnimation;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.Bundle;
import com.codebattle.model.meta.GameMethod;
import com.codebattle.model.meta.Region;
import com.codebattle.model.meta.Skill;
import com.codebattle.model.units.Direction;
import com.codebattle.model.units.Speed;

public class GameMethods {
    public static void increaseHP(GameActor actor, int diff) {
        actor.increaseHP(diff);
    }

    public static void decreaseHP(Bundle args) {
        try {
            GameStage stage = args.extract("Stage", GameStage.class);
            String name = args.extract("Name", String.class);
            Owner owner = GameUtil.toOwner(args.extract("Owner", String.class));
            int diff = Integer.parseInt(args.extract("Diff", String.class));
            GameObject target = stage.findGameObjectByNameAndOwner(name, owner);
            if (target != null) {
                if (target instanceof GameActor)
                    ((GameActor) target).decreaseHP(diff);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void poison(GameActor actor) {
        actor.setState(GameObjectState.POISIONED);
    }

    public static void areaAttack(Bundle args) {
        Skill skill = args.extract("Skill", Skill.class);
        GameStage stage = args.extract("Stage", GameStage.class);
        String attack = args.extract("Attack", String.class);
        GameObject emitter = args.extract("Emitter", GameObject.class);
        int tx = args.extract("tx", int.class);
        int ty = args.extract("ty", int.class);
        int range = skill.getRange();

        int leftTopX = tx - range, leftTopY = ty + range;
        int rightBottomX = tx + range, rightBottomY = ty - range;

        for (int y = leftTopY; y >= rightBottomY; y--) {
            for (int x = leftTopX; x <= rightBottomX; x++) {
                subSkillAttack(stage, emitter, x, y, skill, attack);
            }
        }
    }

    // NOTE: attack is string
    private static void subSkillAttack(GameStage stage, GameObject emitter, int ax, int ay,
            Skill skill, String attack) {
        if (stage.isOutBoundInVirtualMap(ax, ay))
            return;
        GameObject subTarget = stage.findGameObject(ax, ay);
        if (subTarget == null)
            return;
        if (subTarget.getOwner() != emitter.getOwner()) {
            Bundle bundle = new Bundle();
            bundle.bind("Stage", stage);
            bundle.bind("Skill", skill);
            bundle.bind("Target", subTarget);
            bundle.bind("Attack", attack);
            skillAttack(bundle);
        }
    }

    public static void lineAttack(Bundle args) {
        try {
            Skill skill = args.extract("Skill", Skill.class);
            GameStage stage = args.extract("Stage", GameStage.class);
            GameObject emitter = args.extract("Emitter", GameObject.class);
            String attack = args.extract("Attack", String.class);
            int tx = args.extract("tx", int.class);
            int ty = args.extract("ty", int.class);

            Direction dir = relativeDirection(emitter.getVX(), emitter.getVY(), tx, ty);
            int ax = emitter.getVX(), ay = emitter.getVY();

            switch (dir) {
            case UP:
                for (; ay <= ty; ay++) {
                    subSkillAttack(stage, emitter, ax, ay, skill, attack);
                }
                break;
            case DOWN:
                for (; ay >= ty; ay--) {
                    subSkillAttack(stage, emitter, ax, ay, skill, attack);
                }
                break;
            case LEFT:
                for (; ax >= tx; ax--) {
                    subSkillAttack(stage, emitter, ax, ay, skill, attack);
                }
                break;
            case RIGHT:
                for (; ax <= tx; ax++) {
                    subSkillAttack(stage, emitter, ax, ay, skill, attack);
                }
                break;
            default:
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void charge(Bundle args) {
        try {
            Skill skill = args.extract("Skill", Skill.class);
            GameStage stage = args.extract("Stage", GameStage.class);
            GameObject emitter = args.extract("Emitter", GameObject.class);
            int x = args.extract("tx", int.class);
            int y = args.extract("ty", int.class);

            Direction dir = relativeDirection(emitter.getVX(), emitter.getVY(), x, y);
            int pace = paceCount(dir, emitter.getVX(), emitter.getVY(), x, y);

            if (!stage.isOutBoundInVirtualMap(x, y)) {
                System.out.println(emitter.getName() + " charge to "
                        + String.format("(%d , %d)", x, y));
                stage.getVirtualMap().updateVirtualMap(emitter, x, y);
                stage.addAnimation(new MovementAnimation(stage, emitter, dir, pace,
                        Speed.CHARGE));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int paceCount(Direction dir, int sx, int sy, int tx, int ty) {
        int pace = 0;
        switch (dir) {
        case UP:
            for (; sy < ty; sy++) {
                pace++;
            }
            break;
        case DOWN:
            for (; sy > ty; sy--) {
                pace++;
            }
            break;
        case LEFT:
            for (; sx > tx; sx--) {
                pace++;
            }
            break;
        case RIGHT:
            for (; sx < tx; sx++) {
                pace++;
            }
            break;
        default:
            break;
        }

        return pace;
    }

    private static Direction relativeDirection(int sx, int sy, int tx, int ty) {
        if (sy == ty) {
            if (sx <= tx)
                return Direction.RIGHT;
            else
                return Direction.LEFT;
        } else if (sx == tx) {
            if (sy <= ty)
                return Direction.UP;
            else
                return Direction.DOWN;
        } else
            return Direction.HOLD_DEF;
    }

    public static void skillAttack(Bundle args) {
        try {
            Skill skill = args.extract("Skill", Skill.class);
            GameStage stage = args.extract("Stage", GameStage.class);
            GameObject target = args.extract("Target", GameObject.class);
            if (target == null)
                return;
            Attack attack = new Attack(XMLUtil.stringToElement(args.extract("Attack",
                    String.class)));
            stage.addAnimation(new AttackAnimation(stage, attack, target));
            target.onAttacked(attack);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void transfer(Bundle args) {
        try {
            Skill skill = args.extract("Skill", Skill.class);
            GameStage stage = args.extract("Stage", GameStage.class);
            GameObject emitter = args.extract("Emitter", GameObject.class);
            int x = args.extract("tx", int.class);
            int y = args.extract("ty", int.class);

            if (!stage.isOutBoundInVirtualMap(x, y)) {
                stage.getVirtualMap().updateVirtualMap(emitter, x, y);
                stage.addAnimation(new TransferAnimation(stage,
                        GameConstants.SUMMON_ANIMMETA, emitter, x, y));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDialog(Bundle args) {
        try {
            GameStage stage = args.extract("Stage", GameStage.class);
            String context = args.extract("Context", String.class);
            TextureRegion portrait = null;

            String source = args.extract("Source", String.class);
            XmlReader reader = new XmlReader();
            String xml = args.extract("Region", String.class);
            String callback = args.extract("Callback", String.class);

            XmlReader.Element regionElement = reader.parse(xml);
            Region region = new Region(regionElement);
            portrait = TextureFactory.getInstance().loadFrameRow(source, region,
                    ResourceType.PORTRAIT)[0];

            GameDialog dlg = new GameDialog(stage, portrait, context,
                    GameConstants.DEFAULT_SKIN);
            if (callback != null) {
                XmlReader.Element callbackMethodElements = reader.parse(callback);

                for (XmlReader.Element callbackMethodElement : callbackMethodElements.getChildrenByName("method")) {
                    dlg.setCallback(new GameMethod(callbackMethodElement));
                }
            }

            stage.putDialog(dlg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callForWin(Bundle args) {
        GameStage stage = args.extract("Stage", GameStage.class);
        stage.emitStageCompleteEvent(Owner.RED);
        System.out.println("!!!!!!!!!Win!!!!!!!!!!!!");
    }

    public static void lookAt(Bundle args) {
        try {
            GameStage stage = args.extract("Stage", GameStage.class);
            int x = Integer.parseInt(args.extract("x", String.class));
            int y = Integer.parseInt(args.extract("y", String.class));
            stage.setCameraTarget(x * GameConstants.CELL_SIZE, y * GameConstants.CELL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createGameActor(Bundle args) {
        try {
            GameStage stage = args.extract("Stage", GameStage.class);
            String name = args.extract("Name", String.class);
            String type = args.extract("Type", String.class);
            Owner owner = GameUtil.toOwner(args.extract("Owner", String.class));
            int x = Integer.parseInt(args.extract("x", String.class))
                    * GameConstants.CELL_SIZE;
            int y = Integer.parseInt(args.extract("y", String.class))
                    * GameConstants.CELL_SIZE;
            GameActor actor = GameObjectFactory.getInstance().createGameActor(stage, owner,
                    name, type, x, y);
            stage.addGameObject(actor);
            stage.getVirtualMap().resetVirtualMap();
            stage.setCameraTarget(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSwitch(Bundle args) {
        try {
            GameStage stage = args.extract("Stage", GameStage.class);
            int index = Integer.parseInt(args.extract("Index", String.class));
            boolean val = Boolean.parseBoolean(args.extract("Value", String.class));
            stage.setSwitch(index, val);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean isGameObjectDestroyed(Bundle args) {
        try {
            GameStage stage = args.extract("Stage", GameStage.class);
            String name = args.extract("Target", String.class);
            Owner owner = GameUtil.toOwner(args.extract("Owner", String.class));
            return (stage.isExistGameObject(name, owner)) ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isSwitchOn(Bundle args) {
        try {
            GameStage stage = args.extract("Stage", GameStage.class);
            int index = Integer.parseInt(args.extract("Index", String.class));
            if (stage.getSwitchState(index))
                return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isGameObjectAtPosition(Bundle args) {
        try {
            GameStage stage = args.extract("Stage", GameStage.class);
            String name = args.extract("Target", String.class);
            Owner owner = GameUtil.toOwner(args.extract("Owner", String.class));
            int x = Integer.parseInt(args.extract("x", String.class));
            int y = Integer.parseInt(args.extract("y", String.class));
            GameObject target = stage.findGameObjectByNameAndOwner(name, owner);
            if (target != null) {
                return (target.getVX() == x && target.getVY() == y && target.isAlive()) ? true
                        : false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean noCondition(Bundle args) {
        return true;
    }
}
