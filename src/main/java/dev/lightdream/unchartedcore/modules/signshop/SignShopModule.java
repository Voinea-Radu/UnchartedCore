package dev.lightdream.unchartedcore.modules.signshop;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.databases.SignShop;
import dev.lightdream.unchartedcore.modules.CoreModule;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import org.bukkit.event.Listener;

public class SignShopModule extends CoreModule {

    public static SignShopModule instance;
    public SignShopConfig settings;
    public SignShopEvents events;

    public SignShopModule(Main plugin) {
        super(plugin, "SignShopModule");
        instance = this;
    }

    @Override
    public Listener registerEventListeners() {
        return events;
    }

    @Override
    public Command registerCommands() {
        return new SignShopCommand(plugin, this);
    }

    @Override
    public void enable() {
        this.events = new SignShopEvents(plugin);
        loadSighShops();
    }

    @Override
    public void disable() {
        DatabaseUtils.saveSignShops();
    }

    @Override
    public void loadConfigs() {
        settings = plugin.getFileManager().load(SignShopConfig.class);
    }

    private void loadSighShops() {
        DatabaseUtils.getSignShopList().forEach(SignShop::process);
    }
}
