package dev.lightdream.unchartedcore.utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class NbtUtils {

    public static @NotNull ItemStack setNBT(@NotNull ItemStack item, @NotNull String attribute, @NotNull Object value) {
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound nbtCompound = nbtItem.addCompound("settings");
        nbtCompound.setObject(attribute, value);
        return nbtItem.getItem();
    }

    public static @Nullable Object getNBT(@NotNull ItemStack item, @NotNull String attribute) {
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound nbtCompound = nbtItem.addCompound("settings");
        return nbtCompound.getObject(attribute, Object.class);
    }

}
