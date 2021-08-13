package dev.lightdream.unchartedcore.modules.mounts;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class MountsModule extends CoreModule {

    public static MountsModule instance;
    public MountsEvents events;


    public MountsModule(Main plugin) {
        super(plugin, "MountsModule");
        instance = this;
    }

    @Override
    public Listener registerEventListeners() {
        return events;
    }

    @Override
    public List<Command> registerCommands() {
        return Arrays.asList(
                new MountCommand(plugin)
        );
    }

    @Override
    public void enable() {
        events = new MountsEvents();
    }

    @Override
    public void disable() {

    }

    @Override
    public void loadConfigs() {

    }
}