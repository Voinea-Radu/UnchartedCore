package dev.lightdream.unchartedcore.modules.homes;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class AddHomeCommand extends Command {
    /**
     * @param plugin Main class
     */
    public AddHomeCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("addHome"), "", "", false, false, "[player] [count]");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() != 2) {
            sendUsage(sender);
            return;
        }
        User user = DatabaseUtils.getUser(args.get(0));
        if (user == null) {
            MessageUtils.sendMessage(sender, plugin.getMessages().invalidUser);
            return;
        }
        try {
            int add = Integer.parseInt(args.get(1));
            user.extraHomes += add;
        } catch (NumberFormatException e) {
            MessageUtils.sendMessage(sender, plugin.getMessages().invalidNumber);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
