package dev.lightdream.unchartedcore.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@DatabaseTable(tableName = "kits")
@NoArgsConstructor
public class Kit {

    @DatabaseField(canBeNull = false, generatedId = true, columnName = "id")
    public int id;
    @DatabaseField(columnName = "name")
    public String name;
    @DatabaseField(columnName = "kits")
    public String kits;

    public Kit(String name, String kits) {
        this.name = name;
        this.kits = kits;
    }

    public List<ItemStack> getKit(int level) throws IOException {
        List<String> kits = Arrays.asList(this.kits.split(" "));
        String kit = kits.get(Math.min(level - 1, kits.size() - 1));
        return ItemBuilder.itemStackArrayFromBase64(kit);
    }

}
