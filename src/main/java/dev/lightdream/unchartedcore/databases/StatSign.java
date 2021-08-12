package dev.lightdream.unchartedcore.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.modules.stats.StatsModule;
import dev.lightdream.unchartedcore.utils.Utils;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

@NoArgsConstructor
@DatabaseTable(tableName = "stat_signs")
public class StatSign {
    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    public int id;
    @DatabaseField(columnName = "world")
    public String world;
    @DatabaseField(columnName = "x")
    public int x;
    @DatabaseField(columnName = "y")
    public int y;
    @DatabaseField(columnName = "z")
    public int z;
    @DatabaseField(columnName = "item")
    public String stat;

    /**
     * @param world World name
     * @param x     World coordinate X
     * @param y     World coordinate Y
     * @param z     World coordinate Z
     * @param stat  The stat wanted
     */
    public StatSign(String world, int x, int y, int z, String stat) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.stat = stat;
    }

    public StatSign(Location location, String stat) {
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.stat = stat;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public void process() {
        Main plugin = Main.instance;

        Block block = Bukkit.getWorld(this.world).getBlockAt(this.getLocation());
        if (!block.getType().equals(Material.SIGN) && !block.getType().equals(Material.WALL_SIGN)) {
            plugin.getLogger().warning("Stat sign at location " + getLocation() + " is not a sign");
            return;
        }
        if (StatsModule.instance.events.top.get(stat) == null) {
            return;
        }
        Sign sign = (Sign) block.getState();

        sign.setLine(0, Utils.color(plugin.getMessages().statsSignInfoLine));
        sign.setLine(1, Utils.color(plugin.getMessages().statsSignStatLine.replace("%stat%", stat)));
        sign.setLine(2, Utils.color(plugin.getMessages().statsSignPlayerLine.replace("%player%", StatsModule.instance.events.top.get(stat).name)));
        sign.setLine(3, Utils.color(plugin.getMessages().statsSignValueLine).replace("%value%", StatsModule.instance.events.getStat(StatsModule.instance.events.top.get(stat), stat, true)));

        sign.update(true);
    }
}
