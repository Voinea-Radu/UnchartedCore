package dev.lightdream.unchartedcore.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.files.dto.SignShopEntry;
import dev.lightdream.unchartedcore.files.dto.XMaterial;
import dev.lightdream.unchartedcore.modules.signshop.SignShopModule;
import dev.lightdream.unchartedcore.utils.Utils;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

@DatabaseTable(tableName = "sign_shops")
@NoArgsConstructor
public class SignShop {

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
    @DatabaseField(columnName = "type")
    public String type;
    @DatabaseField(columnName = "item")
    public int itemId;

    /**
     * @param world World name
     * @param x     World coordinate X
     * @param y     World coordinate Y
     * @param z     World coordinate Z
     * @param type  SELL/BUY
     */
    public SignShop(String world, int x, int y, int z, String type, int id) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.itemId = id;
    }

    public SignShop(Location location, String type, int id) {
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.type = type;
        this.itemId = id;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    /**
     * @return SignShopEntry containing buy/sell price and material
     */
    public SignShopEntry getDetails() {
        try {
            return SignShopModule.instance.settings.signShopEntries.getOrDefault(itemId, null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void process() {
        Main plugin = Main.instance;
        SignShopEntry data = this.getDetails();
        if (data == null) {
            plugin.getLogger().warning("Sign shop at location " + getLocation() + " has no config associated to it");
            return;
        }
        Block block = Bukkit.getWorld(this.world).getBlockAt(this.getLocation());
        if (!block.getType().equals(Material.SIGN) && !block.getType().equals(Material.WALL_SIGN)) {
            plugin.getLogger().warning("Sign shop at location " + getLocation() + " is not a sign");
            return;
        }
        Sign sign = (Sign) block.getState();

        switch (this.type) {
            case "BUY":
                sign.setLine(0, Utils.color(plugin.getMessages().signBuy));
                sign.setLine(2, Utils.color(plugin.getMessages().signPrice.replace("%price%", String.valueOf(data.buyPrice))));
                sign.setLine(3, Utils.color(plugin.getMessages().signBuyInfo));
                break;
            case "SELL":
                sign.setLine(0, Utils.color(plugin.getMessages().signSell));
                sign.setLine(2, Utils.color(plugin.getMessages().signPrice.replace("%price%", String.valueOf(data.sellPrice))));
                sign.setLine(3, Utils.color(plugin.getMessages().signSellInfo));
                break;
        }
        if (data.material == XMaterial.SPAWNER) {
            sign.setLine(1, Utils.color(plugin.getMessages().signItem.replace("%item%", data.material.toString() + " " + data.data)));
        } else {
            sign.setLine(1, Utils.color(plugin.getMessages().signItem.replace("%item%", data.material.toString())));
        }
        sign.update(true);
    }
}
