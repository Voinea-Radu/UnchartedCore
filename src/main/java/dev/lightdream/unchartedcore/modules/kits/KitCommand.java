package dev.lightdream.unchartedcore.modules.kits;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.databases.Kit;
import dev.lightdream.unchartedcore.modules.sotw.SOTWModule;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitCommand extends Command {
    /**
     * @param plugin Main class
     */
    public KitCommand(@NotNull Main plugin) {
        super(plugin, Arrays.asList("kit", "kits"), "", "", true, false, "[name/delete/create/list] [name]");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            sendUsage(sender);
            return;
        }
        Player player = (Player) sender;
        if (args.size() == 1) {

            if (args.get(0).equals("list")) {
                StringBuilder kits = new StringBuilder(plugin.getMessages().kits);
                for (Kit kt : DatabaseUtils.getKitList()) {
                    kits.append(kt.name);
                }
                MessageUtils.sendMessage(player, kits.toString());
                return;
            }

            Kit kit = DatabaseUtils.getKit(args.get(0));
            if (kit == null) {
                MessageUtils.sendMessage(sender, plugin.getMessages().kitDoesNotExist);
                return;
            }
            if (!player.hasPermission("uc.kits." + args.get(0))) {
                MessageUtils.sendMessage(player, plugin.getMessages().noPermission);
                return;
            }
            try {
                int level = 1;
                for (Integer upDate : KitsModule.instance.settings.upDates) {
                    if (System.currentTimeMillis() - SOTWModule.instance.settings.startDate > upDate * 1000 * 60 * 60L) {
                        level++;
                    }
                }

                kit.getKit(level).forEach(item -> {
                    if (item != null) {
                        player.getInventory().addItem(item);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if (!player.hasPermission(this.permission + ".admin")) {
            MessageUtils.sendMessage(player, plugin.getMessages().noPermission);
            return;
        }
        Kit kit;
        switch (args.get(0)) {
            case "create":
                kit = DatabaseUtils.getKit(args.get(1));
                List<ItemStack> items = new ArrayList<>();
                for (ItemStack itemStack : player.getInventory()) {
                    items.add(itemStack);
                }
                if (kit != null) {
                    kit.kits = kit.kits + " " + ItemBuilder.itemStackArrayToBase64(items);
                } else {
                    DatabaseUtils.getKitList().add(new Kit(args.get(1), ItemBuilder.itemStackArrayToBase64(items)));
                }
                MessageUtils.sendMessage(sender, plugin.getMessages().kitCreated);
                break;
            case "delete":
                kit = DatabaseUtils.getKit(args.get(1));
                if (kit == null) {
                    MessageUtils.sendMessage(sender, plugin.getMessages().kitDoesNotExist);
                    return;
                }
                DatabaseUtils.getKitList().remove(kit);
                MessageUtils.sendMessage(sender, plugin.getMessages().kitDeleted);
        }


    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
