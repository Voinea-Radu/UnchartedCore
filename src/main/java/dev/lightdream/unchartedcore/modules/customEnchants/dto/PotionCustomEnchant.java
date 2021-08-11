package dev.lightdream.unchartedcore.modules.customEnchants.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PotionCustomEnchant {

    public String potion;
    public PotionEnchantScope scope;
    public CustomEnchant enchantSettings;

    public enum PotionEnchantScope{
        SELF, ENEMY
    }


}
