package dev.lightdream.unchartedcore.modules.cactusHopper;

import dev.lightdream.unchartedcore.files.dto.Item;
import dev.lightdream.unchartedcore.files.dto.XMaterial;

import java.util.ArrayList;

public class CactusHopperConfig {

    public int range;
    public Item hopper = new Item(XMaterial.HOPPER, 1, "Cactus Hopper", new ArrayList<>());

}
