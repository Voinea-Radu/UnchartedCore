package dev.lightdream.unchartedcore.modules.enchanting.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import sun.reflect.CallerSensitive;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class EnchantCategory {

    public String name;
    public List<Enchant> enchants;
    public List<String> targets;

}
