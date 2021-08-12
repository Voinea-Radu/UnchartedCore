package dev.lightdream.unchartedcore.modules.playerHeads;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class PlayerHeadsModule extends CoreModule {

    public static PlayerHeadsModule instance;
    public PlayerHeadsConfig settings;

    public PlayerHeadsModule(Main plugin) {
        super(plugin, "PlayerHeadsModule");
        instance = this;
    }

    @Override
    public Listener registerEventListeners() {
        return new PlayerHeadsEvents();
    }

    @Override
    public List<Command> registerCommands() {
        return Arrays.asList(new SellHeadCommand(plugin));
    }

    @Override
    public void enable() {
        DatabaseUtils.savePlayerHeads();
    }

    @Override
    public void disable() {

    }

    @Override
    public void loadConfigs() {
        settings = plugin.getFileManager().load(PlayerHeadsConfig.class);
    }
}
