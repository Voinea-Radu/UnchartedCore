package dev.lightdream.unchartedcore;

import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.commands.commands.DevCommand;
import dev.lightdream.unchartedcore.commands.commands.ReloadCommand;
import dev.lightdream.unchartedcore.files.config.Config;
import dev.lightdream.unchartedcore.files.config.GUIs;
import dev.lightdream.unchartedcore.files.config.Messages;
import dev.lightdream.unchartedcore.files.config.SQL;
import dev.lightdream.unchartedcore.managers.*;
import dev.lightdream.unchartedcore.modules.CoreModule;
import dev.lightdream.unchartedcore.modules.anvil.AnvilModule;
import dev.lightdream.unchartedcore.modules.customEnchants.CustomEnchantsModule;
import dev.lightdream.unchartedcore.modules.enchanting.EnchantingModule;
import dev.lightdream.unchartedcore.modules.signshop.SignShopModule;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class Main extends JavaPlugin {

    //Settings
    public final static String PROJECT_NAME = "UnchartedCore";
    public final static String PROJECT_ID = "uc";
    public static Main instance;
    //Commands
    private final List<Command> commands = new ArrayList<>();
    private final List<CoreModule> modules = new ArrayList<>();
    //SignShop
    private Economy economy = null;
    //Managers
    private CommandManager commandManager;
    private EventManager eventManager;
    private SchedulerManager schedulerManager;
    private InventoryManager inventoryManager;
    //Utils
    private FileManager fileManager;
    //DTO
    private Config settings;
    private Messages messages;
    private GUIs GUIs;
    private SQL sql;
    //Modules
    private SignShopModule signShopModule;
    private EnchantingModule enchantingModule;
    private CustomEnchantsModule customEnchantsModule;
    private AnvilModule anvilModule;

    @Override
    public void onEnable() {
        instance = this;
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        fileManager = new FileManager(this, FileManager.PersistType.YAML);

        //Modules
        signShopModule = new SignShopModule(this);
        enchantingModule = new EnchantingModule(this);
        customEnchantsModule = new CustomEnchantsModule(this);
        anvilModule = new AnvilModule(this);

        modules.add(signShopModule);
        modules.add(enchantingModule);
        modules.add(customEnchantsModule);
        modules.add(anvilModule);

        //Configs
        loadConfigs();


        //Utils
        MessageUtils.init(this);
        try {
            DatabaseUtils.init(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Commands
        commands.add(new ReloadCommand(this));
        commands.add(new DevCommand(this));

        //Load modules
        modules.forEach(CoreModule::enable);

        //Managers
        commandManager = new CommandManager(this, PROJECT_ID.toLowerCase(), commands);
        eventManager = new EventManager(this);
        schedulerManager = new SchedulerManager(this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPI(this).register();
        } else {
            getLogger().severe("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onDisable() {
        //Save files

        //Save to db
        DatabaseUtils.saveUsers();

        //Modules
        modules.forEach(CoreModule::disable);
    }

    public void loadConfigs() {
        settings = fileManager.load(Config.class);
        messages = fileManager.load(Messages.class);
        sql = fileManager.load(SQL.class);
        GUIs = fileManager.load(GUIs.class);

        modules.forEach(CoreModule::loadConfigs);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
