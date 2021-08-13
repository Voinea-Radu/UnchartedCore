package dev.lightdream.unchartedcore.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.lightdream.unchartedcore.Main;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

@SuppressWarnings("unused")
@NoArgsConstructor
@DatabaseTable(tableName = "users")
public class User {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    public Integer id;
    @DatabaseField(columnName = "uuid", unique = true)
    public UUID uuid;
    @DatabaseField(columnName = "name", unique = true)
    public String name;
    @DatabaseField(columnName = "kills")
    public Integer kills;
    @DatabaseField(columnName = "deaths")
    public Integer deaths;
    @DatabaseField(columnName = "online_time")
    public Long onlineTime;
    @DatabaseField(columnName = "traveled_distance")
    public Double traveledDistance;
    @DatabaseField(columnName = "join_date")
    public Long joinDate;
    @DatabaseField(columnName = "head_sold")
    public int headSold;
    @DatabaseField(columnName = "extra_homes")
    public int extraHomes;

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.kills = 0;
        this.deaths = 0;
        this.onlineTime = 0L;
        this.traveledDistance = 0.0;
        this.joinDate = System.currentTimeMillis();
        this.headSold = 0;
        this.extraHomes = 0;
    }

    public int getMaxHomeCount() {
        int homes = this.extraHomes;
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String basePerm = "uc.homes.";

        for (int i = 0; i < 100; i++) {
            if (Main.instance.getPermissions().has(Bukkit.getWorlds().get(0), player.getName(), basePerm + " " + i)) {
                homes += i;
            }
        }
        return homes;
    }

}
