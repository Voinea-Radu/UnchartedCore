package dev.lightdream.unchartedcore.modules.customEnchants.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class CustomEnchant {

    public int id;
    public String name;
    public int maxLevel;
    public List<String> target;
    public double chance;
    public List<String> args;

}
