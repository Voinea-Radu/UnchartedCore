package dev.lightdream.unchartedcore.modules;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import org.bukkit.event.Listener;

public abstract class CoreModule {

    public Main plugin;
    public String name;

    public CoreModule(Main plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public abstract Listener registerEventListeners();

    public abstract Command registerCommands();

    public abstract void enable();

    public abstract void disable();

    public abstract void loadConfigs();

}
