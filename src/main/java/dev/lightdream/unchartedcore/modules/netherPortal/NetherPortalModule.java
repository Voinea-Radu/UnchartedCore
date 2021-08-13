package dev.lightdream.unchartedcore.modules.netherPortal;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

import java.util.List;

public class NetherPortalModule extends CoreModule {
    public NetherPortalModule(Main plugin) {
        super(plugin, "NetherPortalModule");
    }

    @Override
    public Listener registerEventListeners() {
        return new NetherPortalEvents();
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
