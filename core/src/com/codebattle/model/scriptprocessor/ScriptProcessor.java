package com.codebattle.model.scriptprocessor;

import javax.script.ScriptException;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.GameState;
import com.codebattle.model.Owner;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.gameactor.GameActorProxy;

/**
 * Script processor.
 *
 * 1. Pass a stage so that the processor knows where it should do operation.
 * 2. Create a new thread to run all commands.
 */
public class ScriptProcessor extends BaseScriptProcessor {

    private final Owner currentPlayer;
    private final GameStage stage;

    public ScriptProcessor(final GameStage stage, final Owner currentPlayer,
            final String script) {
        super();
        this.stage = stage;
        this.currentPlayer = currentPlayer;
        this.script = script;

        System.out.println("ScriptProcessor put object : ");
        this.engine.put("vs", this.stage.getVirtualSystems()[currentPlayer.index]);
        for (GameObject obj : this.stage.getGameObjectsByOwner(currentPlayer)) {
            System.out.println(obj.getName());
            if (obj instanceof GameActor) {
                GameActorProxy proxy = new GameActorProxy((GameActor) obj);
                this.engine.put(proxy.getAlias(), proxy);
            } else {
                this.engine.put(obj.getName(), obj);
            }
        }
    }

    @Override
    public boolean run() {
        try {
            this.stage.getVirtualMap().preUpdate();
            // Processing script (put animation object into queue
            this.engine.eval(this.script);

            // Processing animation (start processing animation)
            this.stage.printAllVirtualObjects();
            this.stage.setState(GameState.ANIM);
            this.stage.getVirtualMap().resetActorsCulmuSteps();
            this.stage.getVirtualMap().resetObjectOperation();

        } catch (final ScriptException e) {
            System.out.println("------Exception occurred------");
            this.stage.reset();
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
