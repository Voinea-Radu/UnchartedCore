package dev.lightdream.unchartedcore.modules.mounts;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.files.dto.GUIConfig;
import dev.lightdream.unchartedcore.modules.enchanting.EnchantingCategoryGUI;
import dev.lightdream.unchartedcore.modules.mounts.dto.Mount;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import dev.lightdream.unchartedcore.utils.Utils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.particle.ParticleEffect;

public class MountsGUI implements InventoryProvider {

    private static GUIConfig config;

    public static SmartInventory getInventory() {
        config = MountsModule.instance.settings.guiConfig;
        return SmartInventory.builder()
                .id(config.id)
                .provider(new MountsGUI())
                .size(config.rows, config.columns)
                .title(config.title)
                .type(InventoryType.valueOf(config.type))
                .parent(EnchantingCategoryGUI.getInventory(0))
                .manager(Main.instance.getInventoryManager())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(ItemBuilder.makeItem(config.fillItem)));
        config.items.forEach(item -> {
            contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(ItemBuilder.makeItem(item.item), e -> {
                Mount mount = MountsModule.instance.settings.mountList.get(item.args.get(0));
                Entity entity = player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
                Horse horse = (Horse) entity;

                horse.setVariant(Horse.Variant.valueOf(mount.type));
                horse.setTamed(true);
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
                horse.setOwner(player);
                horse.setAdult();
                horse.setJumpStrength(mount.jumpPower);
                horse.setMaxHealth(mount.health);
                horse.setHealth(mount.health);

                if (mount.armour != null) {
                    horse.getInventory().setArmor(new ItemStack(Material.getMaterial(mount.armour), 1));
                    ParticleEffect.EXPLOSION_HUGE.display(player.getLocation());
                }

                if (mount.type.equalsIgnoreCase("SKELETON_HORSE")) {
                    player.getLocation().getWorld().strikeLightning(player.getLocation());
                    player.getLocation().getWorld().strikeLightning(player.getLocation());
                    player.getLocation().getWorld().strikeLightning(player.getLocation());
                }

                MountsModule.instance.events.mounts.put(player, horse);
            }));
        });
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }


}
