package dev.lightdream.unchartedcore.gui.executors;

import dev.lightdream.unchartedcore.gui.executors.executors.EnchantExecutor;

@SuppressWarnings("unused")
public enum GUIExecutors {

    BACK(null),
    ENCHANT(new EnchantExecutor());

    public GUIExecutor executor;

    GUIExecutors(GUIExecutor executor){
        this.executor = executor;
    }

}
