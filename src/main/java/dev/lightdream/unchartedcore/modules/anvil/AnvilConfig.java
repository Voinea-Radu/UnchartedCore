package dev.lightdream.unchartedcore.modules.anvil;

import dev.lightdream.unchartedcore.files.dto.GUIConfig;
import dev.lightdream.unchartedcore.files.dto.GUIItem;
import dev.lightdream.unchartedcore.files.dto.Item;
import dev.lightdream.unchartedcore.files.dto.XMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnvilConfig {

    public GUIConfig guiConfig = new GUIConfig("anvil", "CHEST", "Anvil", 3, 9, new Item(XMaterial.BLACK_STAINED_GLASS_PANE, 1), Arrays.asList(
            new GUIItem(new Item(XMaterial.AIR, 10, 1, "", new ArrayList<>())),
            new GUIItem(new Item(XMaterial.BARRIER, 12, 1, "", new ArrayList<>())),
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
