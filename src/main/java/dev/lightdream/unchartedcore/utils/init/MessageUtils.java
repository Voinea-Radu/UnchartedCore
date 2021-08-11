package dev.lightdream.unchartedcore.utils.init;

import de.themoep.minedown.MineDown;
import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class MessageUtils {

    private static Main plugin;
    private static boolean useMineDown;

    public static void init(Main main) {
        plugin = main;

        List<String> mineDownVersions = Arrays.asList("1.16", "1.17");
        boolean useMineDown = false;
        for (String version : mineDownVersions) {
            if (Bukkit.getServer().getVersion().contains(version)) {
                useMineDown = true;
                break;
            }
        }
        MessageUtils.useMineDown = useMineDown;
    }


    public static void sendMessage(Object target, String message) {
        if (target instanceof CommandSender) {
            ((CommandSender) target).sendMessage(Utils.color(plugin.getMessages().prefix + message));
        }
    }

    public static void sendMessage(String target, String message) {
        sendMessage(DatabaseUtils.getUser(target), message);
    }

    public static void sendMessage(User user, String message) {
        if (user != null) {
            sendMessage(user.uuid, message);
        }
    }

    public static void sendMessage(UUID target, String message) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(target);
        if (player != null) {
            if (player.isOnline()) {
                sendMessage((Player) player, message);
            }
        }
    }

    private static void sendMessage(Player target, String message) {
        if (useMineDown) {
            target.spigot().sendMessage(new MineDown(plugin.getMessages().prefix + message).toComponent());
        } else {
            target.sendMessage(Utils.color(plugin.getMessages().prefix + message));
        }
    }
}
