package dev.lightdream.unchartedcore.modules.rename;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import dev.lightdream.unchartedcore.utils.NbtUtils;
import dev.lightdream.unchartedcore.utils.Utils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RenameEvents implements Listener {

    public List<Player> rename = new ArrayList<>();

    @EventHandler
    public void onRename(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        if (item == null) {
            return;
        }
        if (NbtUtils.getNBT(item, "rename_tag") == null) {
            return;
        }
        MessageUtils.sendMessage(player, Main.instance.getMessages().rename);
        rename.add(player);
    }

    @EventHandler
    public void onRenameType(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        ItemStack item = player.getItemInHand();
        if (!rename.contains(player)) {
            return;
        }
        event.setCancelled(true);
        if (message.equalsIgnoreCase("cancel")) {
            rename.remove(player);
            player.getInventory().addItem(NbtUtils.setNBT(ItemBuilder.makeItem(RenameModule.instance.settings.tag), "rename_tag", true));
            return;
        }
        if (item == null) {
            MessageUtils.sendMessage(player, Main.instance.getMessages().noItem);
            return;
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.color(message));
        item.setItemMeta(meta);
        player.setItemInHand(item);
    }

}
