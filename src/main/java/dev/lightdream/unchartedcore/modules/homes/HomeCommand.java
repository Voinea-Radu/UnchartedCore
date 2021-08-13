package dev.lightdream.unchartedcore.modules.homes;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.databases.Home;
import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class HomeCommand extends Command {
    /**
     * @param plugin Main class
     */
    public HomeCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("home"), "", "", true, false, "[home/set/remove/list] [name]");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            sendUsage(sender);
            return;
        }
        Player player = (Player) sender;
        User user = DatabaseUtils.getUser(player.getUniqueId());
        Home home;
        switch (args.get(0)) {
            case "set":
                home = DatabaseUtils.getHome(user, args.get(1));
                if (home != null) {
                    MessageUtils.sendMessage(player, plugin.getMessages().homeAlreadyExists);
                    break;
                }
                if (args.size() != 2) {
                    sendUsage(sender);
                    break;
                }
                home = new Home(player.getLocation(), player.getUniqueId(), args.get(1));
                DatabaseUtils.getHomesList().add(home);
                MessageUtils.sendMessage(player, plugin.getMessages().homeSet);
                break;
            case "remove":
                if (args.size() != 2) {
                    sendUsage(sender);
                    break;
                }
                home = DatabaseUtils.getHome(user, args.get(1));
                if (home == null) {
                    MessageUtils.sendMessage(player, plugin.getMessages().homeDoesNotExist);
                    break;
                }
                DatabaseUtils.getHomesList().remove(home);
                MessageUtils.sendMessage(player, plugin.getMessages().homeDeleted);
                break;
            case "list":
                List<Home> homes = DatabaseUtils.getHomes(user);
                StringBuilder output = new StringBuilder();
                for (Home h : homes) {
                    output.append(h.name).append(" ");
                }
                MessageUtils.sendMessage(player, plugin.getMessages().homes.replace("%homes%", output.toString()));
                break;
            default:
                home = DatabaseUtils.getHome(user, args.get(0));
                if (home == null) {
                    MessageUtils.sendMessage(player, plugin.getMessages().homeDoesNotExist);
                    break;
                }
                player.teleport(home.getLocation());
                break;
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
