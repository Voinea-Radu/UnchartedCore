package dev.lightdream.unchartedcore.modules.mounts;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MountCommand extends Command {

    List<Horse.Variant> mounts = Arrays.asList(Horse.Variant.HORSE, Horse.Variant.UNDEAD_HORSE, Horse.Variant.DONKEY, Horse.Variant.SKELETON_HORSE, Horse.Variant.MULE);

    /**
     * @param plugin Main class
     */
    public MountCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("mount"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        Player player = (Player) sender;

        Entity entity = player.getLocation().getWorld().spawnCreature(player.getLocation(), EntityType.HORSE);
        Horse horse = (Horse) entity;
        int variant = Utils.generateRandom(0, mounts.size() - 1);
        horse.setVariant(mounts.get(variant));
        horse.setTamed(true);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
        horse.setOwner(player);
        MountsModule.instance.events.mounts.put(player, horse);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}