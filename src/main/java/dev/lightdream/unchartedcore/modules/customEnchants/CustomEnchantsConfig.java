package dev.lightdream.unchartedcore.modules.customEnchants;

import dev.lightdream.unchartedcore.modules.customEnchants.dto.CustomEnchant;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomEnchantsConfig {

    public CustomEnchant cactusProtection = new CustomEnchant(1000, "Cactus Protection", 1, Arrays.asList("ARMOR", "PLAYER_HEAD"), 100, new ArrayList<>());
    public CustomEnchant comboBoost = new CustomEnchant(1001, "Combo Boost", 5, Arrays.asList("WEAPON"), 100, Arrays.asList("0.10", "2")); //critical bonus | combo time
    public CustomEnchant antiDepthStrider = new CustomEnchant(1002, "Anti Depth Strider", 1, Arrays.asList("BOW, WEAPON"), 100, Arrays.asList("3", "2")); //slowness level | slowness duration
    public CustomEnchant critical = new CustomEnchant(1003, "Critical", 3, Arrays.asList("BOW", "WEAPON"), 100, Arrays.asList("0.10")); //slowness level | slowness duration

}
