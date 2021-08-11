package dev.lightdream.unchartedcore.utils;

import fr.minuskube.inv.content.SlotPos;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class Utils {

    public static @NotNull List<String> color(@NotNull List<String> list) {
        List<String> output = new ArrayList<>();
        list.forEach(line -> output.add(color(line)));
        return output;
    }

    public static @NotNull String color(@NotNull String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static boolean checkExecute(double chance) {
        if (chance == 100) {
            return true;
        }
        double result = Math.random() * 101 + 0;
        return result < chance;
    }

    public static int generateRandom(int low, int high) {
        if (high - low == 0) {
            return 0;
        }
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public static double generateRandom(double a, double b) {
        if (b < a) {
            return Math.random() * (a - b + 1) + b;
        }
        return Math.random() * (b - a + 1) + a;
    }

    public static void spawnFireworks(@NotNull Location location, int amount, @NotNull Color color, boolean flicker) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(flicker).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for (int i = 0; i < amount; i++) {
            Firework fw2 = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    /**
     * Removes exactly count number of items of material from player's inventory
     *
     * @param player   Player
     * @param count    Number of items
     * @param material The material to remove
     * @return true if the operation is possible, false otherwise
     */
    public static int removeAvailable(Player player, int count, Material material) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        int totalCount = 0;

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null) {
                continue;
            }
            if (!item.getType().equals(material)) {
                continue;
            }
            items.put(i, item);
            totalCount += item.getAmount();
            player.getInventory().setItem(i, null);
        }
        if (totalCount < count) {
            count = totalCount;
        }

        int tmpCount = count;

        while (tmpCount > 0) {
            for (Integer index : items.keySet()) {
                ItemStack item = items.get(index);
                if (item.getAmount() >= tmpCount) {
                    item.setAmount(item.getAmount() - tmpCount);
                    tmpCount = 0;
                    break;
                }
                tmpCount -= items.get(index).getAmount();
                item.setType(Material.AIR);
                //items.remove(index);
            }
        }
        items.keySet().forEach(index -> player.getInventory().setItem(index, items.get(index)));
        return count;
    }

    public static int addAvailable(Player player, Material material, int count, boolean add) {
        int freeSpots = 0;
        for (ItemStack item : player.getInventory()) {
            if (item != null) {
                if (item.getType().equals(material)) {
                    freeSpots += 64 - item.getAmount();
                }
            } else {
                freeSpots += 64;
            }
        }
        count = Math.min(count, freeSpots);
        if (add) {
            player.getInventory().addItem(new ItemStack(material, count));
        }
        return count;

    }

    public static SlotPos getSlotPosition(int slot) {
        /*
        #########
        #########
        #########
        #########
        #########
        #########
        10
         */

        slot++;
        int row = slot / 9; //6
        int column = slot % 9;
        if (column == 0) {
            column = 9;
            row--;
        }
        return new SlotPos(row, column - 1);
    }


}
