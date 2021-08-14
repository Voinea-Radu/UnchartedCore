package dev.lightdream.unchartedcore.modules.homes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class HomesEvents implements Listener {

    @EventHandler()
    public void onHomeCommandSend(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String[] parts = message.split(" ");
        if (parts[0].equals("/home") || parts[0].equals("/homes")) {
            event.setCancelled(true);
            HomesModule.instance.homeCommand.execute(player, new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)));
        }
    }

}
