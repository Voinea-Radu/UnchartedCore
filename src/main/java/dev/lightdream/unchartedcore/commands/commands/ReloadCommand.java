package dev.lightdream.unchartedcore.commands.commands;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command {

    /**
     * Reloads the plugin's config
     * @param plugin Main class
     */
    public ReloadCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("reload"), "Reloads the configs", "reload", false, false, "");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        plugin.loadConfigs();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }
}
