package dev.lightdream.unchartedcore.modules.customEnchants.enchants;

import dev.lightdream.unchartedcore.modules.customEnchants.CustomEnchantsModule;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class ComboBoost extends Enchantment {
    public ComboBoost(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return CustomEnchantsModule.instance.settings.comboBoost.name;
    }

    @Override
    public int getMaxLevel() {
        return CustomEnchantsModule.instance.settings.comboBoost.maxLevel;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        for (String target : CustomEnchantsModule.instance.settings.comboBoost.target) {
            if (CustomEnchantsModule.instance.enchantTargets.contains(target)) {
                if (EnchantmentTarget.valueOf(target).includes(item.getType())) {
                    return true;
                }
            } else {
                if (item.getType().toString().contains(target)) {
                    return true;
                }
            }
        }
        return false;
    }
}
