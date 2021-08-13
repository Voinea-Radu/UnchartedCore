package dev.lightdream.unchartedcore.modules.signshop;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
        ItemStack item = player.getItemInHand();
        if (item == null) {
            return;
        }
        SignShopEvents.BuySellResponse response;
        switch (args.get(0)) {
            case "hand":
                response = SignShopModule.instance.events.sell(player.getItemInHand().getAmount(), player, player.getItemInHand().getType(), "");
                break;
            case "all":
                response = SignShopModule.instance.events.sell(1000000, player, player.getItemInHand().getType(), "");
                break;
            default:
                sendUsage(sender);
                return;
        }
        if (!response.response) {
            MessageUtils.sendMessage(player, plugin.getMessages().invalidItem);
            return;
        }
        MessageUtils.sendMessage(player, plugin.getMessages().sold.replace("%amount%", String.valueOf(response.count)).replace("%price%", String.valueOf(response.price)));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
