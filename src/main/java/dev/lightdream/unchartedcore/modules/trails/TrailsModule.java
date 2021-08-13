package dev.lightdream.unchartedcore.modules.trails;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class TrailsModule extends CoreModule {
    public TrailsModule(Main plugin) {
        super(plugin, "TrailsModule");
    }

    @Override
    public Listener registerEventListeners() {
        return new TrailEvents();
    }

    @Override
    public List<Command> registerCommands() {
        return Arrays.asList(
                new TrailCommand(plugin)
        );
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
