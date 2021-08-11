package dev.lightdream.unchartedcore.modules.enchanting;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.files.dto.PluginLocation;
import dev.lightdream.unchartedcore.utils.Utils;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Optional;

public class EnchantingEvents implements Listener {

    private final Main plugin;
    public HashMap<Player, ItemStack> items = new HashMap<>();

    public EnchantingEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnchantingTableInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (block.getType() != Material.ENCHANTMENT_TABLE) {
            return;
        }
        PluginLocation pl1 = EnchantingModule.instance.settings.spawnRegionPos1;
        PluginLocation pl2 = EnchantingModule.instance.settings.spawnRegionPos2;
        PluginLocation l1 = pl1;
        PluginLocation l2 = pl2;
        l1.x = Math.min(pl1.x, pl2.x);
        l2.x = Math.max(pl1.x, pl2.x);
        l1.y = Math.min(pl1.y, pl2.y);
        l2.y = Math.max(pl1.y, pl2.y);
        l1.z = Math.min(pl1.z, pl2.z);
        l2.z = Math.max(pl1.z, pl2.z);

        Location location = player.getLocation();

        if (location.getX() < l1.x ||
                location.getX() > l2.x ||
                location.getY() < l1.y ||
                location.getY() > l2.y ||
                location.getZ() < l1.z ||
                location.getZ() > l2.z) {
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        EnchantingCategoryGUI.getInventory(0).open(player);


    }

    @EventHandler
    public void onEnchant(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        Optional<SmartInventory> inventory = plugin.getInventoryManager().getInventory(player);
        if (inventory.isPresent()) {
            boolean allowed = false;
            for (String allowedEnchantItem : EnchantingModule.instance.settings.allowedEnchantItems) {
                if (item.getType().toString().contains(allowedEnchantItem)) {
                    allowed = true;
                }
            }
            InventoryProvider provider = inventory.get().getProvider();
            if (provider instanceof EnchantingCategoryGUI) {
                if (allowed) {
                    EnchantingCategoryGUI gui = ((EnchantingCategoryGUI) inventory.get().getProvider());
                    if (gui.enchantingItem == null) {
                        if (item.getAmount() > 1) {
                            gui.enchantingItem = item.clone();
                            gui.enchantingItem.setAmount(1);
                            items.put(player, gui.enchantingItem);
                            item.setAmount(item.getAmount() - 1);
                            event.setCurrentItem(item);
                            event.setCancelled(true);
                        } else {
                            gui.enchantingItem = item;
                            items.put(player, item);
                            event.setCurrentItem(null);
                        }
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEnchantingLeave(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        InventoryView inventoryView = player.getOpenInventory();
        if (inventoryView.getTitle().equals(Utils.color(EnchantingModule.instance.settings.guiConfig.title))) {
            ItemStack itemStack = items.getOrDefault(player, null);
            if (itemStack != null) {
                player.getInventory().addItem(itemStack);
            }
        }
        items.remove(player);
    }
}
