package dev.lightdream.unchartedcore.commands.commands;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.customEnchants.CustomEnchantsModule;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DevCommand extends Command {

    public DevCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("dev"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        Player player = (Player) sender;

        CustomEnchantsModule.enchantItem(player.getInventory().getItemInHand(), CustomEnchantsModule.instance.cactusProtection, 1, true);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
