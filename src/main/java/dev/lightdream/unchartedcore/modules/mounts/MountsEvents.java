package dev.lightdream.unchartedcore.modules.mounts;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MountsEvents implements Listener {

    public HashMap<Player, Entity> mounts = new HashMap<>();

    @EventHandler
    public void onRiderDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (mounts.containsKey(player)) {
            mounts.get(player).remove();
            mounts.remove(player);
        }
    }

    @EventHandler
    public void onUnmount(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (mounts.containsKey(player)) {
            mounts.get(player).remove();
            mounts.remove(player);
        }
    }

    @EventHandler()
    public void onMountsCommandSend(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String[] parts = message.split(" ");
        if (parts[0].equals("/mount") || parts[0].equals("/mounts")) {
            event.setCancelled(true);
            MountsModule.instance.mountCommand.execute(player, new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)));
        }
    }

    @EventHandler
    public void onMountDeath(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!mounts.containsValue(entity)) {
            return;
        }
        Horse horse = (Horse) entity;
        if (horse.getHealth() - event.getFinalDamage() > 0) {
            return;
        }
        event.setCancelled(true);
        entity.remove();
        Player player = null;
        for (Player p : mounts.keySet()) {
            if (mounts.get(p).equals(entity)) {
                player = p;
                break;
            }
        }
        mounts.remove(player);
    }

}
