package dev.lightdream.unchartedcore.modules.customEnchants;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.modules.customEnchants.dto.CustomEnchant;
import dev.lightdream.unchartedcore.utils.Utils;
import lombok.AllArgsConstructor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class CustomEnchantsEvents implements Listener {

    private final Main plugin;
    private final HashMap<Player, Combo> combos = new HashMap<>();

    public CustomEnchantsEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerTouchCactus(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        if (event.getCause() != EntityDamageEvent.DamageCause.CONTACT) {
            return;
        }

        if (checkEnchant(player, CustomEnchantsModule.instance.cactusProtection, CustomEnchantsModule.instance.settings.cactusProtection.chance).use) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCombo(EntityDamageByEntityEvent event) {
        List<Player> players = getPlayers(event);
        if (players == null) {
            return;
        }
        Player damager = players.get(0);
        Player player = players.get(1);

        //Breaks combo for player
        combos.remove(player);

        double damage = event.getDamage();

        EnchantCheck check = checkEnchant(damager, CustomEnchantsModule.instance.comboBoost, CustomEnchantsModule.instance.settings.comboBoost.chance);
        if (check.use) {
            Combo combo = combos.getOrDefault(damager, null);
            if (combo == null) {
                combo = new Combo(player, 0, 0);
            }
            if (combo.player != player) {
                combo.player = player;
                combo.count = 0;
            }
            if (combo.timeStamp != 0) {
                if (System.currentTimeMillis() - Long.parseLong(CustomEnchantsModule.instance.settings.comboBoost.args.get(1)) * check.level > combo.timeStamp) {
                    combos.remove(damager);
                    return;
                }
            }
            double boost = damage * Double.parseDouble(CustomEnchantsModule.instance.settings.comboBoost.args.get(0)) * check.level;
            damage += boost * combo.count;

            combo.count++;
            combo.timeStamp = System.currentTimeMillis();

            combos.put(damager, combo);
        }


    }

    @EventHandler
    public void onPlayerAntiDepthStrider(EntityDamageByEntityEvent event) {
        List<Player> players = getPlayers(event);
        if (players == null) {
            return;
        }
        Player damager = players.get(0);
        Player player = players.get(1);

        if (!checkEnchant(player, Enchantment.DEPTH_STRIDER, 100).use) {
            return;
        }
        EnchantCheck check = checkEnchant(damager, CustomEnchantsModule.instance.antiDepthStrider, CustomEnchantsModule.instance.settings.antiDepthStrider.chance);

        if (check.use) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * Integer.parseInt(CustomEnchantsModule.instance.settings.antiDepthStrider.args.get(0)) * check.level, Integer.parseInt(CustomEnchantsModule.instance.settings.antiDepthStrider.args.get(1)) * check.level), true);
        }
    }

    @EventHandler
    public void onPlayerCritical(EntityDamageByEntityEvent event) {
        List<Player> players = getPlayers(event);
        if (players == null) {
            return;
        }
        Player damager = players.get(0);

        EnchantCheck check = checkEnchant(damager, CustomEnchantsModule.instance.critical, CustomEnchantsModule.instance.settings.critical.chance);
        if (check.use) {
            double damage = event.getDamage();
            double boost = damage * Double.parseDouble(CustomEnchantsModule.instance.settings.critical.args.get(0)) * check.level;
            event.setDamage(damage + boost);
        }

    }

    @EventHandler
    public void onPotionSpell(EntityDamageByEntityEvent event) {
        List<Player> players = getPlayers(event);
        if (players == null) {
            return;
        }
        Player damager = players.get(0);
        Player player = players.get(1);

        CustomEnchantsModule.instance.potionEnchants.forEach((potion, enchantment) -> {
            CustomEnchant enchantSettings = CustomEnchantsModule.instance.settings.getPotionEnchant(potion).enchantSettings;
            EnchantCheck check = checkEnchant(damager, enchantment, enchantSettings.chance);
            if (check.use) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * Integer.parseInt(enchantSettings.args.get(0)) * check.level, Integer.parseInt(enchantSettings.args.get(1)) * check.level), true);
            }
        });
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

    public EnchantCheck checkEnchant(Player player, Enchantment enchantment, double chance) {
        List<ItemStack> items = new ArrayList<>();
        Collections.addAll(items, player.getInventory().getArmorContents());
        items.add(player.getItemInHand());
        int totalLevel = 0;

        for (ItemStack item : items) {
            if (item.getEnchantments().containsKey(enchantment)) {
                int level = item.getEnchantments().get(enchantment);
                if (Utils.checkExecute(chance * level)) {
                    totalLevel += level;
                }
            }
        }
        return new EnchantCheck(totalLevel != 0, totalLevel);
    }

    @AllArgsConstructor
    public static class Combo {
        public Player player;
        public int count;
        public long timeStamp;
    }

    @AllArgsConstructor
    public static class EnchantCheck {
        public boolean use;
        public int level;
    }

}
