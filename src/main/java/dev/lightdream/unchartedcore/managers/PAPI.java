package dev.lightdream.unchartedcore.managers;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.modules.kits.KitsModule;
import dev.lightdream.unchartedcore.modules.sotw.SOTWModule;
import dev.lightdream.unchartedcore.modules.stats.StatsModule;
import dev.lightdream.unchartedcore.utils.Utils;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"IfStatementWithIdenticalBranches", "unused", "FieldCanBeLocal"})
public class PAPI extends PlaceholderExpansion {

    private final Main spigot;

    public PAPI(Main spigot) {
        this.spigot = spigot;
    }


    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return "L1ghtDream";
    }

    @Override
    public @NotNull String getIdentifier() {
        return Main.PROJECT_NAME.toLowerCase();
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if (player == null) {
            return null;
        }

        User user = DatabaseUtils.getUser(player.getUniqueId());

        switch (identifier) {
            case "kills":
                return StatsModule.instance.events.getStat(user, "Kills", true);
            case "deaths":
                return StatsModule.instance.events.getStat(user, "Deaths", true);
            case "onlineTime":
                return StatsModule.instance.events.getStat(user, "Online Time", true);
            case "traveled":
                return StatsModule.instance.events.getStat(user, "Traveled", true);
            case "joinDate":
                return StatsModule.instance.events.getStat(user, "Join Date", true);
            case "kitRankup":
                int level = 1;
                for (Integer upDate : KitsModule.instance.settings.upDates) {
                    if (System.currentTimeMillis() - SOTWModule.instance.settings.startDate > upDate * 1000 * 60 * 60L) {
                        level++;
                    }
                }
                if (KitsModule.instance.settings.upDates.size() >= level) {
                    return Utils.msToTime(SOTWModule.instance.settings.startDate - KitsModule.instance.settings.upDates.get(level) * 100 * 60 * 60L);
                } else {
                    return "MAX LEVEL";
                }

        }

        return null;
    }
}
