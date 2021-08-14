package dev.lightdream.unchartedcore.modules.rename;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import dev.lightdream.unchartedcore.utils.NbtUtils;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class GiveRenameTagCommand extends Command {
    /**
     * @param plugin Main class
     */
    public GiveRenameTagCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("giveRenameTag"), "", "", false, false, "[player]");
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
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(user.uuid);
        if (!offlinePlayer.isOnline()) {
            MessageUtils.sendMessage(sender, plugin.getMessages().userNotOnline);
            return;
        }
        Player player = offlinePlayer.getPlayer();
        player.getInventory().addItem(NbtUtils.setNBT(ItemBuilder.makeItem(RenameModule.instance.settings.tag), "rename_tag", true));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
