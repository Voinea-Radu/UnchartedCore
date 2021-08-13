package dev.lightdream.unchartedcore.modules.cactusHopper;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class CactusHopperModule extends CoreModule {

    public static CactusHopperModule instance;
    public CactusHopperConfig settings;

    public CactusHopperModule(Main plugin) {
        super(plugin, "CactusHopperModule");
        instance = this;
    }

    @Override
    public Listener registerEventListeners() {
        return new CactusHopperEvents();
    }

    @Override
    public List<Command> registerCommands() {
        return Arrays.asList(
                new CactusHopperCommand(plugin)
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
        settings = plugin.getFileManager().load(CactusHopperConfig.class);
    }
}
