package dev.lightdream.unchartedcore.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@DatabaseTable(tableName = "factions_home_allows")
@NoArgsConstructor
public class CactusHopper {

    @DatabaseField(canBeNull = false, generatedId = true, columnName = "id")
    public int id;
    @DatabaseField(columnName = "world")
    public String world;
    @DatabaseField(columnName = "x")
    public Integer x;
    @DatabaseField(columnName = "y")
    public Integer y;
    @DatabaseField(columnName = "z")
    public Integer z;


    public CactusHopper(String world, Integer x, Integer y, Integer z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CactusHopper(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }
}
