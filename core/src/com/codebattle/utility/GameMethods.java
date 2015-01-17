package com.codebattle.utility;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.gui.GameDialog;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameObjectState;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.VirtualCell;
import com.codebattle.model.animation.AttackAnimation;
import com.codebattle.model.animation.MovementAnimation;
import com.codebattle.model.animation.TargetBasedAnimation;
import com.codebattle.model.animation.TransferAnimation;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.meta.Animation;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.Bundle;
import com.codebattle.model.meta.GameMethod;
import com.codebattle.model.meta.Region;
import com.codebattle.model.meta.Skill;
import com.codebattle.model.units.Direction;
import com.codebattle.model.units.Speed;
import com.codebattle.scene.GameScene;
import com.codebattle.scene.PlayerGameScene;

public class GameMethods {
    public static void increaseHP(final GameActor actor, final int diff) {
        actor.increaseHP(diff);
    }

    public static void decreaseHP(final Bundle args) {
        try {
            final GameStage stage = args.extract("Stage", GameStage.class);
            final String name = args.extract("Name", String.class);
            final Owner owner = GameUtil.toOwner(args.extract("Owner", String.class));
            final int diff = Integer.parseInt(args.extract("Diff", String.class));
            final GameObject target = stage.findGameObjectByNameAndOwner(name, owner);
            if (target != null) {
                if (target instanceof GameActor) {
                    ((GameActor) target).decreaseHP(diff);
                }
            }
        } catch (final NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void poison(final GameActor actor) {
        actor.setState(GameObjectState.POISIONED);
    }

    public static void insertEditorHint(final Bundle args) {
        try {
            final GameStage stage = args.extract("Stage", GameStage.class);
            final String hint = args.extract("Hint", String.class);
            final GameScene scene = stage.parent;
            if (scene instanceof PlayerGameScene) {
                ((PlayerGameScene) scene).getGui().getEditor().setText(hint);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static TargetBasedAnimation encharge(final GameStage stage,
            final GameObject emitter,
            final int diff, final int x, final int y) {
        TargetBasedAnimation animation = null;
        try {
            final VirtualCell cell = stage.getVirtualMap().getCell(x, y);
            final GameObject obj = cell.getObject();
            final Animation anim = GameConstants.HEAL_ANIMMETA;
            if (obj != null) {
                if (obj.getOwner() == emitter.getOwner()) {
                    animation = new TargetBasedAnimation(stage, anim, obj,
                            GameConstants.SUMMON_TARGETBASEANIMATION_SCALE);
                    animation.addSound(GameConstants.HEAL_SE);
                    stage.addAnimation(animation);
                    obj.increaseMP(diff);
                    emitter.decreaseMP(diff);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return animation;
    }

    public static TargetBasedAnimation heal(final GameStage stage, final GameObject emitter,
            final int diff,
            final int x, final int y) {
        TargetBasedAnimation animation = null;
        try {
            final VirtualCell cell = stage.getVirtualMap().getCell(x, y);
            final GameObject obj = cell.getObject();
            final Animation anim = GameConstants.HEAL_ANIMMETA;
            if (obj != null) {
                if (obj.getOwner() == emitter.getOwner()) {
                    animation = new TargetBasedAnimation(stage, anim, obj,
                            GameConstants.SUMMON_TARGETBASEANIMATION_SCALE);
                    animation.addSound(GameConstants.HEAL_SE);
                    stage.addAnimation(animation);
                    obj.increaseHP(diff);
                    emitter.decreaseMP(diff);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return animation;
    }

    public static void shadow(final Bundle args) {
        try {
            final Skill skill = args.extract("Skill", Skill.class);
            final GameStage stage = args.extract("Stage", GameStage.class);
            final GameObject emitter = args.extract("Emitter", GameObject.class);
            int tx = args.extract("tx", int.class);
            int ty = args.extract("ty", int.class);
            if (stage.isOutBoundInVirtualMap(tx, ty)) {
                return;
            }
            tx *= GameConstants.CELL_SIZE;
            ty *= GameConstants.CELL_SIZE;
            final GameActor actor = GameObjectFactory.getInstance().createGameActor(stage,
                    emitter.getOwner(), emitter.source, emitter.type.getTypeName(), tx, ty);
            stage.addGameObject(actor);
            actor.decreaseHP(emitter.getProp().hp / 2);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void areaAttack(final Bundle args) {
        final Skill skill = args.extract("Skill", Skill.class);
        final GameStage stage = args.extract("Stage", GameStage.class);
        final String attack = args.extract("Attack", String.class);
        final GameObject emitter = args.extract("Emitter", GameObject.class);
        final int tx = args.extract("tx", int.class);
        final int ty = args.extract("ty", int.class);
        final int range = skill.getRange();

        final int leftTopX = tx - range, leftTopY = ty + range;
        final int rightBottomX = tx + range, rightBottomY = ty - range;

        for (int y = leftTopY; y >= rightBottomY; y--) {
            for (int x = leftTopX; x <= rightBottomX; x++) {
                subSkillAttack(stage, emitter, x, y, skill, attack);
            }
        }
    }

    // NOTE: attack is string
    private static void subSkillAttack(final GameStage stage, final GameObject emitter,
            final int ax, final int ay,
            final Skill skill, final String attack) {
        if (stage.isOutBoundInVirtualMap(ax, ay)) {
            return;
        }
        final GameObject subTarget = stage.findGameObject(ax, ay);
        if (subTarget == null) {
            return;
        }
        if (subTarget.getOwner() != emitter.getOwner()) {
            final Bundle bundle = new Bundle();
            bundle.bind("Stage", stage);
            bundle.bind("Skill", skill);
            bundle.bind("Target", subTarget);
            bundle.bind("Attack", attack);
            skillAttack(bundle);
        }
    }

    public static void lineAttack(final Bundle args) {
        try {
            final Skill skill = args.extract("Skill", Skill.class);
            final GameStage stage = args.extract("Stage", GameStage.class);
            final GameObject emitter = args.extract("Emitter", GameObject.class);
            final String attack = args.extract("Attack", String.class);
            final int tx = args.extract("tx", int.class);
            final int ty = args.extract("ty", int.class);

            final Direction dir = relativeDirection(emitter.getVX(), emitter.getVY(), tx, ty);
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

        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    public static void charge(final Bundle args) {
        try {
            final Skill skill = args.extract("Skill", Skill.class);
            final GameStage stage = args.extract("Stage", GameStage.class);
            final GameObject emitter = args.extract("Emitter", GameObject.class);
            final int x = args.extract("tx", int.class);
            final int y = args.extract("ty", int.class);

            final Direction dir = relativeDirection(emitter.getVX(), emitter.getVY(), x, y);
            final int pace = paceCount(dir, emitter.getVX(), emitter.getVY(), x, y);

            if (!stage.isOutBoundInVirtualMap(x, y)) {
                System.out.println(emitter.getName() + " charge to "
                        + String.format("(%d , %d)", x, y));
                stage.getVirtualMap().updateVirtualMap(emitter, x, y);
                stage.addAnimation(new MovementAnimation(stage, emitter, dir, pace,
                        Speed.CHARGE));
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static int paceCount(final Direction dir, int sx, int sy, final int tx,
            final int ty) {
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

    private static Direction relativeDirection(final int sx, final int sy, final int tx,
            final int ty) {
        if (sy == ty) {
            if (sx <= tx) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else if (sx == tx) {
            if (sy <= ty) {
                return Direction.UP;
            } else {
                return Direction.DOWN;
            }
        } else {
            return Direction.HOLD_DEF;
        }
    }

    public static void skillAttack(final Bundle args) {
        try {
            final Skill skill = args.extract("Skill", Skill.class);
            final GameStage stage = args.extract("Stage", GameStage.class);
            final GameObject target = args.extract("Target", GameObject.class);
            if (target == null) {
                return;
            }
            final Attack attack = new Attack(XMLUtil.stringToElement(args.extract("Attack",
                    String.class)));
            stage.addAnimation(new AttackAnimation(stage, attack, target));
            target.onAttacked(attack);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void transfer(final Bundle args) {
        try {
            final Skill skill = args.extract("Skill", Skill.class);
            final GameStage stage = args.extract("Stage", GameStage.class);
            final GameObject emitter = args.extract("Emitter", GameObject.class);
            final int x = args.extract("tx", int.class);
            final int y = args.extract("ty", int.class);

            if (!stage.isOutBoundInVirtualMap(x, y)) {
                stage.getVirtualMap().updateVirtualMap(emitter, x, y);
                stage.addAnimation(new TransferAnimation(stage,
                        GameConstants.SUMMON_ANIMMETA, emitter, x, y));
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDialog(final Bundle args) {
        try {
            final GameStage stage = args.extract("Stage", GameStage.class);
            final String context = args.extract("Context", String.class);
            TextureRegion portrait = null;

            final String source = args.extract("Source", String.class);
            final XmlReader reader = new XmlReader();
            final String xml = args.extract("Region", String.class);
            final String callback = args.extract("Callback", String.class);

            final XmlReader.Element regionElement = reader.parse(xml);
            final Region region = new Region(regionElement);
            portrait = TextureFactory.getInstance().loadFrameRow(source, region,
                    ResourceType.PORTRAIT)[0];

            final GameDialog dlg = new GameDialog(stage, portrait, context,
                    GameConstants.DEFAULT_SKIN);
            if (callback != null) {
                final XmlReader.Element callbackMethodElements = reader.parse(callback);

                for (final XmlReader.Element callbackMethodElement : callbackMethodElements.getChildrenByName("method")) {
                    dlg.setCallback(new GameMethod(callbackMethodElement));
                }
            }

            stage.putDialog(dlg);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void callForWin(final Bundle args) {
        final GameStage stage = args.extract("Stage", GameStage.class);
        stage.emitStageCompleteEvent(Owner.RED);
        System.out.println("!!!!!!!!!Win!!!!!!!!!!!!");
    }

    public static void lookAt(final Bundle args) {
        try {
            final GameStage stage = args.extract("Stage", GameStage.class);
            final int x = Integer.parseInt(args.extract("x", String.class));
            final int y = Integer.parseInt(args.extract("y", String.class));
            stage.setCameraTarget(x * GameConstants.CELL_SIZE, y * GameConstants.CELL_SIZE);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void createGameActor(final Bundle args) {
        try {
            final GameStage stage = args.extract("Stage", GameStage.class);
            final String name = args.extract("Name", String.class);
            final String type = args.extract("Type", String.class);
            final Owner owner = GameUtil.toOwner(args.extract("Owner", String.class));
            final int x = Integer.parseInt(args.extract("x", String.class))
                    * GameConstants.CELL_SIZE;
            final int y = Integer.parseInt(args.extract("y", String.class))
                    * GameConstants.CELL_SIZE;
            final GameActor actor =
                    GameObjectFactory.getInstance().createGameActor(stage, owner,
                            name, type, x, y);
            stage.addGameObject(actor);
            stage.getVirtualMap().resetVirtualMap();
            stage.setCameraTarget(x, y);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSwitch(final Bundle args) {
        try {
            final GameStage stage = args.extract("Stage", GameStage.class);
            final int index = Integer.parseInt(args.extract("Index", String.class));
            final boolean val = Boolean.parseBoolean(args.extract("Value", String.class));
            stage.setSwitch(index, val);
        } catch (final NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean isGameObjectDestroyed(final Bundle args) {
        try {
            final GameStage stage = args.extract("Stage", GameStage.class);
            final String name = args.extract("Target", String.class);
            final Owner owner = GameUtil.toOwner(args.extract("Owner", String.class));
            return stage.isExistGameObject(name, owner) ? false : true;
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isSwitchOn(final Bundle args) {
        try {
            final GameStage stage = args.extract("Stage", GameStage.class);
            final int index = Integer.parseInt(args.extract("Index", String.class));
            if (stage.getSwitchState(index)) {
                return true;
            }
        } catch (final NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isGameObjectAtPosition(final Bundle args) {
        try {
            final GameStage stage = args.extract("Stage", GameStage.class);
            final String name = args.extract("Target", String.class);
            final Owner owner = GameUtil.toOwner(args.extract("Owner", String.class));
            final int x = Integer.parseInt(args.extract("x", String.class));
            final int y = Integer.parseInt(args.extract("y", String.class));
            final GameObject target = stage.findGameObjectByNameAndOwner(name, owner);
            if (target != null) {
                return target.getVX() == x && target.getVY() == y && target.isAlive() ? true
                        : false;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean noCondition(final Bundle args) {
        return true;
    }
}
