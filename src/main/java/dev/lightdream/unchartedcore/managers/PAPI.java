package dev.lightdream.unchartedcore.managers;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.modules.stats.StatsModule;
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

        switch (identifier){
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
        }

        return null;
    }
}
