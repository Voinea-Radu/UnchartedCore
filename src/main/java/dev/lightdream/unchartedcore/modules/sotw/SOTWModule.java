package dev.lightdream.unchartedcore.modules.sotw;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class SOTWModule extends CoreModule {
    public static SOTWModule instance;
    public SOTWConfig settings;

    public SOTWModule(Main plugin) {
        super(plugin, "SOTWModule");
        instance = this;
    }

    @Override
    public Listener registerEventListeners() {
        return null;
    }

    @Override
    public List<Command> registerCommands() {
        return Arrays.asList(
                new SOTWCommand(plugin)
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
        settings = plugin.getFileManager().load(SOTWConfig.class);
    }
}
