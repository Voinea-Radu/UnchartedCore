package dev.lightdream.unchartedcore.files.dto;

import dev.lightdream.unchartedcore.files.dto.Item;
import dev.lightdream.unchartedcore.files.dto.XMaterial;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class GUIConfig {

    public String id;
    public String type = "CHEST";
    public String title;
    public int rows = 6;
    public int columns = 9;
    public Item fillItem = new Item(XMaterial.GLASS_PANE, 1, "", new ArrayList<>());
    public List<GUIItem> items;


}