package dev.lightdream.unchartedcore.modules.signshop;

import dev.lightdream.unchartedcore.files.dto.SignShopEntry;
import dev.lightdream.unchartedcore.files.dto.XMaterial;

import java.util.HashMap;

public class SignShopConfig {
    public int signShopRetention = 3;

    public int leftClick = 1;
    public int shiftLeftClick = 16;
    public int rightClick = 64;
    public int shiftRightClick = 100000;

    public HashMap<Integer, SignShopEntry> signShopEntries = new HashMap<Integer, SignShopEntry>() {{
        put(0, new SignShopEntry(XMaterial.DIRT, 10, 5));
        put(1, new SignShopEntry(XMaterial.STONE, 15, 7.5));
    }};


    public SignShopEntry getEntryByMaterial(String material) {
        for (SignShopEntry value : signShopEntries.values()) {
            if (value.material.toString().equalsIgnoreCase(material)) {
                return value;
            }
        }
        return null;
    }


}
