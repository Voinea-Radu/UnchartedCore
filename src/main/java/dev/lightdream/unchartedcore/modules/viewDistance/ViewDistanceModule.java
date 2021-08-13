package dev.lightdream.unchartedcore.modules.viewDistance;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

import java.util.List;

public class ViewDistanceModule extends CoreModule {
    public ViewDistanceModule(Main plugin) {
        super(plugin, "ViewDistanceModule");
    }

    @Override
    public Listener registerEventListeners() {
        return null;
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
