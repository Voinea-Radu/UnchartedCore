package dev.lightdream.unchartedcore.modules.silkSpawners;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

import java.util.List;

public class SilkSpawnersModule extends CoreModule {
    public SilkSpawnersModule(Main plugin) {
        super(plugin, "SilkSpawnersModule");
    }

    @Override
    public Listener registerEventListeners() {
        return new SilkSpawnersEvents();
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

    }
}
