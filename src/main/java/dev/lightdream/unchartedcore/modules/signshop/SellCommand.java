package dev.lightdream.unchartedcore.modules.signshop;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SellCommand extends Command {
    /**
     * @param plugin Main class
     */
    public SellCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("sell"), "", "", true, false, "[hand/all]");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() != 1) {
            sendUsage(sender);
            return;
        }
        Player player = (Player) sender;
        switch (args.get(0)) {
            case "hand":
                SignShopModule.instance.events.sell(player.getItemInHand().getAmount(), player, player.getItemInHand().getType());
                break;
            case "all":
                SignShopModule.instance.events.sell(1000000, player, player.getItemInHand().getType());
                break;
            default:
                sendUsage(sender);
                break;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
