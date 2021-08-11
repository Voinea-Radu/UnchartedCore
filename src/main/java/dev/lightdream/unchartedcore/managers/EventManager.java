package dev.lightdream.unchartedcore.managers;

import dev.lightdream.unchartedcore.Main;

public class EventManager {

    public final Main plugin;

    public EventManager(Main plugin) {
        this.plugin = plugin;
        plugin.getModules().forEach(module -> {
            if (module.registerEventListeners() != null) {
                plugin.getServer().getPluginManager().registerEvents(module.registerEventListeners(), plugin);
            }
        });
    }

}
