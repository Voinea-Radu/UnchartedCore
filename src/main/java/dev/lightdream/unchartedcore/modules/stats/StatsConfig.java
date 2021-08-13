package dev.lightdream.unchartedcore.modules.stats;

import dev.lightdream.unchartedcore.files.dto.GUIConfig;
import dev.lightdream.unchartedcore.files.dto.GUIItem;
import dev.lightdream.unchartedcore.files.dto.Item;
import dev.lightdream.unchartedcore.files.dto.XMaterial;

import java.util.Arrays;

public class StatsConfig {

    public int topCalculate = 30 * 60;
    public int killCoolDown = 30;
    public GUIConfig guiConfig = new GUIConfig("stats_gui", "CHEST", "Stats", 3, 9, new Item(XMaterial.BLACK_STAINED_GLASS_PANE, 1), Arrays.asList(
            new GUIItem(new Item(XMaterial.STONE, 0, 1, "Kills", Arrays.asList("%kills%"))),
            new GUIItem(new Item(XMaterial.STONE, 1, 1, "Deaths", Arrays.asList("%deaths%"))),
            new GUIItem(new Item(XMaterial.STONE, 2, 1, "Join Date", Arrays.asList("%join_date%"))),
            new GUIItem(new Item(XMaterial.STONE, 3, 1, "Traveled", Arrays.asList("%traveled%"))),
            new GUIItem(new Item(XMaterial.STONE, 4, 1, "Online Time", Arrays.asList("%online_time%")))
    ));

}
