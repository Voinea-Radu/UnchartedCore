package dev.lightdream.unchartedcore.modules.enchanting;

import dev.lightdream.unchartedcore.files.dto.*;
import dev.lightdream.unchartedcore.modules.enchanting.dto.Enchant;
import dev.lightdream.unchartedcore.modules.enchanting.dto.EnchantCategory;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class EnchantingConfig {

    public PluginLocation spawnRegionPos1 = new PluginLocation("world", -100, 0, -100);
    public PluginLocation spawnRegionPos2 = new PluginLocation("world", 100, 255, 100);
    public List<EnchantCategory> enchantCategories = Arrays.asList(
            new EnchantCategory("Protection", Arrays.asList(
                    new Enchant("Protection", "PROTECTION_ENVIRONMENTAL", 1, 50),
                    new Enchant("Protection", "PROTECTION_ENVIRONMENTAL", 2, 100),
                    new Enchant("Protection", "PROTECTION_ENVIRONMENTAL", 3, 150),
                    new Enchant("Protection", "PROTECTION_ENVIRONMENTAL", 4, 200),
                    new Enchant("Protection", "PROTECTION_ENVIRONMENTAL", 5, 250)
            ), Arrays.asList("HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS")),
            new EnchantCategory("Efficiency", Arrays.asList(
                    new Enchant("Efficiency", "DIG_SPEED", 1, 50),
                    new Enchant("Efficiency", "DIG_SPEED", 2, 100),
                    new Enchant("Efficiency", "DIG_SPEED", 3, 150),
                    new Enchant("Efficiency", "DIG_SPEED", 4, 200),
                    new Enchant("Efficiency", "DIG_SPEED", 5, 250)
            ), Arrays.asList("PICKAXE")),
            new EnchantCategory("Cactus Protection", Arrays.asList(
                    new Enchant("Cactus Protection", "CACTUS_PROTECTION", 1, 50)
            ), Arrays.asList("HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS", "SKULL")),
            new EnchantCategory("Combo Boost", Arrays.asList(
                    new Enchant("Combo Boost", "COMBO_BOOST", 1, 50),
                    new Enchant("Combo Boost", "COMBO_BOOST", 2, 100),
                    new Enchant("Combo Boost", "COMBO_BOOST", 3, 150),
                    new Enchant("Combo Boost", "COMBO_BOOST", 4, 200),
                    new Enchant("Combo Boost", "COMBO_BOOST", 5, 250)
            ), Arrays.asList("SWORD")),
            new EnchantCategory("Anti Depth Strider", Arrays.asList(
                    new Enchant("Anti Depth Strider", "ANTI_DEPTH_STRIDER", 1, 50)
            ), Arrays.asList("SWORD", "BOW")),
            new EnchantCategory("Critical", Arrays.asList(
                    new Enchant("Critical", "CRITICAL", 1, 50),
                    new Enchant("Critical", "CRITICAL", 2, 100),
                    new Enchant("Critical", "CRITICAL", 3, 150)
            ), Arrays.asList("SWORD", "BOW"))
    );
    public GUIConfig guiConfig = new GUIConfig("enchant_table", "CHEST", "Enchant Table", 6, 9, new Item(XMaterial.BLACK_STAINED_GLASS_PANE, 1), Arrays.asList(
            new GUIItem(new Item(XMaterial.AIR, 10, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 12, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 13, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 14, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 15, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 16, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 21, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 22, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 23, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 24, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 25, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 30, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 31, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 32, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 33, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 34, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 39, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 40, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 41, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 42, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 43, 1, "", new ArrayList<>()))
    ));

    public Item enchantItem = new Item(XMaterial.ENCHANTED_BOOK, 1, "%enchant% %level%", Arrays.asList("%xp%"));
    public Item enchantCategoryItem = new Item(XMaterial.ENCHANTED_BOOK, 1, "%enchant%", new ArrayList<>());

    public List<String> allowedEnchantItems = Arrays.asList(
            "PICKAXE",
            "AXE",
            "SWORD",
            "HELMET",
            "CHESTPLATE",
            "LEGGINGS",
            "BOOTS",
            "SHOVEL",
            "BOW",
            "SKULL"
    );


    public List<EnchantCategory> getEnchantCategories(String target) {
        List<EnchantCategory> output = new ArrayList<>();
        for (EnchantCategory enchantCategory : enchantCategories) {
            for (String enchantTarget : enchantCategory.targets) {
                if (target.contains(enchantTarget)) {
                    output.add(enchantCategory);
                    break;
                }
            }
        }
        return output;
    }
}
