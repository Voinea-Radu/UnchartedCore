package dev.lightdream.unchartedcore.modules.trails;

import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.xenondevs.particle.ParticleEffect;

public class TrailEvents implements Listener {

    @EventHandler
    public void onTrailSpawn(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = DatabaseUtils.getUser(player.getUniqueId());
        if (!user.trail) {
            return;
        }
        ParticleEffect.FLAME.display(player.getLocation());
    }

}
