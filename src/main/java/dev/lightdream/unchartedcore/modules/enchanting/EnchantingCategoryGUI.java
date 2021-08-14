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
    private final int page;
    public ItemStack enchantingItem;
    public List<ItemStack> enchants = new ArrayList<>();

    public EnchantingCategoryGUI(int page) {
        this.page = page;
    }

    public static SmartInventory getInventory(int page) {
        config = EnchantingModule.instance.settings.guiConfig;
        return SmartInventory.builder()
                .id(config.id)
                .provider(new EnchantingCategoryGUI(page))
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
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        if (enchantingItem == null) {
            for (int i = 0; i < config.items.size(); i++) {
                if (i == config.items.size() - 3) {
                    contents.set(Utils.getSlotPosition(config.items.get(i).item.slot), ClickableItem.of(ItemBuilder.makeItem(config.items.get(i).item), e -> {
                        player.closeInventory();
                    }));
                } else if (i != config.items.size() - 1 && i != config.items.size() - 2) {
                    contents.set(Utils.getSlotPosition(config.items.get(i).item.slot), null);
                }
            }
        } else {
            List<EnchantCategory> categories = EnchantingModule.instance.settings.getEnchantCategories(enchantingItem.getType().toString());
            for (int i = 0; i < config.items.size(); i++) {
                GUIItem item = config.items.get(i);
                if (config.items.indexOf(item) == 0) {
                    contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(enchantingItem, e -> {
                        player.getInventory().addItem(enchantingItem);
                        EnchantingModule.instance.events.items.remove(player);
                        player.closeInventory();
                        getInventory(0).open(player);
                    }));
                } else if (i == config.items.size() - 3) {
                    contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(ItemBuilder.makeItem(item.item), e -> {
                        player.closeInventory();
                    }));
                } else if (i == config.items.size() - 2) {
                    if (page != 0) {
                        contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(ItemBuilder.makeItem(item.item), e -> {
                            EnchantingModule.instance.events.items.remove(player);
                            getInventory(page - 1).open(player);
                            Optional<SmartInventory> inventory = Main.instance.getInventoryManager().getInventory(player);
                            ((EnchantingCategoryGUI) inventory.get().getProvider()).enchantingItem = enchantingItem;
                            Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
                                EnchantingModule.instance.events.items.put(player, enchantingItem);
                            }, 1);
                        }));
                    }
                } else if (i == config.items.size() - 1) {
                    if ((page + 1) * (config.items.size() - 3) < categories.size()) {
                        contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(ItemBuilder.makeItem(item.item), e -> {
                            EnchantingModule.instance.events.items.remove(player);
                            getInventory(page + 1).open(player);
                            Optional<SmartInventory> inventory = Main.instance.getInventoryManager().getInventory(player);
                            ((EnchantingCategoryGUI) inventory.get().getProvider()).enchantingItem = enchantingItem;
                            Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
                                EnchantingModule.instance.events.items.put(player, enchantingItem);
                            }, 1);
                        }));
                    }
                } else {
                    if (categories.size() >= i + page * (config.items.size() - 3)) {
                        EnchantCategory category = categories.get(i + page * (config.items.size() - 3) - 1);
                        contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(getBook(category), e -> {
                            EnchantingModule.instance.events.items.remove(player);
                            EnchantingGUI.getInventory(0).open(player);
                            Optional<SmartInventory> inventory = Main.instance.getInventoryManager().getInventory(player);
                            if (inventory.isPresent()) {
                                ((EnchantingGUI) inventory.get().getProvider()).category = category;
                                ((EnchantingGUI) inventory.get().getProvider()).enchantingItem = enchantingItem;
                                Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
                                    EnchantingModule.instance.events.items.put(player, enchantingItem);
                                }, 1);
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
