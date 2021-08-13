package dev.lightdream.unchartedcore.modules.kits;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class KitsModule extends CoreModule {

    public static KitsModule instance;
    public KitsConfig settings;

    public KitsModule(Main plugin) {
        super(plugin, "KitsModule");
        instance = this;
    }

    @Override
    public Listener registerEventListeners() {
        return null;
    }

    @Override
    public List<Command> registerCommands() {
        return Arrays.asList(
                new KitCommand(plugin)
        );
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
        DatabaseUtils.saveKits();
    }

    @Override
    public void loadConfigs() {
        settings = plugin.getFileManager().load(KitsConfig.class);
    }
}
