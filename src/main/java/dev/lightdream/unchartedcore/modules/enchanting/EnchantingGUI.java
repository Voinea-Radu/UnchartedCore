package dev.lightdream.unchartedcore.modules.enchanting;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.files.dto.GUIConfig;
import dev.lightdream.unchartedcore.files.dto.GUIItem;
import dev.lightdream.unchartedcore.files.dto.Item;
import dev.lightdream.unchartedcore.modules.customEnchants.CustomEnchantsModule;
import dev.lightdream.unchartedcore.modules.enchanting.dto.Enchant;
import dev.lightdream.unchartedcore.modules.enchanting.dto.EnchantCategory;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import dev.lightdream.unchartedcore.utils.Utils;
import dev.lightdream.unchartedcore.utils.XPUtils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantingGUI implements InventoryProvider {

    private static GUIConfig config;
    public ItemStack enchantingItem;
    public EnchantCategory category;

    public static SmartInventory getInventory() {
        config = EnchantingModule.instance.settings.guiConfig;
        return SmartInventory.builder()
                .id(config.id)
                .provider(new EnchantingGUI())
                .size(config.rows, config.columns)
                .title(config.title)
                .type(InventoryType.valueOf(config.type))
                .parent(null)
                .manager(Main.instance.getInventoryManager())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        if (enchantingItem == null) {
            return;
        }
        if (category == null) {
            return;
        }
        contents.fill(ClickableItem.empty(ItemBuilder.makeItem(config.fillItem)));
        for (int i = 0; i < config.items.size(); i++) {
            GUIItem item = config.items.get(i);
            if (config.items.indexOf(item) == 0) {
                contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(enchantingItem, e -> {
                    player.getInventory().addItem(enchantingItem);
                    EnchantingModule.instance.events.items.remove(player);
                    player.closeInventory();
                    EnchantingCategoryGUI.getInventory().open(player);
                }));
            } else {
                if (category.enchants.size() >= i) {
                    Enchant enchant = category.enchants.get(i - 1);
                    contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(getBook(enchant), e -> {
                        if (XPUtils.getTotalExperience(player) >= enchant.cost) {
                            XPUtils.setTotalExperience(player, XPUtils.getTotalExperience(player) - enchant.cost);
                            Enchantment enchantment = Enchantment.getByName(enchant.enchant);
                            if (enchantment == null) {
                                enchantment = CustomEnchantsModule.instance.getCustomEnchantment(enchant.enchant);
                                CustomEnchantsModule.enchantItem(enchantingItem, enchantment, enchant.level, false);
                            } else {
                                enchantingItem.addUnsafeEnchantment(enchantment, enchant.level);
                            }
                        }
                    }));
                } else {
                    contents.set(Utils.getSlotPosition(item.item.slot), null);
                }
            }
        }
    }

    private ItemStack getBook(Enchant enchant) {
        Item builder = EnchantingModule.instance.settings.enchantItem.clone();

        builder.displayName = parse(builder.displayName, category.name, enchant.level, enchant.cost);
        builder.lore = parse(builder.lore, enchant.name, enchant.level, enchant.cost);

        return ItemBuilder.makeItem(builder);
    }


    private String parse(String raw, String name, int level, int xp) {
        String parsed = raw;

        parsed = parsed.replace("%enchant%", name);
        parsed = parsed.replace("%level%", String.valueOf(level));
        parsed = parsed.replace("%xp%", String.valueOf(xp));

        return parsed;
    }

    private List<String> parse(List<String> raw, String name, int level, int xp) {
        List<String> parsed = new ArrayList<>();

        raw.forEach(line -> {
            parsed.add(parse(line, name, level, xp));
        });

        return parsed;
    }

}
