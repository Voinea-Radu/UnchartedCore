package dev.lightdream.unchartedcore.modules.customEnchants;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.modules.CoreModule;
import dev.lightdream.unchartedcore.modules.customEnchants.enchants.AntiDepthStrider;
import dev.lightdream.unchartedcore.modules.customEnchants.enchants.CactusProtection;
import dev.lightdream.unchartedcore.modules.customEnchants.enchants.ComboBoost;
import dev.lightdream.unchartedcore.modules.customEnchants.enchants.Critical;
import dev.lightdream.unchartedcore.utils.Utils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CustomEnchantsModule extends CoreModule {

    public static CustomEnchantsModule instance;
    //Enchants
    public Enchantment cactusProtection;
    public Enchantment comboBoost;
    public Enchantment antiDepthStrider;
    public Enchantment critical;

    public CustomEnchantsConfig settings;

    public List<String> enchantTargets = new ArrayList<>();

    public CustomEnchantsModule(Main plugin) {
        super(plugin, "CustomEnchantsModule");
        instance = this;
        for (EnchantmentTarget value : EnchantmentTarget.values()) {
            enchantTargets.add(value.toString());
        }
    }

    public static void enchantItem(ItemStack item, Enchantment enchantment, int level, boolean force) {
        if (enchantment.canEnchantItem(item) || force) {
            item.addUnsafeEnchantment(enchantment, level);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = item.getItemMeta().getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            List<String> toRemove = new ArrayList<>();
            for (String line : lore) {
                if (line.contains(enchantment.getName())) {
                    toRemove.add(line);
                }
            }
            toRemove.forEach(lore::remove);
            lore.add(Utils.color("&f" + enchantment.getName() + " " + level));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }

    @Override
    public Listener registerEventListeners() {
        return new CustomEnchantsEvents(plugin);
    }

    @Override
    public Command registerCommands() {
        return null;
    }

    @Override
    public void enable() {
        cactusProtection = new CactusProtection(CustomEnchantsModule.instance.settings.cactusProtection.id);
        comboBoost = new ComboBoost(CustomEnchantsModule.instance.settings.comboBoost.id);
        antiDepthStrider = new AntiDepthStrider(CustomEnchantsModule.instance.settings.antiDepthStrider.id);
        critical = new Critical(CustomEnchantsModule.instance.settings.critical.id);

        registerEnchant(cactusProtection);
        registerEnchant(comboBoost);
        registerEnchant(antiDepthStrider);
        registerEnchant(critical);
    }

    @Override
    public void disable() {

    }

    @Override
    public void loadConfigs() {
        settings = plugin.getFileManager().load(CustomEnchantsConfig.class);
    }

    private void registerEnchant(Enchantment enchantment) {
        try {
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Enchantment.registerEnchantment(enchantment);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Enchant " + enchantment.getName() + "'s id is already taken. Please select another id.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Enchantment getCustomEnchantment(String name) {
        switch (name) {
            case "CACTUS_PROTECTION":
                return cactusProtection;
            case "COMBO_BOOST":
                return comboBoost;
            case "ANTI_DEPTH_STRIDER":
                return antiDepthStrider;
            case "CRITICAL":
                return critical;
        }
        return null;
    }
}
