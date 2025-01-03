package dev.lightdream.unchartedcore.modules.combatLog;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import dev.lightdream.unchartedcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CombatLogEvents implements Listener {

    public HashMap<Player, Long> combat = new HashMap<>();


    @EventHandler
    public void onCombatEnter(EntityDamageByEntityEvent event) {
        List<Player> players = getPlayers(event);
        if (players == null) {
            return;
        }
        Player damager = players.get(0);
        Player player = players.get(1);

        if (player == damager) {
            event.setCancelled(true);
            return;
        }

        combat.put(player, System.currentTimeMillis());
        combat.put(damager, System.currentTimeMillis());
        Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
            if (System.currentTimeMillis() - combat.getOrDefault(player, 0L) > CombatLogModule.instance.settings.combatLogTime) {
                combat.remove(player);
            }
            if (System.currentTimeMillis() - combat.getOrDefault(damager, 0L) > CombatLogModule.instance.settings.combatLogTime) {
                combat.remove(damager);
            }
        }, CombatLogModule.instance.settings.combatLogTime * 20L);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        combat.remove(event.getEntity());
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        FLocation l1 = new FLocation(event.getFrom());
        FLocation l2 = new FLocation(event.getTo());
        Faction f1 = Board.getInstance().getFactionAt(l1);
        Faction f2 = Board.getInstance().getFactionAt(l2);
        if (f1.isWarZone() && !f2.isWarZone()) {
            if (combat.containsKey(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        FLocation l1 = new FLocation(event.getFrom());
        FLocation l2 = new FLocation(event.getTo());
        Faction f1 = Board.getInstance().getFactionAt(l1);
        Faction f2 = Board.getInstance().getFactionAt(l2);
        if (f1.isWarZone() && !f2.isWarZone()) {
            if (combat.containsKey(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    public List<Player> getPlayers(EntityDamageByEntityEvent event) {
        boolean arrow = false;
        if (!(event.getDamager() instanceof Player)) {
            if (!(event.getDamager() instanceof Arrow)) {
                return null;
            }
            arrow = true;
        }
        if (!(event.getEntity() instanceof Player)) {
            return null;
        }
        Player damager;
        if (arrow) {
            if (!(((Arrow) event.getDamager()).getShooter() instanceof Player)) {
                return null;
            }
            damager = (Player) ((Arrow) event.getDamager()).getShooter();
        } else {
            damager = (Player) event.getDamager();
        }
        Player player = (Player) event.getEntity();
        return Arrays.asList(damager, player);
    }

}
