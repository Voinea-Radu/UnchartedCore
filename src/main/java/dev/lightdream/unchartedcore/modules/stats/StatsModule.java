package dev.lightdream.unchartedcore.modules.stats;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.databases.StatSign;
import dev.lightdream.unchartedcore.modules.CoreModule;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import org.bukkit.event.Listener;

import javax.xml.crypto.Data;
import java.util.List;

public class StatsModule extends CoreModule {

    public static StatsModule instance;
    public StatsConfig settings;
    public StatsEvents events;

    public StatsModule(Main plugin) {
        super(plugin, "StatsModule");
        instance = this;
    }

    @Override
    public Listener registerEventListeners() {
        return events;
    }

    @Override
    public List<Command> registerCommands() {
        return null;
    }

    @Override
    public void enable() {
        events = new StatsEvents();
    }

    @Override
    public void disable() {
        DatabaseUtils.saveStatSigns();
    }

    @Override
    public void loadConfigs() {
        settings = plugin.getFileManager().load(StatsConfig.class);
    }

    public void processSigns(){
        DatabaseUtils.getStatSignsList().forEach(StatSign::process);
    }
}
