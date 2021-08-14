package dev.lightdream.unchartedcore.modules.sotw;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SOTWCommand extends Command {
    /**
     * @param plugin Main class
     */
    public SOTWCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("sotw"), "", "", false, false, "[time]");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() != 1) {
            sendUsage(sender);
            return;
        }
        try {
            int time = Integer.parseInt(args.get(0));
            plugin.onDisable();
            plugin.getSettings().set(SOTWModule.instance.settings);
            Main.loadConfigs = false;
            plugin.onEnable();
            SOTWModule.instance.settings.startDate = System.currentTimeMillis();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                plugin.onDisable();
                Main.loadConfigs = true;
                plugin.onEnable();
            }, time * 20 * 60L);
        } catch (NumberFormatException exception) {
            MessageUtils.sendMessage(sender, plugin.getMessages().invalidNumber);
        }


    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
