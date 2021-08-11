package dev.lightdream.unchartedcore.modules.enchanting;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.files.dto.GUIConfig;
import dev.lightdream.unchartedcore.files.dto.GUIItem;
import dev.lightdream.unchartedcore.files.dto.Item;
import dev.lightdream.unchartedcore.modules.enchanting.dto.EnchantCategory;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import dev.lightdream.unchartedcore.utils.Utils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class EnchantingCategoryGUI implements InventoryProvider {

    private static GUIConfig config;
    public ItemStack enchantingItem;
    public List<ItemStack> enchants = new ArrayList<>();

    public static SmartInventory getInventory() {
        config = EnchantingModule.instance.settings.guiConfig;
        return SmartInventory.builder()
                .id(config.id)
                .provider(new EnchantingCategoryGUI())
                .size(config.rows, config.columns)
                .title(config.title)
                .type(InventoryType.valueOf(config.type))
                .parent(null)
                .manager(Main.instance.getInventoryManager())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(ItemBuilder.makeItem(config.fillItem)));
        if (enchantingItem == null) {
            for (GUIItem item : config.items) {
                int row = (item.item.slot + 1) / 9;
                int column = item.item.slot - row * 9;
                contents.set(new SlotPos(row, column), null);
            }
        } else {
            int row = (config.items.get(0).item.slot + 1) / 9;
            int column = config.items.get(0).item.slot - row * 9;
            contents.set(new SlotPos(row, column), ClickableItem.empty(enchantingItem));
        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        if (enchantingItem == null) {
            for (GUIItem item : config.items) {
                contents.set(Utils.getSlotPosition(item.item.slot), null);
            }
        } else {
            List<EnchantCategory> categories = EnchantingModule.instance.settings.getEnchantCategories(enchantingItem.getType().toString());
            for (int i = 0; i < config.items.size(); i++) {
                GUIItem item = config.items.get(i);
                if (config.items.indexOf(item) == 0) {
                    if (enchantingItem == null) {
                        contents.set(Utils.getSlotPosition(item.item.slot), null);
                    } else {
                        contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(enchantingItem, e -> {
                            player.getInventory().addItem(enchantingItem);
                            EnchantingModule.instance.events.items.remove(player);
                            player.closeInventory();
                            getInventory().open(player);
                        }));
                    }
                } else {
                    if (categories.size() >= i) {
                        EnchantCategory category = categories.get(i - 1);
                        contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(getBook(category), e -> {
                            EnchantingModule.instance.events.items.remove(player);
                            EnchantingGUI.getInventory().open(player);
                            Optional<SmartInventory> inventory = Main.instance.getInventoryManager().getInventory(player);
                            if (inventory.isPresent()) {
                                ((EnchantingGUI) inventory.get().getProvider()).category = category;
                                ((EnchantingGUI) inventory.get().getProvider()).enchantingItem = enchantingItem;
                                Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
                                    System.out.println("Adding back the item " + enchantingItem);
                                    EnchantingModule.instance.events.items.put(player, enchantingItem);
                                }, 2);
                            }
                        }));
                    } else {
                        contents.set(Utils.getSlotPosition(item.item.slot), null);
                    }
                }
            }
        }
    }

    private ItemStack getBook(EnchantCategory category) {
        Item builder = EnchantingModule.instance.settings.enchantCategoryItem.clone();

        builder.displayName = parse(builder.displayName, category.name, 0);
        builder.lore = parse(builder.lore, category.name, 0);

        return ItemBuilder.makeItem(builder);
    }


    private String parse(String raw, String name, int level) {
        String parsed = raw;

        parsed = parsed.replace("%enchant%", name);
        parsed = parsed.replace("%level%", String.valueOf(level));

        return parsed;
    }

    private List<String> parse(List<String> raw, String name, int level) {
        List<String> parsed = new ArrayList<>();

        raw.forEach(line -> {
            parsed.add(parse(line, name, level));
        });

        return parsed;
    }


}
