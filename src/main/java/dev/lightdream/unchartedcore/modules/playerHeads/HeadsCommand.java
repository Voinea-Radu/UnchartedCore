package dev.lightdream.unchartedcore.modules.playerHeads;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class HeadsCommand extends Command {
    /**
     * @param plugin Main class
     */
    public HeadsCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("heads"), "", "", false, false, "[name]");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() != 1) {
            sendUsage(sender);
            return;
        }
        User user = DatabaseUtils.getUser(args.get(0));
        if (user == null) {
            MessageUtils.sendMessage(sender, plugin.getMessages().invalidUser);
            return;
        }
        MessageUtils.sendMessage(sender, plugin.getMessages().soldTimes.replace("%count%", String.valueOf(user.headSold)));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
