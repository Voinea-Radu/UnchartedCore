package dev.lightdream.unchartedcore.modules.warZoneClaims;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

import java.util.List;

public class WarZoneClaimsModule extends CoreModule {
    public WarZoneClaimsModule(Main plugin) {
        super(plugin, "WarZoneClaimsModule");
    }

    @Override
    public Listener registerEventListeners() {
        return new WarZoneClaimsEvents();
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
