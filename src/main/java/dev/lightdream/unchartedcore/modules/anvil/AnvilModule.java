package dev.lightdream.unchartedcore.modules.anvil;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

public class AnvilModule extends CoreModule {

    public static AnvilModule instance;
    public AnvilConfig settings;

    public AnvilModule(Main plugin) {
        super(plugin, "AnvilModule");
        instance = this;
    }

    @Override
    public Listener registerEventListeners() {
        return new AnvilEvents(plugin);
    }

    @Override
    public Command registerCommands() {
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
        settings = plugin.getFileManager().load(AnvilConfig.class);
    }
}
