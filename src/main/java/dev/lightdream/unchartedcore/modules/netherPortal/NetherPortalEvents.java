package dev.lightdream.unchartedcore.modules.netherPortal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class NetherPortalEvents implements Listener {

    @EventHandler
    public void onNetherPortalCreation(PortalCreateEvent event){
        if(!event.getReason().equals(PortalCreateEvent.CreateReason.FIRE)){
            return;
        }
    }

}
