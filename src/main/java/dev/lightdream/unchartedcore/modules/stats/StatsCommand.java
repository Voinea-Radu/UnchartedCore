package dev.lightdream.unchartedcore.modules.stats;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class StatsCommand extends Command {
    /**
     * @param plugin Main class
     */
    public StatsCommand(@NotNull Main plugin) {
        super(plugin, Arrays.asList("stat", "stats"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        StatsGUI.getInventory().open((Player) sender);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
