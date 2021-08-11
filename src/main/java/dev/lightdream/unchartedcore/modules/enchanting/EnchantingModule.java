package dev.lightdream.unchartedcore.modules.enchanting;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

public class EnchantingModule extends CoreModule {

    public static EnchantingModule instance;
    public EnchantingConfig settings;
    public EnchantingEvents events;

    public EnchantingModule(Main plugin) {
        super(plugin, "EnchantingModule");
        instance = this;
    }

    @Override
    public Listener registerEventListeners() {
        return events;
    }

    @Override
    public Command registerCommands() {
        return null;
    }

    @Override
    public void enable() {
        events = new EnchantingEvents(plugin);
    }

    @Override
    public void disable() {

    }

    @Override
    public void loadConfigs() {
        settings = plugin.getFileManager().load(EnchantingConfig.class);
    }
}
