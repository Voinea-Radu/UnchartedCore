package dev.lightdream.unchartedcore.modules.signshop;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.databases.SignShop;
import dev.lightdream.unchartedcore.files.dto.SignShopEntry;
import dev.lightdream.unchartedcore.utils.Utils;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import dev.lightdream.unchartedcore.utils.init.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Arrays;
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
            SignShop signShop = new SignShop(event.getBlock().getLocation(), event.getLine(1), Integer.parseInt(event.getLine(2)));
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

        BuySellResponse response;
        switch (signShop.type) {
            case "BUY":
                response = buy(count, player, data.material.parseMaterial(), data.data, true);
                if (response.response) {
                    setSessionSign(player, signShop, response.count * response.price);
                    break;
                }
                if (response.price == -1) {
                    setSessionSign(player, signShop, plugin.getMessages().cannotBeBought);
                    break;
                }
                setSessionSign(player, signShop, plugin.getMessages().signShopNotEnoughMoney);
                break;
            case "SELL":
                response = sell(count, player, data.material.parseMaterial(), data.data, true);
                if (response.response) {
                    setSessionSign(player, signShop, response.count * response.price);
                    break;
                }
                if (response.price == -1) {
                    setSessionSign(player, signShop, plugin.getMessages().cannotBeSold);
                    break;
                }
                setSessionSign(player, signShop, plugin.getMessages().signShopNotEnoughItems);
                break;
        }
    }

    @EventHandler()
    public void onSellCommandSend(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String[] parts = message.split(" ");
        if (parts[0].equals("/sell")) {
            event.setCancelled(true);
            SignShopModule.instance.sellCommand.execute(player, new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)));
        }

    }

    public BuySellResponse buy(int count, Player player, Material material, String dta, boolean sign) {
        count = Utils.addAvailable(player, material, count, false);
        SignShopEntry data = SignShopModule.instance.settings.getEntryByMaterial(material.toString(), dta);
        double price = data.buyPrice;
        if (price == -1) {
            if (!sign) {
                MessageUtils.sendMessage(player, plugin.getMessages().cannotBeBought);
            }
            return new BuySellResponse(0, -1.0, false);
        }
        if (plugin.getEconomy().getBalance(player) >= price * count) {
            plugin.getEconomy().withdrawPlayer(player, price * count);
            if (material.equals(Material.MOB_SPAWNER)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "silkspawner give " + player.getName() + " " + data.data + " " + count);
            } else {
                Utils.addAvailable(player, data.material.parseMaterial(), count, true);
            }
            return new BuySellResponse(count, price, true);
        } else {
            if (!sign) {
                MessageUtils.sendMessage(player, plugin.getMessages().signShopNotEnoughMoney);
            }
            return new BuySellResponse(count, price, false);
        }
    }

    public BuySellResponse sell(int count, Player player, Material material, String dta, boolean sign) {
        if (material.equals(Material.MOB_SPAWNER)) {
            if (!sign) {
                MessageUtils.sendMessage(player, plugin.getMessages().invalidItem);
            }
            return new BuySellResponse(0, 0.0, false);
        }
        SignShopEntry data = SignShopModule.instance.settings.getEntryByMaterial(material.toString(), dta);
        if (data == null) {
            if (!sign) {
                MessageUtils.sendMessage(player, plugin.getMessages().invalidItem);
            }
            return new BuySellResponse(0, 0.0, false);
        }
        double price = data.sellPrice;
        if (price == -1) {
            if (!sign) {
                MessageUtils.sendMessage(player, plugin.getMessages().cannotBeSold);
            }
            return new BuySellResponse(0, -1.0, false);
        }
        count = Utils.removeAvailable(player, count, material);
        plugin.getEconomy().depositPlayer(player, price * count);
        if (count == 0) {
            if (!sign) {
                MessageUtils.sendMessage(player, plugin.getMessages().signShopNotEnoughItems);
            }
            return new BuySellResponse(count, price, false);
        }
        return new BuySellResponse(count, price, true);

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

    private void setSessionSign(Player player, SignShop signShop, String message) {
        SignShopSession checkSession = new SignShopSession(player, signShop, 0.0, System.currentTimeMillis());
        if (sessions.contains(checkSession)) {
            int sessionIndex = sessions.indexOf(checkSession);
            SignShopSession session = sessions.get(sessionIndex);
            session.updateTimestamp = System.currentTimeMillis();
            displaySignShopSession(player, session, message);
            return;
        }
        sessions.add(checkSession);
        displaySignShopSession(player, checkSession, message);
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

    private void displaySignShopSession(Player player, SignShopSession session, String mesage) {
        Block block = player.getWorld().getBlockAt(session.shop.getLocation());
        Sign sign = (Sign) block.getState();
        String[] lines = sign.getLines();
        switch (session.shop.type) {
            case "BUY":
                lines[3] = Utils.color(mesage);
                break;
            case "SELL":
                lines[3] = Utils.color(mesage);
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
    @NoArgsConstructor
    public class BuySellResponse {
        public Integer count;
        public Double price;
        public Boolean response;
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
