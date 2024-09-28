package pers.neige.neigeitems.action.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.manager.BaseActionManager;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ConditionAction extends Action {
    @Nullable
    private final String conditionString;
    @Nullable
    private final CompiledScript condition;
    @NotNull
    private final Action actions;
    @NotNull
    private final Action async;
    @NotNull
    private final Action sync;
    @NotNull
    private final Action deny;

    public ConditionAction(
            @NotNull BaseActionManager manager,
            @NotNull ConfigurationSection action
    ) {
        conditionString = action.getString("condition");
        if (conditionString != null) {
            try {
                condition = ((Compilable) manager.getEngine()).compile(conditionString);
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        } else {
            condition = null;
        }
        actions = manager.compile(action.get("actions"));
        async = manager.compile(action.get("async"));
        sync = manager.compile(action.get("sync"));
        deny = manager.compile(action.get("deny"));
    }

    public ConditionAction(
            @NotNull BaseActionManager manager,
            @NotNull Map<?, ?> action
    ) {
        Object value = action.get("condition");
        if (value instanceof String) {
            conditionString = (String) value;
            try {
                condition = ((Compilable) manager.getEngine()).compile(conditionString);
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        } else {
            conditionString = null;
            condition = null;
        }
        actions = manager.compile(action.get("actions"));
        async = manager.compile(action.get("async"));
        sync = manager.compile(action.get("sync"));
        deny = manager.compile(action.get("deny"));
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.CONDITION;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    @NotNull
    public CompletableFuture<ActionResult> eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

    @Nullable
    public String getConditionString() {
        return conditionString;
    }

    @Nullable
    public CompiledScript getCondition() {
        return condition;
    }

    @NotNull
    public Action getActions() {
        return actions;
    }

    @NotNull
    public Action getAsync() {
        return async;
    }

    @NotNull
    public Action getSync() {
        return sync;
    }

    @NotNull
    public Action getDeny() {
        return deny;
    }
}
