package dev.lightdream.unchartedcore.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

@DatabaseTable(tableName = "homes")
@NoArgsConstructor
public class Home {

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
    @DatabaseField(columnName = "owner")
    public UUID owner;
    @DatabaseField(columnName = "name")
    public String name;

    public Home(String world, Integer x, Integer y, Integer z, UUID owner, String name) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.owner = owner;
        this.name = name;
    }

    public Home(Location location, UUID owner, String name) {
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.owner = owner;
        this.name = name;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }


}
