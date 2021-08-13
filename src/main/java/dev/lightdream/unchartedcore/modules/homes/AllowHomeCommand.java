package dev.lightdream.unchartedcore.modules.homes;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.perms.Role;
import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.databases.FactionHomeAllow;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class AllowHomeCommand extends Command {
    /**
     * @param plugin Main class
     */
    public AllowHomeCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("allowHome"), "", "", true, false, "[add/remove/list/addrole/removerole] [faction/rolename]");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            sendUsage(sender);
            return;
        }
        Player player = (Player) sender;
        FPlayer fPlayer;
        Faction playerFaction;
        Faction faction;
        FactionHomeAllow allow;

        switch (args.get(0)) {
            case "add":
                fPlayer = FPlayers.getInstance().getById(player.getUniqueId().toString());
                playerFaction = fPlayer.getFaction();
                if (args.size() != 2) {
                    sendUsage(sender);
                    break;
                }
                faction = Factions.getInstance().getByTag(args.get(1));
                if (playerFaction == null || faction == null) {
                    MessageUtils.sendMessage(player, plugin.getMessages().invalidFaction);
                    break;
                }
                if (!fPlayer.getRole().isAtLeast(Role.COLEADER)) {
                    MessageUtils.sendMessage(player, plugin.getMessages().notAllowedToAllowHomes);
                    return;
                }
                if (!DatabaseUtils.canHaveHome(playerFaction.getId(), faction.getId())) {
                    DatabaseUtils.getFactionHomeAllowList().add(new FactionHomeAllow(playerFaction.getId(), faction.getId()));
                }
                MessageUtils.sendMessage(player, plugin.getMessages().homesAllowed);
                break;
            case "remove":
                fPlayer = FPlayers.getInstance().getById(player.getUniqueId().toString());
                playerFaction = fPlayer.getFaction();
                if (args.size() != 2) {
                    sendUsage(sender);
                    break;
                }
                faction = Factions.getInstance().getByTag(args.get(1));
                if (playerFaction == null || faction == null) {
                    MessageUtils.sendMessage(player, plugin.getMessages().invalidFaction);
                    break;
                }
                if (!fPlayer.getRole().isAtLeast(Role.COLEADER)) {
                    MessageUtils.sendMessage(player, plugin.getMessages().notAllowedToAllowHomes);
                    return;
                }
                allow = DatabaseUtils.getFactionHomeAllow(playerFaction.getId(), faction.getId());
                if (allow != null) {
                    DatabaseUtils.getFactionHomeAllowList().remove(allow);
                }
                MessageUtils.sendMessage(player, plugin.getMessages().homesDenied);
                break;
            case "list":
                fPlayer = FPlayers.getInstance().getById(player.getUniqueId().toString());
                playerFaction = fPlayer.getFaction();
                List<FactionHomeAllow> allows = DatabaseUtils.getFactionHomeAllows(playerFaction.getId());
                StringBuilder output = new StringBuilder();
                for (FactionHomeAllow factionHomeAllow : allows) {
                    Faction allowedFaction = Factions.getInstance().getFactionById(factionHomeAllow.allowId);
                    output.append(allowedFaction.getTag()).append(" ");
                }
                MessageUtils.sendMessage(player, plugin.getMessages().homeAllows.replace("%allows%", output.toString()));
                break;
            case "addrole":
                fPlayer = FPlayers.getInstance().getById(player.getUniqueId().toString());
                playerFaction = fPlayer.getFaction();
                if (args.size() != 2) {
                    sendUsage(sender);
                    break;
                }
                if (!fPlayer.getRole().isAtLeast(Role.COLEADER)) {
                    MessageUtils.sendMessage(player, plugin.getMessages().notAllowedToAllowHomes);
                    return;
                }
                if (!DatabaseUtils.canHaveHome(playerFaction.getId(), args.get(1))) {
                    DatabaseUtils.getFactionHomeAllowList().add(new FactionHomeAllow(playerFaction.getId(), args.get(1)));
                }
                MessageUtils.sendMessage(player, plugin.getMessages().homesAllowed);
                break;
            case "removerole":
                fPlayer = FPlayers.getInstance().getById(player.getUniqueId().toString());
                playerFaction = fPlayer.getFaction();
                if (args.size() != 2) {
                    sendUsage(sender);
                    break;
                }
                if (!fPlayer.getRole().isAtLeast(Role.COLEADER)) {
                    MessageUtils.sendMessage(player, plugin.getMessages().notAllowedToAllowHomes);
                    return;
                }
                allow = DatabaseUtils.getFactionHomeAllow(playerFaction.getId(), args.get(1));
                if (allow != null) {
                    DatabaseUtils.getFactionHomeAllowList().remove(allow);
                }
                MessageUtils.sendMessage(player, plugin.getMessages().homesDenied);
                break;

        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
