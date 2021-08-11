package dev.lightdream.unchartedcore.modules.anvil;

import dev.lightdream.unchartedcore.files.dto.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnvilConfig {

    public PluginLocation spawnRegionPos1 = new PluginLocation("world", -100, 0, -100);
    public PluginLocation spawnRegionPos2 = new PluginLocation("world", 100, 255, 100);

    public GUIConfig guiConfig = new GUIConfig("anvil", "CHEST", "Anvil", 3, 9, new Item(XMaterial.BLACK_STAINED_GLASS_PANE, 1), Arrays.asList(
            new GUIItem(new Item(XMaterial.AIR, 10, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.BARRIER, 12, 1, "Rename", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 14, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.AIR, 16, 1, "", new ArrayList<>()))
    ));

    public List<String> allowedAnvilItems = Arrays.asList(
            "ENCHANTED_BOOK",
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


}
