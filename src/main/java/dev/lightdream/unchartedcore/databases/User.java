package dev.lightdream.unchartedcore.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.NoArgsConstructor;

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

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.kills = 0;
        this.deaths = 0;
        this.onlineTime = 0L;
        this.traveledDistance = 0.0;
        this.joinDate = System.currentTimeMillis();
    }

}
