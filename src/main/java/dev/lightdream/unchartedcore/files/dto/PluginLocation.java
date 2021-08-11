package dev.lightdream.unchartedcore.files.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@SuppressWarnings("unused")
@NoArgsConstructor
@AllArgsConstructor
public class PluginLocation {

    public String world;
    public float x;
    public float y;
    public float z;

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

}
