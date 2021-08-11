package dev.lightdream.unchartedcore.commands;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public abstract class Command {

    public final @NotNull List<String> aliases;
    public final @NotNull String description;
    public final @NotNull String permission;
    public final boolean onlyForPlayers;
    public final boolean onlyForConsole;
    public final String usage;
    public final Main plugin;

    /**
     * @param plugin Main class
     * @param aliases /command [alias]
     * @param description Description of the sub command
     * @param permission The permission needed to use the subcommand
     * @param onlyForPlayers Weather the command can only be used by players
     * @param onlyForConsole Weather the command can only be used by conosle
     * @param usage /command alias [description]
     */
    public Command(@NotNull Main plugin, @NotNull List<String> aliases, @NotNull String description, @NotNull String permission, boolean onlyForPlayers, boolean onlyForConsole, @NotNull String usage) {
        this.plugin = plugin;
        this.aliases = aliases;
        this.description = description;
        if (permission.equals("")) {
            this.permission = Main.PROJECT_ID + "." + aliases.get(0);
        } else {
            this.permission = Main.PROJECT_ID + "." + permission;
        }
        this.onlyForPlayers = onlyForPlayers;
        this.onlyForConsole = onlyForConsole;
        this.usage = "/" + Main.PROJECT_ID + " " + aliases.get(0) + " " + usage;
    }

    public abstract void execute(CommandSender sender, List<String> args);

    public abstract List<String> onTabComplete(CommandSender sender, List<String> args);

    /**
     * @param sender The entity who executed the command
     */
    public void sendUsage(CommandSender sender) {
        MessageUtils.sendMessage(sender, usage);
    }
}
