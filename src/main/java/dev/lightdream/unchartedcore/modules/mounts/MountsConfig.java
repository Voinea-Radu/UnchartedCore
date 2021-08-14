package dev.lightdream.unchartedcore.modules.mounts;

import dev.lightdream.unchartedcore.files.dto.GUIConfig;
import dev.lightdream.unchartedcore.files.dto.GUIItem;
import dev.lightdream.unchartedcore.files.dto.Item;
import dev.lightdream.unchartedcore.files.dto.XMaterial;
import dev.lightdream.unchartedcore.modules.mounts.dto.Mount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MountsConfig {

    public HashMap<String, Mount> mountList = new HashMap<String, Mount>() {{
        put("Test1", new Mount("HORSE", 10, 20, "IRON_BARDING"));
        put("Test2", new Mount("UNDEAD_HORSE", 10, 20, null));
        put("Test3", new Mount("DONKEY", 10, 20, "IRON_BARDING"));
        put("Test4", new Mount("SKELETON_HORSE", 10, 20, "IRON_BARDING"));
        put("Test5", new Mount("MULE", 10, 20, null));
    }};

    public GUIConfig guiConfig = new GUIConfig("mounts", "CHEST", "Mounts", 6, 9, new Item(XMaterial.BLACK_STAINED_GLASS_PANE, 1), Arrays.asList(
            new GUIItem(new Item(XMaterial.STONE, 0, 1, "Test1", new ArrayList<>()), "Test1"),
            new GUIItem(new Item(XMaterial.STONE, 1, 1, "Test2", new ArrayList<>()), "Test2"),
            new GUIItem(new Item(XMaterial.STONE, 2, 1, "Test3", new ArrayList<>()), "Test3"),
            new GUIItem(new Item(XMaterial.STONE, 3, 1, "Test4", new ArrayList<>()), "Test4"),
            new GUIItem(new Item(XMaterial.STONE, 4, 1, "Test5", new ArrayList<>()), "Test5")
    ));


}
