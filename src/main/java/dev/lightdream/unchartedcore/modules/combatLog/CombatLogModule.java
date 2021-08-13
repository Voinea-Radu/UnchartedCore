package dev.lightdream.unchartedcore.modules.combatLog;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import org.bukkit.event.Listener;

import java.util.List;

public class CombatLogModule extends CoreModule {

    public static CombatLogModule instance;
    public CombatLogConfig settings;

    public CombatLogModule(Main plugin) {
        super(plugin, "CombatLogModule");
        instance = this;
    }

    @Override
    public Listener registerEventListeners() {
        return new CombatLogEvents();
    }

    @Override
    public List<Command> registerCommands() {
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
        settings = plugin.getFileManager().load(CombatLogConfig.class);
    }
}
