package dev.lightdream.unchartedcore.modules.warZoneClaims;

import com.massivecraft.factions.*;
import com.massivecraft.factions.event.LandClaimEvent;
import com.massivecraft.factions.event.LandUnclaimEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class WarZoneClaimsEvents implements Listener {

    private final List<Player> inEvent = new ArrayList<>();

    @EventHandler
    public void onClaim(LandClaimEvent event) {
        int blockCount = 0;
        FPlayer fPlayer = event.getfPlayer();

        if (inEvent.contains(fPlayer.getPlayer())) {
            return;
        }

        inEvent.add(fPlayer.getPlayer());

        Faction faction = event.getFaction();
        FLocation location = event.getLocation();
        FLocation[][] map = new FLocation[3][3];
        boolean nearWarZone = false;

        map[0][0] = new FLocation(location.getWorldName(), (new Long(location.getX() - 1)).intValue(), (new Long(location.getZ() - 1)).intValue());
        map[0][1] = new FLocation(location.getWorldName(), (new Long(location.getX() - 1)).intValue(), (new Long(location.getZ())).intValue());
        map[0][2] = new FLocation(location.getWorldName(), (new Long(location.getX() - 1)).intValue(), (new Long(location.getZ() + 1)).intValue());
        map[1][0] = new FLocation(location.getWorldName(), (new Long(location.getX())).intValue(), (new Long(location.getZ() - 1)).intValue());
        map[1][1] = new FLocation(location.getWorldName(), (new Long(location.getX())).intValue(), (new Long(location.getZ())).intValue());
        map[1][2] = new FLocation(location.getWorldName(), (new Long(location.getX())).intValue(), (new Long(location.getZ() + 1)).intValue());
        map[2][0] = new FLocation(location.getWorldName(), (new Long(location.getX() + 1)).intValue(), (new Long(location.getZ() - 1)).intValue());
        map[2][1] = new FLocation(location.getWorldName(), (new Long(location.getX() + 1)).intValue(), (new Long(location.getZ())).intValue());
        map[2][2] = new FLocation(location.getWorldName(), (new Long(location.getX() + 1)).intValue(), (new Long(location.getZ() + 1)).intValue());

        for (FLocation[] fLocations : map) {
            for (FLocation fLocation : fLocations) {
                if (Factions.getInstance().getWarZone().getAllClaims().contains(fLocation)) {
                    nearWarZone = true;
                }
            }
        }

        if (!nearWarZone) {
            return;
        }

        for (FLocation[] fLocations : map) {
            for (FLocation fLocation : fLocations) {
                if (fPlayer.canClaimForFactionAtLocation(fPlayer.getFaction(), fLocation, false)) {
                    fPlayer.attemptClaim(fPlayer.getFaction(), fLocation, false);
                } else {
                    blockCount++;
                }
            }
        }
        if (blockCount > 3) {
            for (FLocation[] fLocations : map) {
                for (FLocation fLocation : fLocations) {
                    if (faction.getAllClaims().contains(fLocation)) {
                        Board.getInstance().removeAt(fLocation);
                    }
                }
            }
        }

        inEvent.remove(fPlayer.getPlayer());
    }

    @EventHandler
    public void onLandUnclaim(LandUnclaimEvent event) {

    }


}
