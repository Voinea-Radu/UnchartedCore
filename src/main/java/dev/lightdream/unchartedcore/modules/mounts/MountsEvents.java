package dev.lightdream.unchartedcore.modules.mounts;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;

public class MountsEvents implements Listener {

    public HashMap<Player, Entity> mounts = new HashMap<>();

    @EventHandler
    public void onRiderDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity();
        mounts.get(player).remove();
    }

}
