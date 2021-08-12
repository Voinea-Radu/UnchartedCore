package dev.lightdream.unchartedcore.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

@DatabaseTable(tableName = "players_heads")
@NoArgsConstructor
public class PlayerHead {

    @DatabaseField(canBeNull = false, generatedId = true, columnName = "id")
    public int id;
    @DatabaseField(columnName = "owner")
    public UUID owner;
    @DatabaseField(columnName = "world")
    public String world;
    @DatabaseField(columnName = "x")
    public int x;
    @DatabaseField(columnName = "y")
    public int y;
    @DatabaseField(columnName = "z")
    public int z;

    public PlayerHead(UUID owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public void setLocation(Location location) {
        if (location == null) {
            this.world = "";
            return;
        }
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public boolean onLocation(Location location) {
        if (world == null) {
            return false;
        }
        if (world.equals("")) {
            return false;
        }
        return location.getWorld().getName().equals(world) && location.getBlockX() == x && location.getBlockY() == y && location.getZ() == z;
    }

}
