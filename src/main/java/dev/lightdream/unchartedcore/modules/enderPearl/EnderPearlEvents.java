package dev.lightdream.unchartedcore.modules.enderPearl;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnderPearlEvents implements Listener {

    List<Player> coolDown = new ArrayList<>();

    @EventHandler
    public void onPlayerPearl(ProjectileLaunchEvent event) {
        if (!event.getEntity().getType().equals(EntityType.ENDER_PEARL)) {
            return;
        }
        /*
        if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            return;
        }
         */
        Player player = (Player) event.getEntity().getShooter();
        if (coolDown.contains(player)) {
            event.setCancelled(true);
            MessageUtils.sendMessage(player, Main.instance.getMessages().enderPearlCoolDown);
            if (EnderPearlModule.instance.settings.givePearlBack) {
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
            }
            return;
        }
        coolDown.add(player);
        Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
            coolDown.remove(player);
        }, EnderPearlModule.instance.settings.coolDown * 20L);
    }

}
