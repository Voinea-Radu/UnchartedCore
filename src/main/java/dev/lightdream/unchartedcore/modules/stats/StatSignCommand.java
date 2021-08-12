package dev.lightdream.unchartedcore.modules.stats;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class StatSignCommand extends Command {
    /**
     * @param plugin         Main class
     */
    public StatSignCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("statSign"), "", "", true, false, "delete");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if(args.size()!=1){
            sendUsage(sender);
            return;
        }
        Player player = (Player) sender;
        if(args.get(0).equalsIgnoreCase("delete")){
            StatsModule.instance.events.deleteSession.add(player);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
