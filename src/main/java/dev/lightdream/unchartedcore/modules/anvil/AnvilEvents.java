package dev.lightdream.unchartedcore.modules.anvil;

import dev.lightdream.unchartedcore.Main;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AnvilEvents implements Listener {

    private final Main plugin;

    public AnvilEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAnvilOpen(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (block.getType() != Material.ANVIL) {
            return;
        }

        event.setCancelled(true);

        AnvilGUI.getInventory().open(player);
    }

    @EventHandler
    public void onItemAdd(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        Optional<SmartInventory> inventory = plugin.getInventoryManager().getInventory(player);
        if (inventory.isPresent()) {
            boolean allowed = false;
            for (String allowedEnchantItem : AnvilModule.instance.settings.allowedAnvilItems) {
                if (item.getType().toString().contains(allowedEnchantItem)) {
                    allowed = true;
                }
            }
            InventoryProvider provider = inventory.get().getProvider();
            if (provider instanceof AnvilGUI) {
                if (allowed) {
                    event.setCurrentItem(null);
                    AnvilGUI gui = ((AnvilGUI) inventory.get().getProvider());
                    if (gui.item1 == null) {
                        gui.item1 = item;
                    } else {
                        gui.item2 = item;
                        gui.result = processAnvil(gui.item1, gui.item2);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    public ItemStack processAnvil(ItemStack i1, ItemStack i2) {
        if (i1.getType() != i2.getType()) {
            if (!i2.getType().equals(Material.ENCHANTED_BOOK)) {
                return null;
            }
        }
        HashMap<Enchantment, Integer> e1 = new HashMap<>(i1.getEnchantments());
        HashMap<Enchantment, Integer> e2 = new HashMap<>(i1.getEnchantments());

        List<Enchantment> toRemove = new ArrayList<>();
        HashMap<Enchantment, Integer> e = new HashMap<>(e1);

        for (Enchantment enchantment1 : e1.keySet()) {
            for (Enchantment enchantment2 : e2.keySet()) {
                if (enchantment1.conflictsWith(enchantment2)) {
                    toRemove.add(enchantment2);
                }
                if (enchantment1 == enchantment2) {
                    int l1 = e1.get(enchantment1);
                    int l2 = e2.get(enchantment2);
                    if (l1 == l2) {
                        l1++;
                    }
                    e.put(enchantment1, Math.max(l1, l2));
                } else {
                    e.put(enchantment2, e2.get(enchantment2));
                }
            }
        }

        ItemStack i = i1.clone();
        i.setDurability(i.getType().getMaxDurability());
        for (Enchantment enchantment : e1.keySet()) {
            i.removeEnchantment(enchantment);
        }
        for (Enchantment enchantment : e.keySet()) {
            i.addUnsafeEnchantment(enchantment, e.get(enchantment));
        }

        return i;
    }

}
