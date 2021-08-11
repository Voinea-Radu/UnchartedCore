package dev.lightdream.unchartedcore.gui.executors;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@SuppressWarnings("unused")
public interface GUIExecutor {

    void execute(Player player, List<String> args, ItemStack item);

}
