package dev.lightdream.unchartedcore.modules.rename;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class RenameModule extends CoreModule {

    public static RenameModule instance;
    public RenameConfig settings;

    public RenameModule(Main plugin) {
        super(plugin, "RenameModule");
    }

    @Override
    public Listener registerEventListeners() {
        return new RenameEvents();
    }

    @Override
    public List<Command> registerCommands() {
        return Arrays.asList(
                new GiveRenameTagCommand(plugin)
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
        settings = plugin.getFileManager().load(RenameConfig.class);
    }
}
