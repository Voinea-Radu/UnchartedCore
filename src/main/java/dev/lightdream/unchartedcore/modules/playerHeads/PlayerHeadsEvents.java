package dev.lightdream.unchartedcore.modules.playerHeads;

import dev.lightdream.unchartedcore.databases.PlayerHead;
import dev.lightdream.unchartedcore.files.dto.Item;
import dev.lightdream.unchartedcore.files.dto.XMaterial;
import dev.lightdream.unchartedcore.modules.signshop.SignShopModule;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import dev.lightdream.unchartedcore.utils.NbtUtils;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerHeadsEvents implements Listener {

    @EventHandler
    public void onPlayerDropHead(PlayerDeathEvent event) {
        Player player = event.getEntity();

        PlayerHead ph = new PlayerHead(player.getUniqueId());
        DatabaseUtils.getPlayerHeadList().add(ph);
        player.getWorld().dropItem(player.getLocation(), getHead(player.getName(), ph.id));
    }

    @EventHandler
    public void onHeadPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        Double dId = (Double) NbtUtils.getNBT(item, "id");
        if (dId == null) {
            return;
        }
        int id = (int) Math.floor(dId);
        PlayerHead ph = DatabaseUtils.getPlayerHead(id);
        if (ph == null) {
            return;
        }
        ph.setLocation(event.getBlock().getLocation());
    }

    @EventHandler
    public void onHeadBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        PlayerHead ph = DatabaseUtils.getPlayerHead(block.getLocation());
        if (ph == null) {
            return;
        }
        event.setCancelled(true);
        block.setType(Material.AIR);
        ph.setLocation(null);
        player.getInventory().addItem(getHead(player.getName(), ph.id));
    }

    @EventHandler()
    public void onSellCommandSend(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String[] parts = message.split(" ");
        if (parts[0].equals("/head") || parts[0].equals("/heads")) {
            event.setCancelled(true);
            PlayerHeadsModule.instance.headsCommand.execute(player, new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)));
        }
    }

    public ItemStack getHead(String name, int id) {
        ItemStack head = ItemBuilder.makeItem(new Item(XMaterial.PLAYER_HEAD, 1, name + "' Head", name, new ArrayList<>()));
        head = NbtUtils.setNBT(head, "id", id);
        return head;
    }


}
