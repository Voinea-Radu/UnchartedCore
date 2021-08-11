package dev.lightdream.unchartedcore.modules.anvil;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.files.dto.GUIConfig;
import dev.lightdream.unchartedcore.modules.enchanting.EnchantingCategoryGUI;
import dev.lightdream.unchartedcore.modules.enchanting.EnchantingGUI;
import dev.lightdream.unchartedcore.modules.enchanting.EnchantingModule;
import dev.lightdream.unchartedcore.modules.enchanting.dto.EnchantCategory;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import dev.lightdream.unchartedcore.utils.Utils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class AnvilGUI implements InventoryProvider {

    private static GUIConfig config;
    public ItemStack item1 = null;
    public ItemStack item2 = null;
    public ItemStack result = null;

    public static SmartInventory getInventory() {
        config = AnvilModule.instance.settings.guiConfig;
        return SmartInventory.builder()
                .id(config.id)
                .provider(new AnvilGUI())
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
        contents.set(Utils.getSlotPosition(config.items.get(0).item.slot), ClickableItem.empty(item1));
        contents.set(Utils.getSlotPosition(config.items.get(1).item.slot), ClickableItem.empty(ItemBuilder.makeItem(config.items.get(1).item)));
        contents.set(Utils.getSlotPosition(config.items.get(2).item.slot), ClickableItem.empty(item2));
        contents.set(Utils.getSlotPosition(config.items.get(3).item.slot), ClickableItem.empty(result));

    }
}
