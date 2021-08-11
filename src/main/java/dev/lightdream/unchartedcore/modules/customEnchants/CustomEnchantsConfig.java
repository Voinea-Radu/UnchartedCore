package dev.lightdream.unchartedcore.modules.customEnchants;

import dev.lightdream.unchartedcore.modules.customEnchants.dto.CustomEnchant;
import dev.lightdream.unchartedcore.modules.customEnchants.dto.PotionCustomEnchant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomEnchantsConfig {

    public CustomEnchant cactusProtection = new CustomEnchant(1000, "Cactus Protection", 1, Arrays.asList("ARMOR", "SKULL", "BOOK"), 100, new ArrayList<>());
    public CustomEnchant comboBoost = new CustomEnchant(1001, "Combo Boost", 5, Arrays.asList("WEAPON", "BOOK"), 100, Arrays.asList("0.10", "2")); //critical bonus | combo time
    public CustomEnchant antiDepthStrider = new CustomEnchant(1002, "Anti Depth Strider", 1, Arrays.asList("BOW", "WEAPON", "BOOK"), 100, Arrays.asList("3", "2")); //slowness level | slowness duration
    public CustomEnchant critical = new CustomEnchant(1003, "Critical", 3, Arrays.asList("BOW", "WEAPON", "BOOK"), 100, Arrays.asList("0.10")); //slowness level | slowness duration
    public List<PotionCustomEnchant> potions = Arrays.asList(
            new PotionCustomEnchant("SPEED", PotionCustomEnchant.PotionEnchantScope.SELF, new CustomEnchant(1004, "Speed Boost", 3, Arrays.asList("WEAPON", "BOW", "BOOK"), 100, Arrays.asList("3", "1"))),
            new PotionCustomEnchant("SLOW", PotionCustomEnchant.PotionEnchantScope.ENEMY, new CustomEnchant(1005, "Slow", 3, Arrays.asList("WEAPON", "BOW", "BOOK"), 100, Arrays.asList("3", "1")))
    );


    public PotionCustomEnchant getPotionEnchant(String name) {
        for (PotionCustomEnchant potion : potions) {
            if (potion.potion == name) {
                return potion;
            }
        }
        return null;
    }
}
