package dev.lightdream.unchartedcore.files.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class GUIItem {

    public Item item;
    public List<String> args = new ArrayList<>();

    public GUIItem(Item item, String... args) {
        this.item = item;
        this.args.addAll(Arrays.asList(args));
    }

    @Override
    public String toString() {
        return "GUIItem{" +
                "item=" + item +
                ", args=" + args +
                '}';
    }
}
