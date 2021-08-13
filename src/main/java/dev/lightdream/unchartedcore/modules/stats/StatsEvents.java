package dev.lightdream.unchartedcore.modules.stats;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.databases.StatSign;
import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.modules.signshop.SignShopModule;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class StatsEvents implements Listener {

    private final HashMap<Player, Long> joinTime = new HashMap<>();
    public HashMap<String, User> top = new HashMap<>();
    public List<String> stats = Arrays.asList(
            "Kills",
            "Deaths",
            "Online Time",
            "Traveled",
            "Join Date"
    );
    public Main plugin;
    public List<Player> deleteSession = new ArrayList<>();
    public HashMap<Player, List<Player>> killCoolDown = new HashMap<>();

    public StatsEvents() {
        this.plugin = Main.instance;
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::calculateTop, 1 * 20 * 60L / 2, StatsModule.instance.settings.topCalculate * 20L);
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        List<Player> coolDown = killCoolDown.getOrDefault(killer, new ArrayList<>());
        if (coolDown.contains(player)) {
            return;
        }
        User user = DatabaseUtils.getUser(killer.getUniqueId());
        user.kills++;
        coolDown.add(player);
        killCoolDown.put(killer, coolDown);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            List<Player> cd = killCoolDown.getOrDefault(killer, new ArrayList<>());
            cd.remove(player);
            killCoolDown.put(player, cd);
        }, StatsModule.instance.settings.killCoolDown * 20L);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        User user = DatabaseUtils.getUser(player.getUniqueId());
        user.deaths++;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        joinTime.put(player, System.currentTimeMillis());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = DatabaseUtils.getUser(player.getUniqueId());
        user.onlineTime += System.currentTimeMillis() - joinTime.get(player);
    }

    @EventHandler
    public void onPlayerWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        double distance = event.getFrom().distance(event.getTo());
        User user = DatabaseUtils.getUser(player.getUniqueId());
        user.traveledDistance += distance;
    }

    @EventHandler
    public void onSignEdit(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getLine(0).equals("[STATS]")) {
            String stat = event.getLine(1);
            if (!stats.contains(stat)) {
                return;
            }
            StatSign statSign = new StatSign(event.getBlock().getLocation(), stat);
            DatabaseUtils.getStatSignsList().add(statSign);
            Bukkit.getScheduler().runTaskLater(plugin, statSign::process, 10);
            MessageUtils.sendMessage(player, plugin.getMessages().statSignCreated);
        }
    }

    @EventHandler
    public void onSignBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        StatSign statSign = DatabaseUtils.getStatSign(block.getLocation());
        if (statSign == null) {
            return;
        }

        if (!deleteSession.contains(player)) {
            event.setCancelled(true);
            return;
        }

        DatabaseUtils.getStatSignsList().remove(statSign);
        MessageUtils.sendMessage(player, plugin.getMessages().statSignRemoved);
    }


    public void calculateTop() {
        stats.forEach(this::calculateTop);
        StatsModule.instance.processSigns();
    }

    public void calculateTop(String stat) {
        List<User> users = DatabaseUtils.getUserList();
        users.sort(new StatsComparator(stat));
        if (users.size() != 0) {
            if (stat.equals("Join Date")) {
                top.put("-" + stat, users.get(users.size() - 1));
            }
            if (users.size() != 0) {
                top.put(stat, users.get(0));
            }
        }
    }

    public double getStat(User user, String stat) {
        switch (stat) {
            case "Kills":
                return user.kills;
            case "Deaths":
                return user.deaths;
            case "Online Time":
                return user.onlineTime;
            case "Traveled":
                return user.traveledDistance;
            case "Join Date":
                return user.joinDate;
        }
        return 0.0;
    }

    public String getStat(User user, String stat, boolean str) {
        switch (stat) {
            case "Kills":
                return String.valueOf(user.kills);
            case "Deaths":
                return String.valueOf(user.deaths);
            case "Online Time":
                Long millis = user.onlineTime;
                long days = TimeUnit.MILLISECONDS.toDays(millis);
                millis -= TimeUnit.DAYS.toMillis(days);
                long hours = TimeUnit.MILLISECONDS.toHours(millis);
                millis -= TimeUnit.HOURS.toMillis(hours);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
                millis -= TimeUnit.MINUTES.toMillis(minutes);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
                return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
            case "Traveled":
                return String.valueOf(Math.floor(user.traveledDistance));
            case "Join Date":
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(user.joinDate);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                return day + ":" + month + ":" + year;
        }
        return "";
    }

    @EventHandler()
    public void onStatsCommandSend(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        System.out.println(message);
        String[] parts = message.split(" ");
        if (parts[0].equals("/stat")||parts[0].equals("/stats")) {
            event.setCancelled(true);
            StatsModule.instance.statsCommand.execute(player, new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)));
        }
    }



    public static class StatsComparator implements Comparator<User> {

        private final String stat;

        public StatsComparator(String stat) {
            this.stat = stat;
        }

        @Override
        public int compare(User u1, User u2) {
            return Double.compare(StatsModule.instance.events.getStat(u1, stat), StatsModule.instance.events.getStat(u2, stat)) * -1;
        }
    }
}
