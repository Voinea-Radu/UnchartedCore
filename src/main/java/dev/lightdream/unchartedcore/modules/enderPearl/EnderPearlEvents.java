package dev.lightdream.unchartedcore.modules.enderPearl;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnderPearlEvents implements Listener {

    List<Player> coolDown = new ArrayList<>();

    @EventHandler
    public void onPlayerPearl(PlayerTeleportEvent event) {
        if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            return;
        }
        Player player = event.getPlayer();
        if (coolDown.contains(player)) {
            event.setCancelled(true);
            MessageUtils.sendMessage(player, Main.instance.getMessages().enderPearlCoolDown);
            player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
            return;
        }
        coolDown.add(player);
        Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
            coolDown.remove(player);
        }, EnderPearlModule.instance.settings.coolDown * 20L);
    }

}
