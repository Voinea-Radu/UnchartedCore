package dev.lightdream.unchartedcore.modules.anvil;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.files.dto.PluginLocation;
import dev.lightdream.unchartedcore.modules.customEnchants.CustomEnchantsModule;
import dev.lightdream.unchartedcore.utils.Utils;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class AnvilEvents implements Listener {

    private final Main plugin;
    public HashMap<Player, List<ItemStack>> items = new HashMap<>();
    public HashMap<Player, ItemStack> toRename = new HashMap<>();

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
        PluginLocation pl1 = AnvilModule.instance.settings.spawnRegionPos1;
        PluginLocation pl2 = AnvilModule.instance.settings.spawnRegionPos2;
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
        AnvilGUI.getInventory().open(player);
    }

    @EventHandler
    public void onItemAdd(InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }
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
                    AnvilGUI gui = ((AnvilGUI) inventory.get().getProvider());
                    if (gui.item1 == null) {
                        if (item.getAmount() > 1) {
                            gui.item1 = item.clone();
                            gui.item1.setAmount(1);
                            item.setAmount(item.getAmount() - 1);
                            event.setCurrentItem(item);
                            event.setCancelled(true);
                            gui.result = processAnvil(gui.item1, gui.item2);
                        } else {
                            gui.item1 = item;
                            gui.result = processAnvil(gui.item1, gui.item2);
                            event.setCurrentItem(null);
                        }
                        List<ItemStack> tmp = items.getOrDefault(player, Arrays.asList(null, null));
                        tmp.set(0, gui.item1);
                        items.put(player, tmp);
                    } else if (gui.item2 == null) {
                        if (item.getAmount() > 1) {
                            gui.item2 = item.clone();
                            gui.item2.setAmount(1);
                            item.setAmount(item.getAmount() - 1);
                            event.setCurrentItem(item);
                            event.setCancelled(true);
                            gui.result = processAnvil(gui.item1, gui.item2);
                        } else {
                            gui.item2 = item;
                            gui.result = processAnvil(gui.item1, gui.item2);
                            event.setCurrentItem(null);
                        }
                        List<ItemStack> tmp = items.getOrDefault(player, Arrays.asList(null, null));
                        tmp.set(1, gui.item2);
                        items.put(player, tmp);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onAnvilLeave(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        InventoryView inventoryView = player.getOpenInventory();
        if (inventoryView.getTitle().equals(Utils.color(AnvilModule.instance.settings.guiConfig.title))) {
            List<ItemStack> tmp = items.getOrDefault(player, new ArrayList<>());
            for (ItemStack itemStack : tmp) {
                if (itemStack != null) {
                    player.getInventory().addItem(itemStack);
                }
            }
        }
        items.remove(player);
    }

    @EventHandler
    public void onNameEdit(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!toRename.containsKey(player)) {
            return;
        }

        String message = event.getMessage();
        ItemStack item = toRename.get(player);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.color(message));
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
        event.setCancelled(true);
    }


    public ItemStack processAnvil(ItemStack i1, ItemStack i2) {
        if (i1 == null || i2 == null) {
            return null;
        }
        if (i1.getType() != i2.getType()) {
            if (!i2.getType().equals(Material.ENCHANTED_BOOK)) {
                return null;
            }
        }
        HashMap<Enchantment, Integer> e1 = new HashMap<>(i1.getEnchantments());
        HashMap<Enchantment, Integer> e2 = new HashMap<>(i2.getEnchantments());

        List<Enchantment> toRemove = new ArrayList<>();
        HashMap<Enchantment, Integer> e = new HashMap<>(e1);

        if (e1.size() == 0) {
            for (Enchantment enchantment2 : e2.keySet()) {
                e.put(enchantment2, e2.get(enchantment2));
            }
        }
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
                    e.put(enchantment1, Math.min(enchantment1.getMaxLevel(), Math.max(l1, l2)));
                } else {
                    e.put(enchantment2, e2.get(enchantment2));
                }
            }
        }

        ItemStack i = i1.clone();
        i.setDurability((short) 0);
        for (Enchantment enchantment : toRemove) {
            i.removeEnchantment(enchantment);
        }
        for (Enchantment enchantment : e.keySet()) {
            if (CustomEnchantsModule.instance.isCustom(enchantment)) {
                CustomEnchantsModule.enchantItem(i, enchantment, e.get(enchantment), false);
            } else {
                i.addEnchantment(enchantment, e.get(enchantment));
            }
        }

        return i;
    }

}
