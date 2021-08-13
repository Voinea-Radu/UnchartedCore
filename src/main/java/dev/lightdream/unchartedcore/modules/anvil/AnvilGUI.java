package dev.lightdream.unchartedcore.modules.anvil;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.files.dto.GUIConfig;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import dev.lightdream.unchartedcore.utils.Utils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

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
        contents.set(Utils.getSlotPosition(config.items.get(0).item.slot), ClickableItem.of(item1, e -> {
            if (item1 != null) {
                player.getInventory().addItem(item1);
                item1 = null;
                result = null;
                List<ItemStack> tmp = AnvilModule.instance.events.items.getOrDefault(player, Arrays.asList(null, null));
                tmp.set(0, null);
                AnvilModule.instance.events.items.put(player, tmp);
            }

        }));
        contents.set(Utils.getSlotPosition(config.items.get(1).item.slot), ClickableItem.of(ItemBuilder.makeItem(config.items.get(1).item), e -> {
            player.closeInventory();
        }));
        contents.set(Utils.getSlotPosition(config.items.get(2).item.slot), ClickableItem.of(item2, e -> {
            if (item2 != null) {
                player.getInventory().addItem(item2);
                item2 = null;
                result = null;
                List<ItemStack> tmp = AnvilModule.instance.events.items.getOrDefault(player, Arrays.asList(null, null));
                tmp.set(1, null);
                AnvilModule.instance.events.items.put(player, tmp);
            }
        }));
        contents.set(Utils.getSlotPosition(config.items.get(3).item.slot), ClickableItem.of(result, e -> {
            if (result != null) {
                player.getInventory().addItem(result);
                item1 = null;
                item2 = null;
                result = null;
                AnvilModule.instance.events.items.put(player, Arrays.asList(null, null));
            }
        }));

    }
}
