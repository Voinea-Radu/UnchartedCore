package dev.lightdream.unchartedcore.modules.trails;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TrailCommand extends Command {
    /**
     * @param plugin Main class
     */
    public TrailCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("trail"), "", "", false, false, "[player]");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            sendUsage(sender);
            return;
        }
        User user = DatabaseUtils.getUser(args.get(0));
        if (user == null) {
            MessageUtils.sendMessage(sender, plugin.getMessages().invalidUser);
            return;
        }
        user.trail = !user.trail;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
