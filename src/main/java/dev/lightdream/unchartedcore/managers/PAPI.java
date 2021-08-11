package dev.lightdream.unchartedcore.managers;

import dev.lightdream.unchartedcore.Main;
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

        //Placeholders

        return null;
    }
}
