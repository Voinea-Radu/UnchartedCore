package dev.lightdream.unchartedcore.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.NoArgsConstructor;

@DatabaseTable(tableName = "factions_home_allows")
@NoArgsConstructor
public class FactionHomeAllow {

    @DatabaseField(canBeNull = false, generatedId = true, columnName = "id")
    public int id;
    @DatabaseField(columnName = "factions_id")
    public String factionId;
    @DatabaseField(columnName = "allow_id")
    public String allowId;

    public FactionHomeAllow(String factionId, String allowId){
        this.factionId = factionId;
        this.allowId = allowId;
    }

}
