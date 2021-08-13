package dev.lightdream.unchartedcore.modules.enderPearl;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

import java.util.List;

public class EnderPearlModule extends CoreModule {

    public static EnderPearlModule instance;
    public EnderPearlConfig settings;

    public EnderPearlModule(Main plugin) {
        super(plugin, "EnderPearlModule");
        instance  =this;
    }

    @Override
    public Listener registerEventListeners() {
        return new EnderPearlEvents();
    }

    @Override
    public List<Command> registerCommands() {
        return null;
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void loadConfigs() {
        settings = plugin.getFileManager().load(EnderPearlConfig.class);
    }
}
