package dev.lightdream.unchartedcore.modules.signshop;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SignShopCommand extends Command {
    /**
     * @param plugin Main class
     */
    public SignShopCommand(@NotNull Main plugin, SignShopModule module) {
        super(plugin, Collections.singletonList("signshop"), "", "", true, false, "delete");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() != 1) {
            sendUsage(sender);
            return;
        }
        Player player = (Player) sender;
        if (SignShopModule.instance.events.deleteSession.contains(player)) {
            SignShopModule.instance.events.deleteSession.remove(player);
            MessageUtils.sendMessage(player, plugin.getMessages().signShopDeletedSessionStopped);
        } else {
            SignShopModule.instance.events.deleteSession.add(player);
            MessageUtils.sendMessage(player, plugin.getMessages().signShopDeletedSessionStarted);
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            return Arrays.asList("delete");
        } else {
            return new ArrayList<>();
        }
    }
}
