package dev.lightdream.unchartedcore.managers;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfStringBuilder", "DuplicatedCode"})
public class CommandManager implements CommandExecutor, TabCompleter {

    private final Main plugin;
    private final List<Command> commands;

    /**
     * @param plugin   Main class
     * @param command  /[command]
     * @param commands /command [commands]
     */
    public CommandManager(Main plugin, String command, List<Command> commands) {
        this.plugin = plugin;
        plugin.getCommand(command).setExecutor(this);
        plugin.getCommand(command).setTabCompleter(this);
        this.commands = commands;
        plugin.getModules().forEach(module -> {
            if (module.registerCommands() != null) {
                commands.addAll(module.registerCommands());
            }
        });
        this.commands.sort(Comparator.comparing(com -> com.aliases.get(0)));
    }

    /**
     * Unregister a subcommand
     *
     * @param command /main-command [command]
     */
    public void unregisterCommand(Command command) {
        commands.remove(command);
    }

    /**
     * Send the config specified or auto-generated help command
     *
     * @param sender Command sender
     */
    public void sendUsage(CommandSender sender) {
        StringBuilder helpCommandOutput = new StringBuilder();
        helpCommandOutput.append("\n");

        if (plugin.getMessages().helpCommand.size() == 0) {
            for (Command command : plugin.getCommands()) {
                if (sender.hasPermission(command.permission)) {
                    helpCommandOutput.append(command.usage);
                    helpCommandOutput.append("\n");
                }
            }
        } else {
            for (String line : plugin.getMessages().helpCommand) {
                helpCommandOutput.append(line);
                helpCommandOutput.append("\n");
            }
        }

        MessageUtils.sendMessage(sender, helpCommandOutput.toString());
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command bukkitCommand, String label, String[] args) {
        if (args.length == 0) {
            for (Command command : commands) {
                if (command.aliases.get(0).equals("")) {
                    command.execute(sender, Arrays.asList(args));
                    return true;
                }
            }
            sendUsage(sender);
            return true;
        }

        for (Command command : commands) {
            if (!(command.aliases.contains(args[0]))) {
                continue;
            }

            if (command.onlyForPlayers && !(sender instanceof Player)) {
                MessageUtils.sendMessage(sender, plugin.getMessages().mustBeAPlayer);
                return true;
            }

            if (command.onlyForConsole && !(sender instanceof ConsoleCommandSender)) {
                MessageUtils.sendMessage(sender, plugin.getMessages().mustBeConsole);
                return true;
            }

            if (!hasPermission(sender, command.permission)) {
                MessageUtils.sendMessage(sender, plugin.getMessages().noPermission);
                return true;
            }

            command.execute(sender, new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
            return true;
        }

        MessageUtils.sendMessage(sender, plugin.getMessages().unknownCommand);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bukkitCommand, String bukkitAlias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> result = new ArrayList<>();
            for (Command command : commands) {
                for (String alias : command.aliases) {
                    if (alias.toLowerCase().startsWith(args[0].toLowerCase()) && hasPermission(sender, command.permission)) {
                        result.add(alias);
                    }
                }
            }
            return result;
        }

        for (Command command : commands) {
            if (command.aliases.contains(args[0]) && hasPermission(sender, command.permission)) {
                return command.onTabComplete(sender, new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
            }
        }

        return Collections.emptyList();
    }

    /**
     * @param sender     The command sender
     * @param permission The permission node
     * @return Whether the sender has permission or not
     */
    private boolean hasPermission(CommandSender sender, String permission) {
        return ((sender.hasPermission(permission) || permission.equalsIgnoreCase("")));
    }
}
