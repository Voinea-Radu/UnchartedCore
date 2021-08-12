package dev.lightdream.unchartedcore.modules.playerHeads;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.databases.PlayerHead;
import dev.lightdream.unchartedcore.utils.NbtUtils;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SellHeadCommand extends Command {
    /**
     * @param plugin Main class
     */
    public SellHeadCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("sellHead"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();
        if (item == null) {
            System.out.println("1");
            MessageUtils.sendMessage(player, plugin.getMessages().invalidHead);
            return;
        }
        Double dId = ((Double) NbtUtils.getNBT(item, "id"));
        if (dId == null) {
            System.out.println("2");
            MessageUtils.sendMessage(player, plugin.getMessages().invalidHead);
            return;
        }
        int id = (int) Math.floor(dId);
        PlayerHead ph = DatabaseUtils.getPlayerHead(id);
        if (ph == null) {
            System.out.println("3");
            MessageUtils.sendMessage(player, plugin.getMessages().invalidHead);
            return;
        }
        if (ph.owner.equals(player.getUniqueId())) {
            System.out.println("4");
            MessageUtils.sendMessage(player, plugin.getMessages().cannotSellOwnHead);
            return;
        }
        double price = plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(ph.owner)) * PlayerHeadsModule.instance.settings.headSell;
        MessageUtils.sendMessage(player, plugin.getMessages().soldHead.replace("%price%", String.valueOf(price)));
        plugin.getEconomy().depositPlayer(player, price);
        DatabaseUtils.getPlayerHeadList().remove(ph);
        player.setItemInHand(null);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
