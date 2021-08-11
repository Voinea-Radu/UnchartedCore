package dev.lightdream.unchartedcore.modules.signshop;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.databases.SignShop;
import dev.lightdream.unchartedcore.files.dto.SignShopEntry;
import dev.lightdream.unchartedcore.utils.Utils;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignShopEvents implements Listener {

    private final Main plugin;
    private final List<SignShopSession> sessions = new ArrayList<>();
    public List<Player> deleteSession = new ArrayList<>();

    public SignShopEvents(Main plugin) {
        this.plugin = plugin;

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<SignShopSession> toRemove = new ArrayList<>();
            sessions.forEach(session -> {
                if (System.currentTimeMillis() - session.updateTimestamp >= SignShopModule.instance.settings.signShopRetention * 1000L) {
                    removeSignShopSession(session);
                    toRemove.add(session);
                }
            });
            toRemove.forEach(sessions::remove);

        }, 0, SignShopModule.instance.settings.signShopRetention * 10L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DatabaseUtils.getUser(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onSignEdit(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getLine(0).equals("[SIGN_SHOP]")) {
            SignShop signShop = new SignShop(event.getBlock().getLocation(), event.getLine(1), event.getLine(2));
            DatabaseUtils.getSignShopList().add(signShop);
            Bukkit.getScheduler().runTaskLater(plugin, signShop::process, 10);
            MessageUtils.sendMessage(player, plugin.getMessages().signShopCreated);
        }
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        Block block = event.getClickedBlock();
        if (!block.getType().equals(Material.WALL_SIGN) && !block.getType().equals(Material.SIGN)) {
            return;
        }

        SignShop signShop = DatabaseUtils.getSignShop(block.getLocation());
        if (signShop == null) {
            return;
        }

        if (deleteSession.contains(player)) {
            DatabaseUtils.getSignShopList().remove(signShop);
            MessageUtils.sendMessage(player, plugin.getMessages().signShopDeleted);
            return;
        }

        event.setCancelled(true);
        SignShopEntry data = signShop.getDetails();
        double price;
        int count;

        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (!player.isSneaking()) {
                count = SignShopModule.instance.settings.leftClick;
            } else {
                count = SignShopModule.instance.settings.shiftLeftClick;
            }
        } else {
            if (!player.isSneaking()) {
                count = SignShopModule.instance.settings.rightClick;
            } else {
                count = SignShopModule.instance.settings.shiftRightClick;
            }
        }

        switch (signShop.type) {
            case "BUY":
                count = Utils.addAvailable(player, signShop.getDetails().material.parseMaterial(), count, false);
                price = data.buyPrice;
                if (plugin.getEconomy().getBalance(player) >= price * count) {
                    plugin.getEconomy().withdrawPlayer(player, price * count);
                    setSessionSign(player, signShop, count * price);
                    Utils.addAvailable(player, signShop.getDetails().material.parseMaterial(), count, true);
                } else {
                    Utils.removeAvailable(player, count, signShop.getDetails().material.parseMaterial());
                }
                break;
            case "SELL":
                price = data.sellPrice;
                count = Utils.removeAvailable(player, count, signShop.getDetails().material.parseMaterial());
                plugin.getEconomy().depositPlayer(player, price * count);
                setSessionSign(player, signShop, count * price);
                break;

        }


    }

    private void setSessionSign(Player player, SignShop signShop, double amount) {
        SignShopSession checkSession = new SignShopSession(player, signShop, amount, System.currentTimeMillis());
        if (sessions.contains(checkSession)) {
            int sessionIndex = sessions.indexOf(checkSession);
            SignShopSession session = sessions.get(sessionIndex);
            session.amount += checkSession.amount;
            session.updateTimestamp = System.currentTimeMillis();
            displaySignShopSession(player, session);
            return;
        }
        sessions.add(checkSession);
        displaySignShopSession(player, checkSession);
    }

    private void displaySignShopSession(Player player, SignShopSession session) {
        Block block = player.getWorld().getBlockAt(session.shop.getLocation());
        Sign sign = (Sign) block.getState();
        String[] lines = sign.getLines();
        switch (session.shop.type) {
            case "BUY":
                lines[3] = Utils.color(plugin.getMessages().signMoneySpent.replace("%amount%", String.valueOf(session.amount)));
                break;
            case "SELL":
                lines[3] = Utils.color(plugin.getMessages().signMoneyEarned.replace("%amount%", String.valueOf(session.amount)));
                break;
        }
        player.sendSignChange(session.shop.getLocation(), lines);
    }

    private void removeSignShopSession(SignShopSession session) {
        if (!session.player.isOnline()) {
            return;
        }
        Block block = session.player.getWorld().getBlockAt(session.shop.getLocation());
        if (!block.getType().equals(Material.SIGN) && !block.getType().equals(Material.WALL_SIGN)) {
            return;
        }
        BlockState state = block.getState();
        Sign sign = (Sign) state;
        String[] lines = sign.getLines();
        switch (session.shop.type) {
            case "BUY":
                lines[3] = Utils.color(plugin.getMessages().signBuyInfo);
                break;
            case "SELL":
                lines[3] = Utils.color(plugin.getMessages().signSellInfo);
                break;
        }
        session.player.sendSignChange(session.shop.getLocation(), lines);
    }

    @AllArgsConstructor
    private class SignShopSession {
        public Player player;
        public SignShop shop;
        public Double amount;
        public Long updateTimestamp;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SignShopSession that = (SignShopSession) o;
            return Objects.equals(player, that.player) && Objects.equals(shop, that.shop);
        }

        @Override
        public int hashCode() {
            return Objects.hash(player, shop);
        }
    }


}
