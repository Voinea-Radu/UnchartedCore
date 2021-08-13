package dev.lightdream.unchartedcore.modules.cactusHopper;

import dev.lightdream.unchartedcore.databases.CactusHopper;
import dev.lightdream.unchartedcore.utils.NbtUtils;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class CactusHopperEvents implements Listener {


    @EventHandler
    public void onCactusGrow(BlockGrowEvent event) {
        Block block = event.getBlock();
        if (!block.getType().equals(Material.CACTUS)) {
            return;
        }
        CactusHopper hopper = DatabaseUtils.getCactusHopper(block.getLocation(), CactusHopperModule.instance.settings.range);
        if (hopper == null) {
            return;
        }
        event.setCancelled(true);
        Block hopperBlock = hopper.getLocation().getBlock();
        if (hopperBlock.getType() != Material.HOPPER) {
            DatabaseUtils.getCactusHopperList().remove(hopper);
            return;
        }
        Hopper hb = (Hopper) hopperBlock.getState();
        hb.getInventory().addItem(new ItemStack(Material.CACTUS, 1));
    }

    @EventHandler
    public void onHopperPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        ItemStack item = event.getItemInHand();
        if (!item.getType().equals(Material.HOPPER)) {
            return;
        }
        if (NbtUtils.getNBT(item, "cactus_hopper") == null) {
            return;
        }
        DatabaseUtils.getCactusHopperList().add(new CactusHopper(block.getLocation()));
    }

    @EventHandler
    public void onHopperBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!block.getType().equals(Material.HOPPER)) {
            return;
        }
        CactusHopper hopper = DatabaseUtils.getCactusHopper(block.getLocation());
        if (hopper == null) {
            return;
        }
        DatabaseUtils.getCactusHopperList().remove(hopper);
    }

}
