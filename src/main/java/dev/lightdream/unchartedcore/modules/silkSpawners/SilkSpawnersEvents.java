package dev.lightdream.unchartedcore.modules.silkSpawners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SilkSpawnersEvents implements Listener {

    @EventHandler
    public void onSpawnerBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (block.getType() != Material.MOB_SPAWNER) {
            return;
        }
        event.setCancelled(true);
        if (!player.getItemInHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
            return;
        }
        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "silkspawner give " + player.getName() + " " + spawner.getSpawnedType() + " 1");
        block.setType(Material.AIR);
    }

}