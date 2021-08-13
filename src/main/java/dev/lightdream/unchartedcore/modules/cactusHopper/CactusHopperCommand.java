package dev.lightdream.unchartedcore.modules.cactusHopper;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import dev.lightdream.unchartedcore.utils.NbtUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CactusHopperCommand extends Command {
    /**
     * @param plugin Main class
     */
    public CactusHopperCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("cactusHooper"), "", "", false, false, "[player]");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() != 1) {
            sendUsage(sender);
            return;
        }
        Player player = Bukkit.getPlayer(args.get(0));
        if (player == null) {
            MessageUtils.sendMessage(sender, plugin.getMessages().invalidUser);
            return;
        }
        player.getInventory().addItem(NbtUtils.setNBT(ItemBuilder.makeItem(CactusHopperModule.instance.settings.hopper), "cactus_hopper", true));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
